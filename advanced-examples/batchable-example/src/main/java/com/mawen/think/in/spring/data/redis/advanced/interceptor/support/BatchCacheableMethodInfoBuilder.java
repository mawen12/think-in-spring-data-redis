package com.mawen.think.in.spring.data.redis.advanced.interceptor.support;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/19
 */
public final class BatchCacheableMethodInfoBuilder {

	private static final ConcurrentMap<String, BatchCacheableMethodInfo> CACHE = new ConcurrentHashMap<>(3);

	public static BatchCacheableMethodInfo build(Method method) {
		String methodName = method.getName();

		return CACHE.computeIfAbsent(methodName, key -> new BatchCacheableMethodInfo<>(method));
	}
}
