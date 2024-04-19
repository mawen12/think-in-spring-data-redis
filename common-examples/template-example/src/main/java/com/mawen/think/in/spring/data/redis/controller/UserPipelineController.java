package com.mawen.think.in.spring.data.redis.controller;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import com.mawen.think.in.spring.data.redis.pojo.User;
import com.mawen.think.in.spring.data.redis.util.JsonUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/19
 */
@RestController
@RequestMapping("/user/pip")
public class UserPipelineController {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@PutMapping("/batch")
	public void batchPut(@RequestBody List<User> users) {

		BiConsumer<StringRedisConnection, List<User>> setUser = (connection, list) -> {
			list.forEach(user -> connection.set(String.valueOf(user.id()), JsonUtil.serialize(user)));
		};

		RedisCallback<Void> redisCallback = connection -> {
			StringRedisConnection stringRedisConnection = (StringRedisConnection) connection;
			setUser.accept(stringRedisConnection, users);
			return null;
		};

		redisTemplate.executePipelined(redisCallback);
	}

	@PutMapping("/batch/set")
	public void batchSet(@RequestBody List<User> users) {
		Map<String, String> map = users.stream().collect(Collectors.toMap(user -> String.valueOf(user.id()), JsonUtil::serialize));
		redisTemplate.opsForValue().multiSet(map);
	}

}
