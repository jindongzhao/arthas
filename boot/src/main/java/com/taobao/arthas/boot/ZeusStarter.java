package com.taobao.arthas.boot;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.alibaba.fastjson.JSON;
import com.taobao.arthas.agent.AgentBootstrap;
import com.taobao.arthas.common.AnsiLog;
import com.taobao.arthas.common.HttpUtils;
import com.taobao.arthas.common.dto.HeartBeatReqDto;
import com.taobao.arthas.common.dto.HeartBeatRespDto;
import com.taobao.arthas.core.Arthas;
import com.taobao.arthas.core.shell.ShellServerOptions;

/**
 * 启动类
 * 
 * @author zhaojindong
 *
 */
public class ZeusStarter {

	private final String DEFAULT_TARGET_IP = "127.0.0.1";
	private final int DEFAULT_TELNET_PORT = 3658;
	private final int DEFAULT_HTTP_PORT = 8563;
	public final long DEFAULT_SESSION_TIMEOUT_SECONDS = ShellServerOptions.DEFAULT_SESSION_TIMEOUT / 1000;

	private final String URL_BASE = "http://127.0.0.1:8080";
	private final String URL_HEART_BEAT = URL_BASE + "/heartBeat/isAlive";

	// 启动线程，建立心跳连接
	public void init() {
		Thread zeusThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						HeartBeatReqDto reqDto = new HeartBeatReqDto();
						reqDto.setAppIp(getAppIp());
						reqDto.setAppStartCmd(getAppCmd());

						System.out.println("发送心跳");
						String heartBeatResp = HttpUtils.doPost(URL_HEART_BEAT, HttpUtils.generateRequestParam(reqDto));
						HeartBeatRespDto respDto = JSON.parseObject(heartBeatResp, HeartBeatRespDto.class);
						System.out.println("收到心跳反馈：" + JSON.toJSONString(respDto));

						// 带有执行命令
						if (respDto.getCommandList() != null) {
							doCommand(respDto.getCommandList());
						}

						Thread.sleep(1000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		zeusThread.setName("Zeus-heart-beating-thread");
		zeusThread.start();
	}

	/**
	 * 执行attach
	 * 
	 * @Description
	 * @param
	 * @return
	 * @throws @author:
	 *             zhaojindong @date: 14 Jun 2019 18:05:29
	 */
	public void doAttach() {
		// classPath
		List<String> attachArgs = new ArrayList<String>();

		attachArgs.add("-jar");
		attachArgs.add(getJarFilePath(Arthas.class));

		attachArgs.add("-pid");
		Integer pid = getCurrentPid();
		attachArgs.add("" + pid);

		attachArgs.add("-target-ip");
		attachArgs.add(DEFAULT_TARGET_IP);

		attachArgs.add("-telnet-port");
		attachArgs.add("" + DEFAULT_TELNET_PORT);

		attachArgs.add("-http-port");
		attachArgs.add("" + DEFAULT_HTTP_PORT);

		attachArgs.add("-core");
		attachArgs.add(getJarFilePath(Arthas.class));

		attachArgs.add("-agent");
		attachArgs.add(getJarFilePath(AgentBootstrap.class));

		attachArgs.add("-session-timeout");
		attachArgs.add("" + DEFAULT_SESSION_TIMEOUT_SECONDS);

		AnsiLog.debug("Start attach. args: " + attachArgs);

		// zjd 启动arthas进程
		ProcessUtils.startArthasCore(pid, attachArgs);

		AnsiLog.info("finish attach.", pid);
	}

	private String getJarFilePath(Class clazz) {
		return new File(clazz.getProtectionDomain().getCodeSource().getLocation().getPath()).getAbsolutePath();
	}

	private Integer getCurrentPid() {
		RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
		return Integer.valueOf(runtimeMXBean.getName().split("@")[0]);
	}

	private String getAppIp() {
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		byte[] ipAddr = addr.getAddress();
		String ipAddrStr = "";
		for (int i = 0; i < ipAddr.length; i++) {
			if (i > 0) {
				ipAddrStr += ".";
			}
			ipAddrStr += ipAddr[i] & 0xFF;
		}
		return ipAddrStr;
	}

	private String getAppCmd() {
		if(isWindowsOs()) {
			return "java -jar app.jar" + " "+new Random().nextInt(2);
		}else {
			BufferedReader br = null;
			try {
				String cmd = "ps aux | grep " + getCurrentPid();
				Process p = Runtime.getRuntime().exec(cmd);
				br = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String line = null;
				StringBuilder sb = new StringBuilder();
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				return sb.toString();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			return null;
		}
	}

	private void doCommand(List<String> commandList) {
		for(String command : commandList) {
			if (command.equals("attach")) {
				// TODO attach 之后，需要通知manager
				System.out.println("do attach...");
				doAttach();
				return;
			}
		}
	}

	public static void main(String[] args) {
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		byte[] ipAddr = addr.getAddress();
		String ipAddrStr = "";
		for (int i = 0; i < ipAddr.length; i++) {
			if (i > 0) {
				ipAddrStr += ".";
			}
			ipAddrStr += ipAddr[i] & 0xFF;
		}
		System.out.println(ipAddrStr.toString());
	}
	
	private static boolean isWindowsOs() {
		String os = System.getProperty("os.name");  
		return os.toLowerCase().startsWith("win"); 
	}
}
