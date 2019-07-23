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
import com.taobao.arthas.boot.processor.ProcessorServer;
import com.taobao.arthas.boot.register.ManageRegister;
import com.taobao.arthas.common.AnsiLog;
import com.taobao.arthas.common.ManageRespsCodeEnum;
import com.taobao.arthas.common.ManageRpcCommandEnum;
import com.taobao.arthas.common.ManageRpcUtil;
import com.taobao.arthas.common.dto.RegisterDto;
import com.taobao.arthas.common.dto.HeartBeatRespDto;
import com.taobao.arthas.common.dto.ManageTaskDto;
import com.taobao.arthas.common.log.ArthasLogUtil;
import com.taobao.arthas.core.shell.ShellServerOptions;
import com.taobao.middleware.logger.Logger;

/**
 * 启动类
 * 
 * @author zhaojindong
 *
 */
public class ZeusStarter {
	private static final Logger logger = ArthasLogUtil.getArthasLogger();
	
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
		logger.info("start zeus "+zeusVersion);
		Thread zeusThread = new Thread(new Runnable() {

			@Override
			public void run() {
				//初始化配置
				GlobalConfig.zeusPath = GlobalConfig.zeusPath + "/zeus/" + zeusVersion + "/";
				GlobalConfig.jarPathAgent = GlobalConfig.zeusPath + "arthas-agent.jar";
				GlobalConfig.jarPathCore = GlobalConfig.zeusPath + "arthas-core.jar";
				GlobalConfig.jarPathSpy = GlobalConfig.zeusPath + "arthas-spy.jar";

				// 下载zeus需要的jar到user.home下
				downloadJars();
				
				//启动manage command 处理器服务
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						ProcessorServer.start();
					}
				}).start();
				
				//向manage server注册自己
				ManageRegister.register();
			}
		});
		zeusThread.setName("Zeus-heart-beating-thread");
		zeusThread.start();
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
		File zeusDir = new File(GlobalConfig.zeusPath);
		if (!zeusDir.exists()) {
			zeusDir.mkdirs();

			// 下载jar
			try {
				logger.info("download agent jar from"+GlobalConfig.jarPathAgent);
				DownloadUtils.saveUrl(GlobalConfig.jarPathAgent, GlobalConfig.JAR_URL_AGENT, true);
				logger.info("download core jar from"+GlobalConfig.jarPathCore);
				DownloadUtils.saveUrl(GlobalConfig.jarPathCore, GlobalConfig.JAR_URL_CORE, true);
				logger.info("download spy jar from"+GlobalConfig.jarPathSpy);
				DownloadUtils.saveUrl(GlobalConfig.jarPathSpy, GlobalConfig.JAR_URL_SPY, true);

			} catch (Exception e) {
				logger.error("DOWNLOAD_JAR_EXCEPTION", "download jar error", e);
				e.printStackTrace();
			}
		}
	}

}
