package com.mawen.think.in.spring.data.redis.controller;

import java.util.List;

import com.mawen.think.in.spring.data.redis.pojo.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/2
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private RedisTemplate<String, User> redisTemplate;

	//================================================================================================
	// Template
	//================================================================================================

	@PutMapping("/{key}")
	public void save(@PathVariable String key, @RequestBody User user) {
		redisTemplate.opsForValue().set(key, user);
	}

	@GetMapping("/{key}")
	public User get(@PathVariable String key) {
		return redisTemplate.opsForValue().get(key);
	}

	@GetMapping("/")
	public List<User> list(@RequestParam List<String> keys) {
		return redisTemplate.opsForValue().multiGet(keys);
	}

	@DeleteMapping("/{key}")
	public Boolean delete(@PathVariable String key) {
		return redisTemplate.delete(key);
	}
}
