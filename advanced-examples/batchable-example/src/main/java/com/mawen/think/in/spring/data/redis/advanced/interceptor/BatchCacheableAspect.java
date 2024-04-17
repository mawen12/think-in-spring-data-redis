package com.mawen.think.in.spring.data.redis.advanced.interceptor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.mawen.think.in.spring.data.redis.advanced.annotation.BatchCacheable;
import com.mawen.think.in.spring.data.redis.advanced.pojo.IBase;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/17
 */
@Slf4j
@Aspect
@Component
public class BatchCacheableAspect {

	private ConcurrentMap<String, ElementKey> map = new ConcurrentHashMap<>(3);

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Pointcut("@annotation(com.mawen.think.in.spring.data.redis.advanced.annotation.BatchCacheable)")
	public void batchCacheableAnnotation() {}

	@Pointcut("execution(@com.mawen.think.in.spring.data.redis.advanced.annotation.BatchCacheable * *(..))")
	public void batchCacheableMethod() {}

	@Around(value = "batchCacheableMethod()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		if (log.isTraceEnabled()) {
			log.trace("Info BatchCacheableAspect#around...");
		}

		// get from method cache

		// check method is valid

		validMethod(joinPoint);

		// extract args
//		Collection<String> keys = extractKeys(joinPoint);

		// query by args
//		List<String> cachedItems = redisTemplate.opsForValue().multiGet(keys);

		// invoke method

//		joinPoint.proceed();

		// put into redis

		return joinPoint.proceed();
	}

	private boolean validMethod(ProceedingJoinPoint joinPoint) {

		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		BatchCacheable batchCacheable = signature.getMethod().getAnnotation(BatchCacheable.class);
		Type type = signature.getMethod().getGenericReturnType();

		if (type instanceof ParameterizedType parameterizedType) {
			Type actualType = parameterizedType.getActualTypeArguments()[0];
			if (actualType instanceof IBase) {
				if (log.isTraceEnabled()) {
					log.trace("JoinPoint[{}] expected return actual type is IBase.class, but actual type is {}", joinPoint.toLongString(), actualType);
				}
				return false;
			}
			map.put(signature.getMethod().getName(), new ElementKey<>(batchCacheable, actualType, IBase::getId));
			return true;
		} else {
			if (log.isTraceEnabled()) {
				log.trace("JoinPoint[{}] expected Return Type is Collection or its subclass, but actual type is {}", joinPoint.toLongString(), type);
			}

			return false;
		}
	}

	private Collection<String> extractKeys(ProceedingJoinPoint joinPoint) {

		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		BatchCacheable batchCacheable = signature.getMethod().getAnnotation(BatchCacheable.class);

		Object[] args = joinPoint.getArgs();
		int index = batchCacheable.argIndex();
		if (index < 0 || index >= args.length) {
			if (log.isTraceEnabled()) {
				log.trace("JoinPoint[{}] has invalid index, because index is out of range", joinPoint.toLongString());
			}
			return null;
		}

		Object arg = args[index];
		if (!(arg instanceof Collection<?> collection)) {
			if (log.isTraceEnabled()) {
				log.trace("JoinPoint[{}] expected collection type argument in {}, but get actual type {}", joinPoint.toLongString(), index, arg.getClass());
			}
			return null;
		}

		if (arg == null) {
			if (log.isTraceEnabled()) {
				log.trace("JoinPoint[{}] expected non-null argument in {}, but get null", joinPoint.toLongString(), index);
			}
			return null;
		}

		return doExtractKeys(collection, batchCacheable.key());
	}

	private Collection<String> doExtractKeys(Collection<?> collection, String key) {
		return collection.stream().map(element -> parseKey(key, element)).collect(Collectors.toList());
	}

	private String parseKey(String key, Object suffix) {
		String suffixStr = null;
		if (suffix instanceof Integer intKey) {
			suffixStr = Integer.toString(intKey);
		}
		else if (suffix instanceof String strKey) {
			suffixStr = strKey;
		}
		else {
			suffixStr = Objects.toString(suffix);
		}

		return key + ":" + suffixStr;
	}


	@Data
	private static class ElementKey<T, K> {
		private BatchCacheable batchCacheable;
		private Type actualType;
		private Function<T, K> keyGetter;

		public ElementKey(BatchCacheable batchCacheable, Type actualType, Function<T, K> keyGetter) {

			this.batchCacheable = batchCacheable;
			this.actualType = actualType;
			this.keyGetter = keyGetter;
		}

		public static ElementKey<Object, Object> EMPTY = new ElementKey<>(null, null, null);

	}
}
