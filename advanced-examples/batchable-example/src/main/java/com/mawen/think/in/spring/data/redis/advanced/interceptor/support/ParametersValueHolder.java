package com.mawen.think.in.spring.data.redis.advanced.interceptor.support;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.mawen.think.in.spring.data.redis.advanced.support.Collectable;
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

	private BatchCacheableParamInfo paramInfo;

	private Collection<?> sourceCacheParamValue;


	public ParametersValueHolder(Object[] args, BatchCacheableParamInfo paramInfo) {
		this.args = args;
		this.paramInfo = paramInfo;
		this.sourceCacheParamValue = (Collection<?>) args[paramInfo.getParamIndex()];
	}


	public List<String> getKeys() {
		return collectToList(sourceCacheParamValue.stream().map(this::convert));
	}

	public Object[] getMergedArgs(List<String> newArg) {
		Set<String> keys = new HashSet<>(newArg);
		sourceCacheParamValue.removeIf(key -> !keys.contains(convert(key)));

		this.args[paramInfo.getParamIndex()] = sourceCacheParamValue;
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
