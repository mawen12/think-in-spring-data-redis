package com.mawen.think.in.spring.data.redis.advanced;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/17
 */
@EnableAspectJAutoProxy(proxyTargetClass = true)
@SpringBootApplication
public class BatchCacheableApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatchCacheableApplication.class, args);
	}
}
