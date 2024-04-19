package com.mawen.think.in.spring.data.redis.advanced.interceptor.support;

import java.lang.reflect.Method;
import java.util.List;

import com.mawen.think.in.spring.data.redis.advanced.annotation.BatchCacheable;
import lombok.Getter;

import org.springframework.data.util.Lazy;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/18
 */
@Getter
public class BatchCacheableMethodInfo<T> {

	private final Method method;

	private final BatchCacheable batchCacheable;

	private final BatchCacheableParamInfo paramInfo;

	private final BatchCacheableReturnInfo<T> returnInfo;

	private final Lazy<Boolean> validFlag;


	public BatchCacheableMethodInfo(Method method) {
		this.method = method;
		this.batchCacheable = method.getAnnotation(BatchCacheable.class);
		this.paramInfo = new BatchCacheableParamInfo(method, extractCacheParamIndex());
		this.returnInfo = new BatchCacheableReturnInfo<>(method, batchCacheable.type());

		this.validFlag = Lazy.of(this::getValidFlag);
	}

	public String getAnnotationKey() {
		return batchCacheable.key();
	}

	public List<T> parseResult(Object result) {
		return result != null ? (List<T>)result : null;
	}

	public boolean isValid() {
		return validFlag.get();
	}

	private int extractCacheParamIndex() {
		int index = batchCacheable.argIndex();
		int parameterCount = method.getParameterCount();
		return index < 0 || index >= parameterCount ? -1 : index;
	}

	private boolean getValidFlag() {
		return paramInfo.isValid() && returnInfo.isValid() ;
	}
}
