package com.mawen.think.in.spring.data.redis.service;

import java.util.List;

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
public class UserService {

	private final RedisTemplate<String, User> redisTemplate;
	private final RedisCacheManager redisCacheManager;

	//================================================================================================
	// Annotation
	//================================================================================================

	@CachePut(value = "user")
	public void save(String key, User user) {
		log.info("Enter UserService#save method...");
		redisTemplate.opsForValue().set(key, user);
	}

	@Cacheable(value = "user")
	public User get(String key) {
		log.info("Enter UserService#get method...");
		return redisTemplate.opsForValue().get(key);
	}

	public List<User> list(List<String> keys) {
		log.info("Enter UserService#list method...");
		return redisTemplate.opsForValue().multiGet(keys);
	}

	@CacheEvict(value = "user")
	public Boolean delete(String key) {
		log.info("Enter UserService#delete method...");
		return redisTemplate.delete(key);
	}

}
