package com.mawen.think.in.spring.data.redis.advanced.interceptor;

import java.util.List;
import java.util.function.Function;

import com.mawen.think.in.spring.data.redis.advanced.cache.RedisCacheStringStringListFunction;
import com.mawen.think.in.spring.data.redis.advanced.interceptor.support.BatchCacheableMethodInfo;
import com.mawen.think.in.spring.data.redis.advanced.interceptor.support.BatchCacheableMethodInfoBuilder;
import com.mawen.think.in.spring.data.redis.advanced.interceptor.support.ParametersValueHolder;
import com.mawen.think.in.spring.data.redis.core.ListFunction;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/19
 */
@Slf4j
public class RedisCacheInvoker<R> {

	private final ProceedingJoinPoint joinPoint;

	private final RedisTemplate<String, String> redisTemplate;

	private final BatchCacheableMethodInfo<R> methodInfo;


	public RedisCacheInvoker(ProceedingJoinPoint joinPoint, RedisTemplate<String, String> redisTemplate) {
		this.joinPoint = joinPoint;
		this.redisTemplate = redisTemplate;

		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		this.methodInfo = BatchCacheableMethodInfoBuilder.build(methodSignature.getMethod());
	}

	public Object invoke() throws Throwable {
		if (!methodInfo.isValid()) {
			return joinPoint.proceed();
		}

		ParametersValueHolder parametersValueHolder = new ParametersValueHolder(joinPoint.getArgs(), methodInfo.getParamInfo());

		Function<List<String>, List<R>> methodCall = keys -> {
			try {
				Object result = joinPoint.proceed(parametersValueHolder.getMergedArgs(keys));
				return methodInfo.parseResult(result);
			}
			catch (Throwable e) {
				throw new RuntimeException(e);
			}
		};

		Function<BatchCacheableMethodInfo<R>, RedisCacheStringStringListFunction<R>> redisCacheGetter = batchCacheableMethodInfo -> new RedisCacheStringStringListFunction<>(
				keys -> redisTemplate.opsForValue().multiGet(keys),
				map -> redisTemplate.opsForValue().multiSet(map),
				batchCacheableMethodInfo.getReturnInfo().getRawReturnType(),
				key -> batchCacheableMethodInfo.getAnnotationKey() + ":" + key
		);

		ListFunction<String, R> redisCacheFunction = redisCacheGetter.apply(methodInfo);

		return ListFunction.of(redisCacheFunction, methodCall, methodInfo.getReturnInfo().getKeyParser().parse())
				.apply(parametersValueHolder.getKeys());
	}
}
