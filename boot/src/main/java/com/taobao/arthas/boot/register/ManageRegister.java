package com.taobao.arthas.boot.register;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import com.alibaba.fastjson.JSON;
import com.taobao.arthas.boot.GlobalConfig;
import com.taobao.arthas.common.ManageRpcUtil;
import com.taobao.arthas.common.dto.HeartBeatRespDto;
import com.taobao.arthas.common.dto.ManageBaseDto;
import com.taobao.arthas.common.dto.RegisterDto;
import com.taobao.arthas.common.log.ArthasLogUtil;
import com.taobao.middleware.logger.Logger;

/**
 * 管理服务注册类
 * 
 * @author zhaojindong
 *
 */
public class ManageRegister {
	private static final Logger logger = ArthasLogUtil.getArthasLogger();
	private static final String URL_REGISTER = GlobalConfig.MANAGE_SERVER_URL_BASE + "/client/register";
	
	/**
	 * 向manage server注册自己
	 * 
	 * @Description
	 * @param
	 * @return
	 * @throws @author:
	 *             zhaojindong @date: 23 Jul 2019 20:19:01
	 */
	public static void register() {
		RegisterDto registerDto = new RegisterDto();
		registerDto.setAppIp(getAppIp());
		registerDto.setAppStartCmd(getAppCmd());
		registerDto.setPid(getCurrentPid());
		registerDto.setConnTelnetPort(GlobalConfig.CONN_TELNET_PORT);
		registerDto.setCmdTelnetPort(GlobalConfig.CMD_TELNET_PORT);
		System.out.println("注册自己");
		logger.debug("register to manage server ...");
		ManageBaseDto manageBaseDto = ManageRpcUtil.sendManageRequest(URL_REGISTER, registerDto, ManageBaseDto.class);
		logger.debug("finish register to manage server: " + JSON.toJSONString(manageBaseDto));
	}

	private static Integer getCurrentPid() {
		RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
		return Integer.valueOf(runtimeMXBean.getName().split("@")[0]);
	}

	private static String getAppIp() {
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

	private static String getAppCmd() {
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
