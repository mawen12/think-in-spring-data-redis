package com.mawen.think.in.spring.data.redis.advanced.interceptor.support;

import java.util.function.Function;

import com.mawen.think.in.spring.data.redis.advanced.annotation.BatchCacheable;
import lombok.Data;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/18
 */
@Data
public class ElementKey<T> {

	public static ElementKey<Object> EMPTY = new ElementKey<>(null, null, null);

	private BatchCacheable batchCacheable;
	private Class<T> actualType;
	private Function<T, String> keyGetter;


	public ElementKey(BatchCacheable batchCacheable, Class<T> actualType, Function<T, String> keyGetter) {
		this.batchCacheable = batchCacheable;
		this.actualType = actualType;
		this.keyGetter = keyGetter;
	}
}
