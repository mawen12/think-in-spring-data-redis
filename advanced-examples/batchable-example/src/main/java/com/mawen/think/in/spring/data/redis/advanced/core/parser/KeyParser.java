package com.mawen.think.in.spring.data.redis.advanced.core.parser;

import java.lang.reflect.Type;
import java.util.function.Function;

import org.springframework.lang.Nullable;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/18
 */
public interface KeyParser<T, U> {

	boolean canParse(@Nullable Type type);

	Function<T, String> parse();

	Function<T, U> rawParse();
}
