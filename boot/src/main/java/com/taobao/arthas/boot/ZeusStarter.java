package com.taobao.arthas.boot;

import java.io.File;

import com.taobao.arthas.boot.processor.ProcessorServer;
import com.taobao.arthas.boot.register.ManageRegister;
import com.taobao.arthas.common.log.ArthasLogUtil;
import com.taobao.middleware.logger.Logger;

/**
 * 启动类
 * 
 * @author zhaojindong
 *
 */
public class ZeusStarter {
	private static final Logger logger = ArthasLogUtil.getArthasClientLogger();
	
	public static void main(String[] args) {
		ZeusStarter zeusStarter = new ZeusStarter();
		//TODO 先下载jar，然后使用ClassLoader来加载jar，避免应用方依赖
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
				try {
					Thread.sleep(10*1000);
					
					//初始化配置
					GlobalConfig.zeusPath = GlobalConfig.zeusPath + "/zeus/" + zeusVersion + "/";
					GlobalConfig.jarPathAgent = GlobalConfig.zeusPath + "arthas-agent.jar";
					GlobalConfig.jarPathCore = GlobalConfig.zeusPath + "arthas-core.jar";
					GlobalConfig.jarPathSpy = GlobalConfig.zeusPath + "arthas-spy.jar";
	
					// 下载zeus需要的jar到user.home下
					downloadJars();
					
					//启动manage command 处理器服务
					Thread commandServerThread = new Thread(new Runnable() {
						@Override
						public void run() {
							ProcessorServer.start();
						}
					});
					commandServerThread.setName("zeus-command-server-thread");	
					commandServerThread.start();
					
				}catch (Exception ex) {
					logger.error("-1", "启动zeus异常", ex.getMessage());
				}
			}
		});
		zeusThread.setName("zeus-starter-thread");
		zeusThread.start();
		
		//向manage server注册自己
		ManageRegister.register();
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
