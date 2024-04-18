package com.mawen.think.in.spring.data.redis.advanced.interceptor;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import com.mawen.think.in.spring.data.redis.advanced.interceptor.function.RedisCacheStringStringListFunction;
import com.mawen.think.in.spring.data.redis.advanced.interceptor.holder.ParametersValueHolder;
import com.mawen.think.in.spring.data.redis.advanced.interceptor.support.BatchCacheableMethodInfo;
import com.mawen.think.in.spring.data.redis.core.ListFunction;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/17
 */
@Slf4j
@Aspect
@Component
public class BatchCacheableAspect {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	private Function<Collection<String>, List<String>> redisMultiGetter = keys -> redisTemplate.opsForValue().multiGet(keys);
	private Consumer<Map<String, String>> redisMultiSetter = map -> redisTemplate.opsForValue().multiSet(map);


	@Pointcut("@annotation(com.mawen.think.in.spring.data.redis.advanced.annotation.BatchCacheable)")
	public void batchCacheableAnnotation() {
	}

//	@Pointcut("execution(@com.mawen.think.in.spring.data.redis.advanced.annotation.BatchCacheable * *(..))")
//	public void batchCacheableMethod() {
//	}

	@Around(value = "batchCacheableAnnotation()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		if (log.isTraceEnabled()) {
			log.trace("Info BatchCacheableAspect#around...");
		}

		BatchCacheableMethodInfo methodInfo = new BatchCacheableMethodInfo(joinPoint);
		if (!methodInfo.isValid()) {
			return joinPoint.proceed();
		}

		ParametersValueHolder parametersValue = new ParametersValueHolder(joinPoint.getArgs(), methodInfo.getCacheParamIndex(), methodInfo.getRawCacheParamType());
		List<String> paramKeys = parametersValue.collectCacheKeys();

		RedisCacheStringStringListFunction<Object> redisCacheGetter = new RedisCacheStringStringListFunction<>(redisMultiGetter, redisMultiSetter, (Class<Object>) methodInfo.getRawReturnType(), key -> methodInfo.getAnnotationKey() + ":" + key);
		Function<List<String>, List<Object>> methodCall = keys -> {
			try {
				Object result = joinPoint.proceed(parametersValue.getMergedArgs(keys));
				return methodInfo.parseResult(result);
			}
			catch (Throwable e) {
				throw new RuntimeException(e);
			}
		};

		Function<List<String>, List<Object>> cacheFunction = ListFunction.of(redisCacheGetter, methodCall, methodInfo.getElementKey().getKeyGetter());

		return cacheFunction.apply(paramKeys);
	}

}
