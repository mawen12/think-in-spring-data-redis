package com.mawen.think.in.spring.data.redis.advanced.interceptor.support;

import java.lang.reflect.Method;
import java.util.List;

import com.mawen.think.in.spring.data.redis.advanced.annotation.BatchCacheable;
import com.mawen.think.in.spring.data.redis.advanced.interceptor.parser.KeyParser;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

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

	private final BatchCacheable batchCacheable;

	private final BatchCacheableParamInfo paramInfo;

	private final BatchCacheableReturnInfo<T> returnInfo;

	public BatchCacheableMethodInfo(Method method) {
		Assert.notNull(method, "Method must not be null");
		Assert.isTrue(method.isAnnotationPresent(BatchCacheable.class), () -> String.format("Method [%s] must be annotated with @BatchCacheable", method));

		this.method = method;
		this.batchCacheable = method.getAnnotation(BatchCacheable.class);
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
				log.trace("Method [{}] has invalid parameters index[{}]", method.toString(), paramInfo.getParamIndex());
			}
			boolean returnValid = returnInfo.isValid();
			if (!returnValid) {
				log.trace("Method [{}] has invalid return type [{}] and parser [{}]", method.toString(), returnInfo.getReturnType(), returnInfo.getKeyParser());
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
}
