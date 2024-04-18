package com.mawen.think.in.spring.data.redis.advanced.interceptor.parser;

import java.lang.reflect.Type;
import java.util.function.Function;

import com.mawen.think.in.spring.data.redis.advanced.pojo.IBase;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/18
 */
public enum IBaseTypeParser implements TypeParser<IBase> {
	INSTANCE;

	private static final Function<IBase, Long> idGetter = IBase::getId;

	@Override
	public boolean canParse(Type type) {
		return IBase.class.isAssignableFrom((Class<?>) type);
	}

	@Override
	public Function<IBase, String> parse() {
		return idGetter.andThen(String::valueOf);
	}

	@Override
	public String toString() {
		return IBaseTypeParser.class.getSimpleName();
	}
}
