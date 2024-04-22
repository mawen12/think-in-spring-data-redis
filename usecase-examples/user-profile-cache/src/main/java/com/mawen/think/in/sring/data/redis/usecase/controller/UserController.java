package com.mawen.think.in.sring.data.redis.usecase.controller;

import java.time.Duration;

import com.mawen.think.in.sring.data.redis.usecase.pojo.User;
import com.mawen.think.in.sring.data.redis.usecase.service.UserCacheService;
import com.mawen.think.in.sring.data.redis.usecase.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/15
 */
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final UserCacheService userCacheService;

	private final RedisTemplate<String, User> redisTemplate;

	private static final String CACHE_KEY = "user";

	@GetMapping("/{name}")
	public User get(@PathVariable String name) {

		User mawen = redisTemplate.opsForValue().get(CACHE_KEY + name);
		if (mawen == null) {
			log.info("Cache Miss: {}", CACHE_KEY + name);
			mawen = userService.getUser(name);
			redisTemplate.opsForValue().set(CACHE_KEY + name, mawen, Duration.ofSeconds(10));
		}
		return mawen;
	}

	@GetMapping("/cache/{name}")
	public User cache(@PathVariable String name) {
		return userCacheService.getUser(name);
	}
}
