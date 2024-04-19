package com.mawen.think.in.spring.data.redis.advanced.service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.mawen.think.in.spring.data.redis.advanced.annotation.BatchCacheable;
import com.mawen.think.in.spring.data.redis.advanced.pojo.IBase;
import com.mawen.think.in.spring.data.redis.advanced.pojo.User;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/17
 */

@Slf4j
@Primary
@Service
public class UserServiceImpl implements UserService {

	public static Set<User> users = Set.of(
			new User(1L, "mawen", 10),
			new User(2L, "Bob", 15),
			new User(3L, "Luce", 30),
			new User(4L, "Lucy", 65),
			new User(5L, "Jack", 70)
	);

	@BatchCacheable(key = "mawen", argIndex = 0, timeout = 20, unit = TimeUnit.SECONDS, type = IBase.class)
	public List<User> getUser(List<Long> ids) {
		return users.stream().filter(user -> ids.contains(user.getId())).collect(Collectors.toList());
	}
}
