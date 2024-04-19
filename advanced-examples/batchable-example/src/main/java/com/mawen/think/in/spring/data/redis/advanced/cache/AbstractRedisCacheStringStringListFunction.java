package com.mawen.think.in.spring.data.redis.advanced.cache;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.mawen.think.in.spring.data.redis.core.CacheableListFunction;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/18
 */
public abstract class AbstractRedisCacheStringStringListFunction<U> extends CacheableListFunction<String, U> {

	private final Function<Collection<String>, List<String>> redisMGetter;

	private final Consumer<Map<String, String>> redisMSetter;

	private final Class<U> uClass;

	public AbstractRedisCacheStringStringListFunction(
			Function<Collection<String>, List<String>> redisMGetter,
			Consumer<Map<String, String>> redisMSetter,
			Class<U> uClass) {
		this.redisMGetter = redisMGetter;
		this.redisMSetter = redisMSetter;
		this.uClass = uClass;
	}

	@Override
	public List<U> apply(List<String> keys) {
		return redisMGetter.apply(keys)
				.stream()
				.filter(Objects::nonNull)
				.map(formatKey())
				.map(key -> deserializerGetter().apply(key, uClass))
				.collect(Collectors.toList());
	}

	@Override
	public List<U> cache(List<U> ts, Function<U, String> uniqueKeyGetter) {
		redisMSetter.accept(ts.stream().collect(Collectors.toMap(uniqueKeyGetter.andThen(formatKey()), serializerGetter())));
		return ts;
	}

	abstract Function<U, String> serializerGetter();

	abstract <T> BiFunction<String, Class<U>, U> deserializerGetter();

	Function<String, String> formatKey() {
		return Function.identity();
	}
}
