package com.taobao.arthas.boot;

import com.taobao.arthas.core.shell.ShellServerOptions;

public class GlobalConfig {
	public static  final String DEFAULT_TARGET_IP = "127.0.0.1";
	public static  final int DEFAULT_TELNET_PORT = 3658;
	public static  final int DEFAULT_HTTP_PORT = 8563;
	public static  final long DEFAULT_SESSION_TIMEOUT_SECONDS = ShellServerOptions.DEFAULT_SESSION_TIMEOUT / 1000;
	
	public static final String MANAGE_SERVER_URL_BASE = "http://127.0.0.1:9009";

	public static  final String JAR_URL_BASE = "https://github.com/jindongzhao/arthas/blob/zeus/lib/3.1.1/";
	public static  final String JAR_URL_AGENT = JAR_URL_BASE + "arthas-agent-3.1.1.jar?raw=true";
	public static  final String JAR_URL_CORE = JAR_URL_BASE + "arthas-core-3.1.1.jar?raw=true";
	public static  final String JAR_URL_SPY = JAR_URL_BASE + "arthas-spy-3.1.1.jar?raw=true";

	public static String zeusPath = System.getProperty("user.home");
	public static  String jarPathAgent;
	public static  String jarPathCore;
	public static  String jarPathSpy;
	
	public static final int CONN_TELNET_PORT = 3999;
	public static final int CMD_TELNET_PORT = 5999;
	
	/**
	 * 注册成功后生成的appClientId
	 */
	public static Long appClientId;
}
