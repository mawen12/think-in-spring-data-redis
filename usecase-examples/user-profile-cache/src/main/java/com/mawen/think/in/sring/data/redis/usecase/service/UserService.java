package com.mawen.think.in.sring.data.redis.usecase.service;

import java.util.Objects;

import com.mawen.think.in.sring.data.redis.usecase.pojo.User;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/15
 */
@Slf4j
@Service
public class UserService {

	public static User DEFAULT_USER = new User(1L, "mawen", 30);

	@SneakyThrows
	public User getUser(String name) {
		log.info("Into UserService#getUser and Sleep One Second ......");
		Thread.sleep(1000L);
		if (Objects.equals(name, DEFAULT_USER.name())) {
			return DEFAULT_USER;
		} else {
			return null;
		}
	}

}
