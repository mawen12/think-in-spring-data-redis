package com.mawen.think.in.spring.data.redis.core;

import java.util.function.BiConsumer;
import java.util.function.Function;

import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/22
 */
@Slf4j
public abstract class CacheableSingleFunction<K, V> implements SingleFunction<K, V> {

	@Override
	public V apply(K key) {
		return cacheGetter().apply(key);
	}

	public V cache(K key, V value) {
		cacheSetter().accept(key, value);
		return value;
	}

	public final Function<K, V> compose(Function<K, V> fallback) {
		return key -> {
			V value = apply(key);

			if (value != null) {
				if (log.isTraceEnabled()) {
					log.trace("{} Cache Hit!", getIdentifier());
				}
				return value;
			}

			if (log.isTraceEnabled()) {
				log.trace("{} Cache Miss!", getIdentifier());
			}

			value = fallback.apply(key);

			cache(key, value);

			return value;
		};
	}

	protected abstract Function<K, V> cacheGetter();

	protected abstract BiConsumer<K, V> cacheSetter();

	protected abstract String getIdentifier();


	public static <K, V> Function<K, V> of(CacheableSingleFunction<K, V> cacheGetter, Function<K, V> fallback) {
		return cacheGetter.compose(fallback);
	}
}
