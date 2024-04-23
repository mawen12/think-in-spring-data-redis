package com.mawen.think.in.spring.data.redis.advanced.core.interceptor;

import com.mawen.think.in.spring.data.redis.advanced.core.RedisCacheBatchInvoker;
import com.mawen.think.in.spring.data.redis.advanced.core.RedisCacheInvoker;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
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
	private StringRedisTemplate redisTemplate;

	@Pointcut("@annotation(com.mawen.think.in.spring.data.redis.advanced.core.annotation.BatchCacheable)")
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

//		RedisCacheInvoker<Object> invoker = new RedisCacheInvoker<>(joinPoint, redisTemplate);

		RedisCacheBatchInvoker<Object> invoker = new RedisCacheBatchInvoker<>(joinPoint, redisTemplate);

		return invoker.invoke();
	}
}
