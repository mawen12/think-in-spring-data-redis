package com.mawen.think.in.spring.data.redis.advanced.core.cache;

import java.util.function.BiConsumer;
import java.util.function.Function;

import com.mawen.think.in.spring.data.redis.core.CacheableSingleFunction;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/22
 */
public class RedisCacheSingleFunction<V> extends CacheableSingleFunction<Object, V> {

	private final Function<String, String> redisGetter;

	private final BiConsumer<String, String> redisSetter;

	private final Function<Object, String> commonSerializer;

	private final Function<Object, String> composeKeySerializer;

	private final Function<String, V> valueDeserializer;

	private final String identifier;


	public RedisCacheSingleFunction(Function<String, String> redisGetter, BiConsumer<String, String> redisSetter,
			Function<Object, String> commonSerializer,
			Function<String, String> keyFormatter,
			Function<String, V> valueDeserializer,
			String identifier) {
		this.redisGetter = redisGetter;
		this.redisSetter = redisSetter;
		this.commonSerializer = commonSerializer;
		this.composeKeySerializer = commonSerializer.andThen(keyFormatter);
		this.valueDeserializer = valueDeserializer;
		this.identifier = identifier;
	}

	@Override
	protected Function<Object, V> cacheGetter() {
		return key -> {

			String redisKey = composeKeySerializer.apply(key);

			String json = redisGetter.apply(redisKey);
			return valueDeserializer.apply(json);
		};
	}

	@Override
	protected BiConsumer<Object, V> cacheSetter() {
		return (key, value) -> {

			String redisKey = composeKeySerializer.apply(key);
			String redisValue = commonSerializer.apply(value);

			redisSetter.accept(redisKey, redisValue);
		};
	}

	@Override
	protected String getIdentifier() {
		return identifier;
	}
}
