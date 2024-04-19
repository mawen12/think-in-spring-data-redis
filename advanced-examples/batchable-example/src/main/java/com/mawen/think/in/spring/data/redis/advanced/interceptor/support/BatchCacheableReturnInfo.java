package com.mawen.think.in.spring.data.redis.advanced.interceptor.support;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

import com.mawen.think.in.spring.data.redis.advanced.interceptor.TypeParserRunner;
import com.mawen.think.in.spring.data.redis.advanced.interceptor.parser.KeyParser;
import lombok.Getter;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/19
 */
@Getter
public class BatchCacheableReturnInfo<T> {

	private final Class<?> returnType;

	private final Class<T> rawReturnType;

	private final KeyParser<T, ?> keyParser;

	public BatchCacheableReturnInfo(Method method, Class<?> batchCacheableType) {
		this.returnType = method.getReturnType();
		this.rawReturnType = extractActualType(method.getGenericReturnType());

		KeyParser<?, ?> typeParser = TypeParserRunner.getTypeParser(batchCacheableType);
		if (typeParser != null && typeParser.canParse(rawReturnType)) {
			this.keyParser = (KeyParser<T, ?>) typeParser;
		}
		else {
			this.keyParser = null;
		}
	}

	public boolean isValid() {
		return Collection.class.isAssignableFrom(returnType)
				&& keyParser != null && keyParser.canParse(rawReturnType);
	}


	private Class<T> extractActualType(Type type) {
		if (type instanceof ParameterizedType parameterizedType) {
			Type actualType = parameterizedType.getActualTypeArguments()[0];
			return (Class<T>) actualType;
		}
		return null;
	}
}
