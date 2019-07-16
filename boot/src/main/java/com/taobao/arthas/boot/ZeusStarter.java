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
import com.taobao.arthas.common.AnsiLog;
import com.taobao.arthas.common.ManageRespsCodeEnum;
import com.taobao.arthas.common.ManageRpcCommandEnum;
import com.taobao.arthas.common.ManageRpcUtil;
import com.taobao.arthas.common.dto.HeartBeatReqDto;
import com.taobao.arthas.common.dto.HeartBeatRespDto;
import com.taobao.arthas.common.dto.ManageTaskDto;
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

	private final String JAR_URL_BASE = "https://github.com/jindongzhao/arthas/blob/zeus/lib/3.1.1/";
	private final String JAR_URL_AGENT = JAR_URL_BASE + "arthas-agent-3.1.1.jar?raw=true";
	private final String JAR_URL_CORE = JAR_URL_BASE + "arthas-core-3.1.1.jar?raw=true";
	private final String JAR_URL_SPY = JAR_URL_BASE + "arthas-spy-3.1.1.jar?raw=true";

	private String zeusPath = System.getProperty("user.home");
	private String jarPathAgent;
	private String jarPathCore;
	private String jarPathSpy;

	public static void main(String[] args) {
		ZeusStarter zeusStarter = new ZeusStarter();
		zeusStarter.init("3.1.1");
	}

	/**
	 * 启动线程
	 * 
	 * @Description
	 * @param zeusVersion
	 *            版本号
	 * @return
	 * @throws @author:
	 *             zhaojindong @date: 2 Jul 2019 11:44:11
	 */
	public void init(final String zeusVersion) {
		Thread zeusThread = new Thread(new Runnable() {

			@Override
			public void run() {
				zeusPath = zeusPath + "/zeus/" + zeusVersion + "/";
				jarPathAgent = zeusPath + "arthas-agent.jar";
				jarPathCore = zeusPath + "arthas-core.jar";
				jarPathSpy = zeusPath + "arthas-spy.jar";

				// 下载zeus需要的jar到user.home下
				downloadJars();

				while (true) {
					try {
						HeartBeatReqDto reqDto = new HeartBeatReqDto();
						reqDto.setAppIp(getAppIp());
						reqDto.setPid(getCurrentPid());
						reqDto.setAppStartCmd(getAppCmd());

						System.out.println("heartbeating...");
						HeartBeatRespDto respDto = ManageRpcUtil.sendRManageRequest(URL_HEART_BEAT, reqDto, HeartBeatRespDto.class);
						System.out.println("heartbeating echo:" + JSON.toJSONString(respDto));

						// 心跳返回结果中带有事务，执行
						if (ManageRespsCodeEnum.SUCCESS.getCode().equals(respDto.getResultCode())
								&& respDto.getTaskDtoList() != null) {
							for(ManageTaskDto txDto : respDto.getTaskDtoList()) {
								processTx(txDto);
							}
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
	 * 执行事务
	* @Description 
	* @param 
	* @return 
	* @throws 
	* @author: zhaojindong  @date: 15 Jul 2019 20:45:23
	 */
	private void processTx(ManageTaskDto transactionDto) {
		if (transactionDto.getCommand().equals(ManageRpcCommandEnum.COMMAND_ATTACH.getCode())) {
			// TODO attach 之后，需要通知manager
			System.out.println("do attach...");
			doAttachCommand();
			return;
		}
	}

	/**
	 * 执行attach命令
	 * 
	 * @Description
	 * @param
	 * @return
	 * @throws @author:
	 *             zhaojindong @date: 14 Jun 2019 18:05:29
	 */
	public void doAttachCommand() {
		// classPath
		List<String> attachArgs = new ArrayList<String>();

		attachArgs.add("-jar");
		attachArgs.add(new File(jarPathCore).getAbsolutePath());

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
		attachArgs.add(new File(jarPathCore).getAbsolutePath());

		attachArgs.add("-agent");
		attachArgs.add(new File(jarPathAgent).getAbsolutePath());

		attachArgs.add("-session-timeout");
		attachArgs.add("" + DEFAULT_SESSION_TIMEOUT_SECONDS);

		AnsiLog.debug("Start attach. args: " + attachArgs);

		// zjd 启动arthas进程
		System.out.println("执行attach,参数:" + JSON.toJSONString(attachArgs));
		ProcessUtils.startArthasCore(pid, attachArgs);

		AnsiLog.info("finish attach.", pid);
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
		if (isWindowsOs()) {
			return "java -jar app.jar" + " " + new Random().nextInt(2);
		} else {
			BufferedReader br = null;
			try {
				String cmd = "ps -x -p " + getCurrentPid();
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

	/**
	 * 下载attach需要的jar，存放在user.home目录下
	 * 
	 * @Description
	 * @param
	 * @return
	 * @throws @author:
	 *             zhaojindong @date: 2 Jul 2019 11:55:23
	 */
	private void downloadJars() {
		// 判断文件是否存在
		File zeusDir = new File(zeusPath);
		if (!zeusDir.exists()) {
			zeusDir.mkdirs();

			// 下载jar
			try {
				DownloadUtils.saveUrl(jarPathAgent, JAR_URL_AGENT, true);
				DownloadUtils.saveUrl(jarPathCore, JAR_URL_CORE, true);
				DownloadUtils.saveUrl(jarPathSpy, JAR_URL_SPY, true);

				/*
				 * HttpUtils.downloadFile(JAR_URL_AGENT, jarPathAgent);
				 * HttpUtils.downloadFile(JAR_URL_CORE, jarPathCore);
				 * HttpUtils.downloadFile(JAR_URL_SPY, jarPathSpy);
				 */
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static boolean isWindowsOs() {
		String os = System.getProperty("os.name");
		return os.toLowerCase().startsWith("win");
	}

}
