package com.mawen.think.in.spring.data.redis.pojo;

import org.springframework.data.annotation.Id;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/2
 */
public record User(@Id Long id, String name, Integer age) {
}
