package com.mawen.think.in.spring.data.redis.advanced.interceptor.support;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mawen.think.in.spring.data.redis.advanced.annotation.BatchCacheable;
import com.mawen.think.in.spring.data.redis.advanced.interceptor.parser.TypeParserRunner;
import lombok.Data;
import lombok.Getter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import org.springframework.data.util.Lazy;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/18
 */
@Getter
public class BatchCacheableMethodInfo {

	private Method method;

	private BatchCacheable batchCacheable;

	private int cacheParamIndex;

	private Class<?> cacheParamType;

	private Class<?> rawCacheParamType;

	private Class<?> returnType;

	private Class<?> rawReturnType;

	private Lazy<Boolean> validFlag;

	private ElementKey<Object> elementKey;


	public BatchCacheableMethodInfo(ProceedingJoinPoint joinPoint) {

		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		this.method = signature.getMethod();

		this.batchCacheable = method.getAnnotation(BatchCacheable.class);
		this.cacheParamIndex = extractCacheParamIndex();

		this.cacheParamType = cacheParamIndex != -1 ? method.getParameterTypes()[cacheParamIndex] : null;
		this.rawCacheParamType = cacheParamIndex != -1 ? extractActualType(method.getGenericParameterTypes()[cacheParamIndex]) : null;

		this.returnType = method.getReturnType();
		this.rawReturnType = extractActualType(method.getGenericReturnType());

		this.validFlag = Lazy.of(this::getValidFlag);

		this.elementKey = new ElementKey<>(batchCacheable, (Class)rawReturnType, TypeParserRunner.RUNNER.parse(rawReturnType));
	}

	public boolean isValid() {
		return validFlag.get();
	}

	public String getAnnotationKey() {
		return batchCacheable.key();
	}

	public List<Object> parseResult(Object result) {
		if (result == null) {
			return new ArrayList<>();
		}

		return (List<Object>)result;
	}

	private Class<?> extractActualType(Type type) {
		if (type instanceof ParameterizedType parameterizedType) {
			Type actualType = parameterizedType.getActualTypeArguments()[0];
			return (Class<?>) actualType;
		}
		return null;
	}

	private int extractCacheParamIndex() {
		int index = batchCacheable.argIndex();
		int parameterCount = method.getParameterCount();
		return index < 0 || index >= parameterCount ? -1 : index;
	}

	private boolean getValidFlag() {
		return Collection.class.isAssignableFrom(returnType)
				&& cacheParamIndex != -1
				&& TypeParserRunner.RUNNER.canParse(rawReturnType);
	}
}
