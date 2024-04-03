package com.mawen.think.in.spring.data.redis.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import com.mawen.think.in.spring.data.redis.core.ListFunction;
import com.mawen.think.in.spring.data.redis.pojo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/3
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CacheUserService {

	private final UserService userService;

	private static final Map<Long, User> DB_USER = Map.of(1L, new User(1L, "mawen", 20));

	//================================================================================================
	// Function
	//================================================================================================

	@CachePut(value = "user")
	public void save(String key, User user) {
		userService.save(key, user);
	}

	@Cacheable(value = "user")
	public User get(Long key) {
		log.info("Enter UserService#get method...");
		return DB_USER.get(key);
	}

	public List<User> list(List<Long> keys) {
		log.info("Enter UserService#list method...");
		return keys.stream().map(DB_USER::get).filter(Objects::nonNull).toList();
	}

	@CacheEvict(value = "user")
	public Boolean delete(String key) {
		log.info("Enter UserService#delete method...");
		return userService.delete(key);
	}
}
