package com.mawen.think.in.spring.data.redis.advanced.interceptor.parser;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.function.Function;

import com.mawen.think.in.spring.data.redis.advanced.pojo.IKeyBase;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/18
 */
public enum IKeyBaseKeyParser implements KeyParser<IKeyBase<Serializable>, Serializable> {
	INSTANCE;

	@Override
	public boolean canParse(Type type) {
		return IKeyBase.class.isAssignableFrom((Class<?>) type);
	}

	@Override
	public Function<IKeyBase<Serializable>, String> parse() {
		return rawParse().andThen(String::valueOf);
	}

	@Override
	public Function<IKeyBase<Serializable>, Serializable> rawParse() {
		return IKeyBase::getKey;
	}

	@Override
	public String toString() {
		return "IKeyBaseTypeParser";
	}
}
