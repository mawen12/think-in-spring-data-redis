package com.mawen.think.in.spring.data.redis.advanced.interceptor;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

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

	@Pointcut("@annotation(com.mawen.think.in.spring.data.redis.advanced.annotation.BatchCacheable)")
	public void batchCacheableAnnotation() {
	}

//	@Pointcut("execution(@com.mawen.think.in.spring.data.redis.advanced.annotation.BatchCacheable * *(..))")
//	public void batchCacheableMethod() {
//	}

	@Around(value = "batchCacheableAnnotation()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		if (log.isTraceEnabled()) {
			log.trace("Into BatchCacheableAspect......");
		}

		RedisCacheInvoker<Object> invoker = new RedisCacheInvoker<>(joinPoint, redisTemplate);

		return invoker.invoke();
	}
}
