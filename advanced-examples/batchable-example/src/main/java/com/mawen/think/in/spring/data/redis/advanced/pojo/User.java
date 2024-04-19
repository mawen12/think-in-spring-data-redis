package com.mawen.think.in.spring.data.redis.advanced.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record User(Long id, String name, Integer age) implements IBase, IKeyBase<String> {

	@Override
	public Long getId() {
		return id();
	}

	@JsonIgnore
	@Override
	public String getKey() {
		return name();
	}
}
