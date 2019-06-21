
package com.taobao.arthas.manage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.taobao.arthas.manage" }, excludeFilters = {})
public class ManageServerApp {

	public static void main(String[] args) {
		SpringApplication.run(ManageServerApp.class, args);
	}

}
