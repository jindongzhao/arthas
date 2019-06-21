package com.zjd.test.mvntest;

import com.taobao.arthas.boot.ZeusStarter;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws Exception {
		System.out.println("starting。。。");
		ZeusStarter zeusStarter = new ZeusStarter();
		zeusStarter.init();
	}
}
