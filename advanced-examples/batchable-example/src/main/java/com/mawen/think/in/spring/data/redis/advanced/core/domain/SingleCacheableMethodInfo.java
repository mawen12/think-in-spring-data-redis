package com.mawen.think.in.spring.data.redis.advanced.core.domain;

import java.lang.reflect.Method;

import com.mawen.think.in.spring.data.redis.advanced.core.annotation.SingleCacheable;

import org.springframework.data.redis.core.types.Expiration;
import org.springframework.util.Assert;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/19
 */
public class SingleCacheableMethodInfo<T> extends AbstractCacheableMethodInfo<T> {

	private SingleCacheable singleCacheable;

	public SingleCacheableMethodInfo(Method method) {
		super(method);

		Assert.isTrue(method.isAnnotationPresent(SingleCacheable.class), () -> String.format("Method [%s] must be annotated with @SingleCacheable", method));

		this.singleCacheable = method.getAnnotation(SingleCacheable.class);
	}

	@Override
	String getAnnotationKey() {
		return "";
	}

	@Override
	boolean isValid() {
		return false;
	}

	@Override
	Expiration extractExpiration() {
		return null;
	}
}
