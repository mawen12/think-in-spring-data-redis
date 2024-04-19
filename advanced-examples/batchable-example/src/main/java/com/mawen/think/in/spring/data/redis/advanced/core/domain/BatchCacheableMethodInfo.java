package com.mawen.think.in.spring.data.redis.advanced.core.domain;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.mawen.think.in.spring.data.redis.advanced.core.annotation.BatchCacheable;
import com.mawen.think.in.spring.data.redis.advanced.core.parser.KeyParser;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.core.types.Expiration;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/18
 */
@Getter
@Slf4j
public class BatchCacheableMethodInfo<T> {

	private final Method method;

	private final String methodShortName;

	private final BatchCacheable batchCacheable;

	@Nullable
	private final Expiration expiration;

	private final BatchCacheableParamInfo paramInfo;

	private final BatchCacheableReturnInfo<T> returnInfo;

	public BatchCacheableMethodInfo(Method method) {
		Assert.notNull(method, "Method must not be null");
		Assert.isTrue(method.isAnnotationPresent(BatchCacheable.class), () -> String.format("Method [%s] must be annotated with @BatchCacheable", method));

		this.method = method;
		this.methodShortName = extractMethodShortName();
		this.batchCacheable = method.getAnnotation(BatchCacheable.class);
		this.expiration = extractExpiration();
		this.paramInfo = new BatchCacheableParamInfo(method, extractCacheParamIndex());
		this.returnInfo = new BatchCacheableReturnInfo<>(method, batchCacheable.type());
	}

	public String getAnnotationKey() {
		return batchCacheable.key();
	}

	@Nullable
	public List<T> parseResult(@Nullable Object result) {
		return (List<T>) result;
	}

	public Class<T> getRawReturnType() {
		return returnInfo.getRawReturnType();
	}

	public KeyParser<T, ?> getKeyParser() {
		return returnInfo.getKeyParser();
	}

	public boolean isValid() {
		if (log.isTraceEnabled()) {
			boolean paramValid = paramInfo.isValid();
			if (!paramValid) {
				log.trace("Method [{}] has invalid parameters index [{}]", methodShortName, batchCacheable.argIndex());
			}
			boolean returnValid = returnInfo.isValid();
			if (!returnValid) {
				log.trace("Method [{}] has invalid return type [{}] and parser [{}]", methodShortName, returnInfo.getReturnType(), returnInfo.getKeyParser());
			}

			return paramValid && returnValid;
		}
		return paramInfo.isValid() && returnInfo.isValid();
	}

	private int extractCacheParamIndex() {
		int index = batchCacheable.argIndex();
		int parameterCount = method.getParameterCount();
		return index < 0 || index >= parameterCount ? -1 : index;
	}

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

	private Expiration extractExpiration() {
		long timeout = batchCacheable.timeout();
		TimeUnit unit = batchCacheable.unit();
		if (timeout <= 0) {
			return null;
		}

		return Expiration.from(timeout, unit);
	}
}
