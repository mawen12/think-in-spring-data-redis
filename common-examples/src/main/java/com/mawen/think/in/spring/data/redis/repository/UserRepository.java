package com.mawen.think.in.spring.data.redis.repository;

import com.mawen.think.in.spring.data.redis.pojo.User;

import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/2
 */
@Repository
public interface UserRepository extends KeyValueRepository<User, Long> {
}
