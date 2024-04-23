package com.mawen.think.in.spring.data.redis.core;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/3
 */
public abstract class CacheableListFunction<K, V> implements ListFunction<K, V> {

	@Override
	public Function<List<K>, List<V>> compose(Function<List<K>, List<V>> fallbackGetter, Function<V, K> uniqueKeyGetter) {
		return ListFunction.super.compose(fallbackGetter.andThen(ret -> cache(ret,uniqueKeyGetter)), uniqueKeyGetter);
	}

	public List<V> cache(List<V> values, Function<V, K> uniqueKeyGetter) {
		cacheSetter().accept(values.stream().map(uniqueKeyGetter).toList(), values);

		return values;
	}

	protected abstract Function<List<K>, List<V>> cacheGetter();

	protected abstract BiConsumer<List<K>, List<V>> cacheSetter();

	protected abstract Function<V, K> uniqueKeyGetter();
}
