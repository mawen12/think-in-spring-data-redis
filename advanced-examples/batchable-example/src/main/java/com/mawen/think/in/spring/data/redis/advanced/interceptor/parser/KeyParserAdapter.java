package com.mawen.think.in.spring.data.redis.advanced.interceptor.parser;

import java.lang.reflect.Type;
import java.util.function.Function;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/18
 */
public class KeyParserAdapter<T, U> implements KeyParser<T, U> {

	private final KeyParser<T, U> delegate;

	public KeyParserAdapter(KeyParser<T, U> delegate) {
		this.delegate = delegate;
	}

	@Override
	public boolean canParse(Type type) {
		return delegate.canParse(type);
	}

	@Override
	public Function<T, String> parse() {
		return delegate.parse();
	}

	@Override
	public Function<T, U> rawParse() {
		return delegate.rawParse();
	}

	@Override
	public String toString() {
		return "TypeParserAdapter{" +
				"typeParser=" + delegate +
				'}';
	}
}
