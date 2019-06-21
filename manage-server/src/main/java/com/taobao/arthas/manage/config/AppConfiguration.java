package com.taobao.arthas.manage.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author gonghuan@corp.netease.com
 * @date 2019/1/18
 */
@Configuration
@ImportResource(locations = { "classpath:/conf/application-all.xml" })
public class AppConfiguration extends WebMvcConfigurerAdapter {

}
