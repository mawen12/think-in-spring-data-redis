package com.mawen.think.in.spring.data.redis.advanced.interceptor.parser;

import java.lang.reflect.Type;
import java.util.function.Function;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/18
 */
public interface TypeParser<T> {

	boolean canParse(Type type);

	Function<T, String> parse();
}
