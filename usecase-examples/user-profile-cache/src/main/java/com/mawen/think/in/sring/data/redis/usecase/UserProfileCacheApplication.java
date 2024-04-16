package com.mawen.think.in.sring.data.redis.usecase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/15
 */
@SpringBootApplication
@EnableCaching
public class UserProfileCacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserProfileCacheApplication.class, args);
	}
}
