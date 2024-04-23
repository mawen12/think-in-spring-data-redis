package com.mawen.think.in.spring.data.redis.advanced.core.cache;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import com.mawen.think.in.spring.data.redis.core.CacheableListFunction;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/23
 */
public class RedisCacheListFunction<V> extends CacheableListFunction<String, V> {

	private final Function<List<String>, List<String>> redisMGetter;

	private final Consumer<Map<String, String>> redisMSetter;

	private final Class<V> vClass;

	private final Function<String, String> keyConverter;

	private final Function<String, V> valueDeserializer;

	private final String identifier;


	public RedisCacheListFunction(Function<List<String>, List<String>> redisMGetter,
			Consumer<Map<String, String>> redisMSetter,
			Class<V> vClass,
			Function<String, String> keyConverter,
			Function<String, V> valueDeserializer,
			String identifier) {
		this.redisMGetter = redisMGetter;
		this.redisMSetter = redisMSetter;
		this.vClass = vClass;
		this.keyConverter = keyConverter;
		this.valueDeserializer = valueDeserializer;
		this.identifier = identifier;
	}


	@Override
	protected Function<List<String>, List<V>> cacheGetter() {
		return null;
	}

	@Override
	protected BiConsumer<List<String>, List<V>> cacheSetter() {
		return null;
	}

	@Override
	protected Function<V, String> uniqueKeyGetter() {
		return ;
	}
}
