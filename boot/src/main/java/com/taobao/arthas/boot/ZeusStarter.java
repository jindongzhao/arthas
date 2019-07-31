package com.taobao.arthas.boot;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import com.taobao.arthas.boot.processor.ProcessorServer;
import com.taobao.arthas.boot.register.ManageRegister;
import com.taobao.arthas.common.ManageRespsCodeEnum;
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
		// TODO 先下载jar，然后使用ClassLoader来加载jar，避免应用方依赖
		//zeusStarter.init("3.1.1");
		
		//logger.info("执行cmd的结果=========>"+excuteCmd("ps -X -p 32735 | grep 32735"));
		logger.info("执行cmd的结果=========>"+excuteCmd("ps axu | grep 32735"));
		
		//Mock 主进程进行中
		while(true) {
			try {
				Thread.sleep(1000*100);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static String excuteCmd(String cmd) {
		BufferedReader br = null;
		try {
			logger.info("执行命令："+cmd);
			Process p = Runtime.getRuntime().exec(cmd);
			br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = null;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
			logger.info("获取到的cmd结果："+sb.toString());
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
		try {
			Thread.sleep(10 * 1000);

			logger.info("start zeus " + zeusVersion);

			Thread zeusThread = new Thread(new Runnable() {

				@Override
				public void run() {
					// 初始化配置
					GlobalConfig.zeusPath = GlobalConfig.zeusPath + "/zeus/" + zeusVersion + "/";
					GlobalConfig.jarPathAgent = GlobalConfig.zeusPath + "arthas-agent.jar";
					GlobalConfig.jarPathCore = GlobalConfig.zeusPath + "arthas-core.jar";
					GlobalConfig.jarPathSpy = GlobalConfig.zeusPath + "arthas-spy.jar";

					// 下载zeus需要的jar到user.home下
					downloadJars();

					// 启动manage command 处理器服务
					Thread commandServerThread = new Thread(new Runnable() {
						@Override
						public void run() {
							ProcessorServer.start();
						}
					});
					commandServerThread.setName("zeus-command-server-thread");
					commandServerThread.start();
				}
			});
			zeusThread.setName("zeus-starter-thread");
			zeusThread.start();

			// 向manage server注册自己
			ManageRegister.register();

		} catch (Exception ex) {
			logger.error("-1", "启动zeus异常", ex.getMessage());
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
		File zeusDir = new File(GlobalConfig.zeusPath);
		if (!zeusDir.exists()) {
			zeusDir.mkdirs();

			// 下载jar
			try {
				logger.info("download agent jar from" + GlobalConfig.jarPathAgent);
				DownloadUtils.saveUrl(GlobalConfig.jarPathAgent, GlobalConfig.JAR_URL_AGENT, true);
				logger.info("download core jar from" + GlobalConfig.jarPathCore);
				DownloadUtils.saveUrl(GlobalConfig.jarPathCore, GlobalConfig.JAR_URL_CORE, true);
				logger.info("download spy jar from" + GlobalConfig.jarPathSpy);
				DownloadUtils.saveUrl(GlobalConfig.jarPathSpy, GlobalConfig.JAR_URL_SPY, true);

			} catch (Exception e) {
				logger.error("DOWNLOAD_JAR_EXCEPTION", "download jar error", e);
				e.printStackTrace();
			}
		}
	}

}
