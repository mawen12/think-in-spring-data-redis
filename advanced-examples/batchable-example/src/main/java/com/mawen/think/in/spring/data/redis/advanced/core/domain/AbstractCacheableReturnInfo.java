package com.mawen.think.in.spring.data.redis.advanced.core.domain;

import java.lang.reflect.Method;

import lombok.Getter;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/19
 */
@Getter
public abstract class AbstractCacheableReturnInfo {

	private final Class<?> returnType;

	AbstractCacheableReturnInfo(Method method) {
		this.returnType = method.getReturnType();
	}

	abstract boolean isValid();
}
