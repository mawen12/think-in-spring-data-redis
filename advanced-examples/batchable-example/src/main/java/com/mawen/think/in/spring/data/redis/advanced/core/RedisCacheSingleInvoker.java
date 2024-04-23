package com.mawen.think.in.spring.data.redis.advanced.core;

import java.util.function.BiConsumer;
import java.util.function.Function;

import com.mawen.think.in.spring.data.redis.advanced.core.cache.RedisCacheSingleFunction;
import com.mawen.think.in.spring.data.redis.advanced.core.domain.SingleCacheableMethodInfo;
import com.mawen.think.in.spring.data.redis.core.CacheableSingleFunction;
import org.aspectj.lang.ProceedingJoinPoint;

import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/22
 */
public class RedisCacheSingleInvoker<R> extends AbstractRedisCacheInvoker {

	private SingleCacheableMethodInfo<R> methodInfo;

	private Function<String, String> redisGetter;

	private BiConsumer<String, String> redisSetter;


	protected RedisCacheSingleInvoker(ProceedingJoinPoint joinPoint, StringRedisTemplate redisTemplate) {
		super(joinPoint, redisTemplate);

		this.methodInfo = new SingleCacheableMethodInfo<>(method);
		this.redisGetter = key -> redisTemplate.opsForValue().get(key);
		this.redisSetter = (key, value) -> redisTemplate.opsForValue().set(key, value);
	}


	@Override
	public Object invoke() throws Throwable {

		if (!methodInfo.isValid()) {
			return joinPoint.proceed();
		}

		Function<Object, R> function = CacheableSingleFunction.of(getRedisCacheCall(), getMethodCall());

		return function.apply(joinPoint.getArgs());
	}

	private Function<Object, R> getMethodCall() {
		return args -> {
			Object result = null;
			try {
				result = joinPoint.proceed();
				return (R) methodInfo.parseResult(result);
			}
			catch (Throwable e) {
				throw new RuntimeException(e);
			}
		};
	}

	private RedisCacheSingleFunction<R> getRedisCacheCall() {

		return new RedisCacheSingleFunction<>(redisGetter, redisSetter,
				methodInfo.getParamInfo().getSerializer(),
				key -> methodInfo.getSingleCacheable().key() + ":" + key,
				methodInfo.getReturnInfo().getDeserializer(),
				methodInfo.getMethodShortName());
	}
}
