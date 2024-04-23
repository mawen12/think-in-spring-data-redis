package com.mawen.think.in.spring.data.redis.advanced.core.domain;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import com.mawen.think.in.spring.data.redis.advanced.core.annotation.SingleCacheable;
import lombok.Getter;

import org.springframework.data.redis.core.types.Expiration;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/19
 */
@Getter
public class SingleCacheableMethodInfo<T> extends AbstractCacheableMethodInfo<T> {

	private final SingleCacheable singleCacheable;

	@Nullable
	private final Expiration expiration;

	private final SingleCacheableParamInfo paramInfo;

	private final SingleCacheableReturnInfo<T> returnInfo;


	public SingleCacheableMethodInfo(Method method) {
		super(method);

		Assert.isTrue(method.isAnnotationPresent(SingleCacheable.class), () -> String.format("Method [%s] must be annotated with @SingleCacheable", method));

		this.singleCacheable = method.getAnnotation(SingleCacheable.class);
		this.expiration = extractExpiration();
		this.paramInfo = new SingleCacheableParamInfo();
		this.returnInfo = new SingleCacheableReturnInfo<>(method);
	}


	String getAnnotationKey() {
		return singleCacheable.key();
	}

	@Override
	public boolean isValid() {
		return false;
	}

	Expiration extractExpiration() {
		long timeout = singleCacheable.timeout();
		TimeUnit unit = singleCacheable.unit();
		if (timeout <= 0) {
			return null;
		}

		return Expiration.from(timeout, unit);
	}
}
