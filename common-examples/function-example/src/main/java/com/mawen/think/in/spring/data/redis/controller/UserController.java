package com.mawen.think.in.spring.data.redis.controller;

import java.util.List;

import com.mawen.think.in.spring.data.redis.pojo.User;
import com.mawen.think.in.spring.data.redis.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PutMapping("/{key}")
	public void save(@PathVariable String key, @RequestBody User user) {
		log.info("Enter UserController#save method...");
		userService.save(key, user);
	}

	@GetMapping("/{key}")
	public User get(@PathVariable String key) {
		log.info("Enter UserController#get method...");
		return userService.get(key);
	}

	@GetMapping("/")
	public List<User> list(@RequestParam List<Long> keys) {
		log.info("Enter UserController#list method...");
		return userService.list(keys);
	}

	@DeleteMapping("/{key}")
	public Boolean delete(@PathVariable String key) {
		log.info("Enter UserController#delete method...");
		return userService.delete(key);
	}

}
