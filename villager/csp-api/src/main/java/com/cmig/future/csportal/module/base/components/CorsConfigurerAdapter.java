package com.cmig.future.csportal.module.base.components;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 18:05 2017/7/6.
 * @Modified by zhangtao on 18:05 2017/7/6.
 */
@Component
@Configuration
@EnableWebMvc
public class CorsConfigurerAdapter extends WebMvcConfigurerAdapter {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/mgt/**").allowedOrigins("*").allowedMethods("GET", "HEAD", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "TRACE");
		registry.addMapping("/user/**").allowedOrigins("*").allowedMethods("GET", "HEAD", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "TRACE");
		registry.addMapping("/common/**").allowedOrigins("*").allowedMethods("GET", "HEAD", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "TRACE");
	}
}
