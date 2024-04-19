package com.mawen.think.in.spring.data.redis.advanced.interceptor.support;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import lombok.Data;

import org.springframework.data.util.Lazy;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/18
 */
@Data
class BatchCacheableParamInfo {

	private final int paramIndex;

	private final Class<?> paramType;

	private final Class<?> rawCacheParamType;

	BatchCacheableParamInfo(Method method, int cacheParamIndex) {
		Class<?> cacheParamType = cacheParamIndex != -1 ? method.getParameterTypes()[cacheParamIndex] : null;
		Class<?> rawCacheParamType = cacheParamIndex != -1 ? extractActualType(method.getGenericParameterTypes()[cacheParamIndex]) : null;

		this.paramIndex = cacheParamIndex;
		this.paramType = cacheParamType;
		this.rawCacheParamType = rawCacheParamType;
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
