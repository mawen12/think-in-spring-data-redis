package com.mawen.think.in.spring.data.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/2
 */
@EnableCaching // Enabled Cache Annotation
@SpringBootApplication
public class FunctionAnnotationApp {

	public static void main(String[] args) {
		SpringApplication.run(FunctionAnnotationApp.class, args);
	}
}
