package com.mawen.think.in.spring.data.redis.advanced.service;

import java.util.List;

import com.mawen.think.in.spring.data.redis.advanced.pojo.User;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/17
 */
public interface UserService {


	List<User> getUser(List<Long> ids);
}
