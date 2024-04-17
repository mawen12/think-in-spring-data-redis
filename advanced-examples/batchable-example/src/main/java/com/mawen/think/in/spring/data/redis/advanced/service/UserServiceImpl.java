package com.mawen.think.in.spring.data.redis.advanced.service;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import com.mawen.think.in.spring.data.redis.advanced.annotation.BatchCacheable;
import com.mawen.think.in.spring.data.redis.advanced.pojo.User;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/17
 */

@Slf4j
@Primary
@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@BatchCacheable
	public List<User> getUser(List<Long> ids) {
		return List.of();
	}
}
