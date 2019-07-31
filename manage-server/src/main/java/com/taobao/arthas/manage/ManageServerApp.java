
package com.taobao.arthas.manage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.taobao.arthas.manage.filter.CrossDomainFilter;

@SpringBootApplication
@ComponentScan(basePackages = { "com.taobao.arthas.manage" }, excludeFilters = {})
public class ManageServerApp {

	public static void main(String[] args) {
		SpringApplication.run(ManageServerApp.class, args);

	}

	/**
	 * 跨域请求
	 * 
	 * @param filter
	 * @return
	 */
	@Bean
	public FilterRegistrationBean crossDomainFilter() {

		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new CrossDomainFilter());
		filterRegistrationBean.setName("crossDomainFilter");
		filterRegistrationBean.setOrder(1);
		Map<String, String> initParams = new HashMap<String, String>();
		filterRegistrationBean.setInitParameters(initParams);
		List<String> urlPatterns = new ArrayList<String>();
		urlPatterns.add("/*");
		filterRegistrationBean.setUrlPatterns(urlPatterns);

		return filterRegistrationBean;
	}

}
