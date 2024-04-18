package com.mawen.think.in.spring.data.redis.advanced.pojo;

public record User(Long id, String name, Integer age) implements IBase, IKeyBase<String> {

	@Override
	public Long getId() {
		return id();
	}

	@Override
	public String getKey() {
		return name();
	}
}
