package com.mawen.think.in.spring.data.redis.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mawen.think.in.spring.data.redis.pojo.User;
import com.mawen.think.in.spring.data.redis.util.JsonUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/17
 */
@RestController
@RequestMapping("/user/str")
public class UserStrController {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@GetMapping("/{key}")
	public User get(@PathVariable String key) {
		String content = redisTemplate.opsForValue().get(key);

		return content != null ? JsonUtil.deserialize(content, User.class) : null;
	}

	@PutMapping("/{key}")
	public void put(@PathVariable String key, @RequestBody User user) {
		redisTemplate.opsForValue().set(key, JsonUtil.serialize(user));
	}
}
