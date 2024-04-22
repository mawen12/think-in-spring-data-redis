package com.mawen.think.in.spring.data.redis.advanced.core.domain;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import lombok.Data;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/18
 */
@Data
public class BatchCacheableParamInfo {

	private final int paramIndex;

	private final Class<?> paramType;

	private final Class<?> rawParamType;

	BatchCacheableParamInfo(Method method, int cacheParamIndex) {
		Class<?> cacheParamType = cacheParamIndex != -1 ? method.getParameterTypes()[cacheParamIndex] : null;
		Class<?> rawParamType = cacheParamIndex != -1 ? extractActualType(method.getGenericParameterTypes()[cacheParamIndex]) : null;

		this.paramIndex = cacheParamIndex;
		this.paramType = cacheParamType;
		this.rawParamType = rawParamType;
	}

	public boolean isValid() {
		return paramIndex != -1;
	}

	private Class<?> extractActualType(Type type) {
		if (type instanceof ParameterizedType parameterizedType) {
			Type actualType = parameterizedType.getActualTypeArguments()[0];
			return (Class<?>)actualType;
		}
		return null;
	}
}
