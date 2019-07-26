package com.taobao.arthas.boot.processor;

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
import com.taobao.arthas.boot.GlobalConfig;
import com.taobao.arthas.boot.ProcessUtils;
import com.taobao.arthas.common.AnsiLog;
import com.taobao.arthas.common.ManageRpcCommandEnum;
import com.taobao.arthas.common.log.ArthasLogUtil;
import com.taobao.middleware.logger.Logger;

public class ManageCommandHandler {
	private static final Logger logger = ArthasLogUtil.getArthasClientLogger();
	/**
	 * 处理 manage server发送过来的命令
	 * 
	 * @Description
	 * @param
	 * @return
	 * @throws @author:
	 *             zhaojindong @date: 23 Jul 2019 20:09:43
	 */
	public String handleCommand(String command) {
		if (ManageRpcCommandEnum.COMMAND_ATTACH.getCode().equals(command)) {
			// command: attach
			doAttachCommand();
		}
		return null;
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
		attachArgs.add(new File(GlobalConfig.jarPathCore).getAbsolutePath());

		attachArgs.add("-pid");
		Integer pid = getCurrentPid();
		attachArgs.add("" + pid);

		attachArgs.add("-target-ip");
		attachArgs.add(GlobalConfig.DEFAULT_TARGET_IP);

		attachArgs.add("-telnet-port");
		attachArgs.add("" + GlobalConfig.DEFAULT_TELNET_PORT);

		//不启用http server
		/*attachArgs.add("-http-port");
		attachArgs.add("" + GlobalConfig.DEFAULT_HTTP_PORT);*/

		attachArgs.add("-core");
		attachArgs.add(new File(GlobalConfig.jarPathCore).getAbsolutePath());

		attachArgs.add("-agent");
		attachArgs.add(new File(GlobalConfig.jarPathAgent).getAbsolutePath());

		attachArgs.add("-session-timeout");
		attachArgs.add("" + GlobalConfig.DEFAULT_SESSION_TIMEOUT_SECONDS);
		
		//app client id
		attachArgs.add("-app-client-id");
		attachArgs.add("" + GlobalConfig.appClientId);

		AnsiLog.debug("Start attach. args: " + attachArgs);

		// zjd 启动arthas进程
		logger.info("执行attach,参数:" + JSON.toJSONString(attachArgs));
		ProcessUtils.startArthasCore(pid, attachArgs);

		logger.info("finish attach.", pid);
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
		BufferedReader br = null;
		try {
			Integer pid = getCurrentPid();
			String cmd = "ps -X -p " + pid + " | grep " + pid;
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
