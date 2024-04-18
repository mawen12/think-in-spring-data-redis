package com.mawen.think.in.spring.data.redis.advanced.interceptor.holder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.mawen.think.in.spring.data.redis.advanced.interceptor.support.Collectable;
import lombok.Data;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/18
 */
@Data
public class ParametersValueHolder implements Collectable, Converter<Object, String> {

	private Object[] args;

	private int cacheParamIndex;

	private Class<?> rawCacheParamType;

	private Collection<?> sourceCacheParamValue;


	public ParametersValueHolder(Object[] args, int cacheParamIndex, Class<?> rawCacheParamType) {
		this.args = args;
		this.cacheParamIndex = cacheParamIndex;
		this.rawCacheParamType = rawCacheParamType;
		this.sourceCacheParamValue = (Collection<?>) args[cacheParamIndex];
	}

	public List<String> collectCacheKeys() {
		return collectToList(sourceCacheParamValue.stream().map(this::convert));
	}

	public Object[] getMergedArgs(List<String> newArg) {
		Set<String> keys = new HashSet<>(newArg);
		Iterator<?> iterator = sourceCacheParamValue.iterator();
		while (iterator.hasNext()) {
			Object key = iterator.next();
			if (!keys.contains(convert(key))) {
				iterator.remove();
			}
		}

		this.args[cacheParamIndex] = sourceCacheParamValue;
		return this.args;
	}

	@Override
	public String convert(@NonNull Object key) {
		String keyStr = null;

		if (key instanceof Integer intKey) {
			keyStr = Integer.toString(intKey);
		}
		else if (key instanceof String strKey) {
			keyStr = strKey;
		}
		else {
			keyStr = Objects.toString(key);
		}

		return keyStr;
	}
}
