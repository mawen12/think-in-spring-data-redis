package com.mawen.think.in.spring.data.redis.controller;

import java.util.List;
import java.util.function.Function;

import com.mawen.think.in.spring.data.redis.core.ListFunction;
import com.mawen.think.in.spring.data.redis.pojo.User;
import com.mawen.think.in.spring.data.redis.service.CacheUserService;
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
 * @since 2024/4/3
 */
@Slf4j
@RestController
@RequestMapping("/user/cache")
@RequiredArgsConstructor
public class CacheUserController {

	private final CacheUserService cacheUserService;
	private final UserService userService;


	@PutMapping("/{key}")
	public void save(@PathVariable String key, @RequestBody User user) {
		log.info("Enter CacheUserController#save method...");
		cacheUserService.save(key, user);
	}

	@GetMapping("/{key}")
	public User get(@PathVariable Long key) {
		log.info("Enter CacheUserController#get method...");
		return cacheUserService.get(key);
	}

	@GetMapping("/")
	public List<User> list(@RequestParam List<Long> keys) {
		log.info("Enter CacheUserController#list method...");

		ListFunction<Long, User> cacheGetter = cacheUserService::list;
		Function<List<Long>, List<User>> dbGetter = userService::list;
		Function<User, Long> idGetter = User::id;
		Function<List<Long>, List<User>> composeGetter = cacheGetter.compose(dbGetter, idGetter);

		return composeGetter.apply(keys);
	}

	@DeleteMapping("/{key}")
	public Boolean delete(@PathVariable String key) {
		log.info("Enter CacheUserController#delete method...");
		return cacheUserService.delete(key);
	}
}
