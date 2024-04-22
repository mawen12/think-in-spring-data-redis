package com.mawen.think.in.spring.data.redis.advanced.core;

import java.lang.reflect.Method;

import com.mawen.think.in.spring.data.redis.advanced.core.support.Invokable;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/22
 */
public abstract class AbstractRedisCacheInvoker implements Invokable {

	protected final ProceedingJoinPoint joinPoint;

	protected final StringRedisTemplate redisTemplate;

	protected final Method method;


	protected AbstractRedisCacheInvoker(ProceedingJoinPoint joinPoint, StringRedisTemplate redisTemplate) {
		this.joinPoint = joinPoint;
		this.redisTemplate = redisTemplate;
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		this.method = methodSignature.getMethod();
	}

}
