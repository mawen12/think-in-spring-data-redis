package com.mawen.think.in.spring.data.redis.advanced.pojo;

public record User(Long id, String name, Integer age) implements IBase {

	public Long getId() {
		return id();
	}
}
