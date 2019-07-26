package com.taobao.arthas.manage.client;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.telnet.TelnetClient;
import org.apache.commons.net.telnet.TelnetOptionHandler;
import org.apache.commons.net.telnet.WindowSizeOptionHandler;

/**
 * 命令发送类实现
 * 
 * @author zhaojindong
 *
 */
public class TelnetCommandSender implements ICommandSender {
	private static final String PROMPT = "$"; // 命令返回的结束字符
	private static final int DEFAULT_CONNECTION_TIMEOUT = 5000; // 5000 ms
	private static final int DEFAULT_BUFFER_SIZE = 1024;

	private String targetIp;
	private Integer telnetPort;

	public TelnetCommandSender(String targetIp, Integer telnetPort) {
		this.targetIp = targetIp;
		this.telnetPort = telnetPort;
	}

	public String sendCommand(String cmd) {
		// zjd 客户端
		final TelnetClient telnet = new TelnetClient();
		telnet.setConnectTimeout(DEFAULT_CONNECTION_TIMEOUT);

		// send init terminal size
		TelnetOptionHandler sizeOpt = new WindowSizeOptionHandler(64, 32, true, true, false, false);
		try {
			telnet.addOptionHandler(sizeOpt);
			// zjd 连接到服务端
			telnet.connect(targetIp, telnetPort);
			InputStream inputStream = telnet.getInputStream();
			OutputStream outputStream = telnet.getOutputStream();

			// send command to server
			outputStream.write((cmd + " | plaintext\n").getBytes());
			outputStream.flush();
			// read result from server and output
			String response = readUntil(inputStream, PROMPT);
			System.out.print("收到命令行返回："+response);

			telnet.disconnect();

			return response;
		} catch (Exception e) {
			System.out.println("Connect to telnet server error: " + targetIp + " " + telnetPort);
		}
		return null;
	}

	private static String readUntil(InputStream in, String prompt) {
		try {
			StringBuilder sBuffer = new StringBuilder();
			byte[] b = new byte[DEFAULT_BUFFER_SIZE];
			while (true) {
				int size = in.read(b);
				if (-1 != size) {
					sBuffer.append(new String(b, 0, size));
					String data = sBuffer.toString();
					if (data.trim().endsWith(prompt)) {
						break;
					}
				}
			}
			return sBuffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
