package com.mawen.think.in.spring.data.redis.advanced.core.domain;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import lombok.Getter;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/19
 */
@Getter
public abstract class AbstractCacheableReturnInfo {

	protected final Class<?> returnType;
	protected final boolean isCollection;
	protected final boolean isMap;
	protected final List<Class<?>> rawTypes;

	AbstractCacheableReturnInfo(Method method) {
		this.returnType = method.getReturnType();
		this.isCollection = Collection.class.isAssignableFrom(returnType);
		this.isMap = Map.class.isAssignableFrom(returnType);
		this.rawTypes = isCollection
				? getRawTypesAsList(method)
				: isMap
					? getRawTypesAsMap(method)
					: null;
	}

	abstract boolean isValid();

	private List<Class<?>> getRawTypesAsMap(Method method) {
		Type type = method.getGenericReturnType();
		if (type instanceof ParameterizedType parameterizedType) {
			Type[] argTypes = parameterizedType.getActualTypeArguments();
			return List.of((Class<?>)argTypes[0], (Class<?>)argTypes[1]);
		}
		return null;
	}

	private List<Class<?>> getRawTypesAsList(Method method) {
		Type type = method.getGenericReturnType();
		if (type instanceof ParameterizedType parameterizedType) {
			Type[] argTypes = parameterizedType.getActualTypeArguments();
			return List.of((Class<?>)argTypes[0]);
		}
		return null;
	}
}
