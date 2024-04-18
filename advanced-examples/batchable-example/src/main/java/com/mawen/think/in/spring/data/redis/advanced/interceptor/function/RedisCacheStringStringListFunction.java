package com.mawen.think.in.spring.data.redis.advanced.interceptor.function;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import com.mawen.think.in.spring.data.redis.advanced.util.JsonUtil;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/18
 */
public class RedisCacheStringStringListFunction<U> extends AbstractRedisCacheStringStringListFunction<U> {

	private Function<String, String> keyConverter;

	public RedisCacheStringStringListFunction(Function<Collection<String>, List<String>> redisMGetter,
			Consumer<Map<String, String>> redisMSetter,
			Class<U> uClass,
			Function<String, String> keyConverter) {
		super(redisMGetter, redisMSetter, uClass);
		this.keyConverter = keyConverter;
	}

	@Override
	Function<U, String> serializerGetter() {
		return JsonUtil::serialize;
	}

	@Override
	BiFunction<String, Class<U>, U> deserializerGetter() {
		return JsonUtil::deserialize;
	}

	@Override
	Function<String, String> formatKey() {
		return keyConverter;
	}
}
