package com.mawen.think.in.spring.data.redis.core;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/3
 */
@FunctionalInterface
public interface SingleFunction<T, R> {

	R apply(T t);
}
