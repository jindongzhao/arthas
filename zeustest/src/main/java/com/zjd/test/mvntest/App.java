package com.zjd.test.mvntest;

import java.io.File;

import com.taobao.arthas.boot.ZeusStarter;
import com.taobao.arthas.core.Arthas;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws Exception {
		System.out.println("starting。。。");
		ZeusStarter zeusStarter = new ZeusStarter();
		zeusStarter.init();
		
		/*testGetJar();*/
	}
	
	private static void testGetJar() {
		String path = Arthas.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		System.out.println(path);
		File file = new File(path);
		System.out.println(file.getAbsolutePath());
	}
}
