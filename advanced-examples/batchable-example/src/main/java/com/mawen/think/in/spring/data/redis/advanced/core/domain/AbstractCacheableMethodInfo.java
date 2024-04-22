package com.mawen.think.in.spring.data.redis.advanced.core.domain;

import java.lang.reflect.Method;
import java.util.List;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.core.types.Expiration;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/19
 */
@Slf4j
@Getter
public abstract class AbstractCacheableMethodInfo<T> {

	private final Method method;

	private final String methodShortName;


	public AbstractCacheableMethodInfo(Method method) {
		Assert.notNull(method, "Method must not be null");

		this.method = method;
		this.methodShortName = extractMethodShortName();
	}


	@Nullable
	public List<T> parseResult(@Nullable Object result) {
		return (List<T>) result;
	}

	abstract boolean isValid();

	private String extractMethodShortName() {
		StringBuilder builder = new StringBuilder()
				.append(method.getDeclaringClass().getSimpleName())
				.append('.')
				.append(method.getName())
				.append('(');

		for (Class<?> parameterType : method.getParameterTypes()) {
			builder.append(parameterType.getTypeName());
		}

		builder.append(')');

		return builder.toString();
	}
}
