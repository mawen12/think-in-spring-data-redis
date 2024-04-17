package com.mawen.think.in.spring.data.redis.advanced.controller;

import java.util.List;

import com.mawen.think.in.spring.data.redis.advanced.pojo.User;
import com.mawen.think.in.spring.data.redis.advanced.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/17
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/")
	public List<User> listUser(@RequestParam List<Long> ids) {
		return userService.getUser(ids);
	}
}
