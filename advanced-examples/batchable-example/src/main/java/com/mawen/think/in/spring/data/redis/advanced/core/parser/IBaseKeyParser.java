package com.mawen.think.in.spring.data.redis.advanced.core.parser;

import java.lang.reflect.Type;
import java.util.function.Function;

import com.mawen.think.in.spring.data.redis.advanced.pojo.IBase;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/18
 */
public enum IBaseKeyParser implements KeyParser<IBase, Long> {
	INSTANCE;

	@Override
	public boolean canParse(@Nullable Type type) {
		return type != null && IBase.class.isAssignableFrom((Class<?>) type);
	}

	@Override
	public Function<IBase, String> parse() {
		return rawParse().andThen(String::valueOf);
	}

	@Override
	public Function<IBase, Long> rawParse() {
		return IBase::getId;
	}

	@Override
	public String toString() {
		return IBaseKeyParser.class.getSimpleName();
	}
}
