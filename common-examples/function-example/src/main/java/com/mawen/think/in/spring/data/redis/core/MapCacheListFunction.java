package com.mawen.think.in.spring.data.redis.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.util.Assert;

/**
 * Base Cache
 *
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/4
 */
public class MapCacheListFunction<T, R> extends CacheableListFunction<T, R> {

	private final Map<T, R> cache;


	MapCacheListFunction() {
		this(new HashMap<>());
	}

	MapCacheListFunction(Supplier<Map<T, R>> mapSupplier) {
		Assert.notNull(mapSupplier,"MapSupplier must not be null");
		Map<T, R> map = mapSupplier.get();
		Assert.notNull(map,"Map from Supplier must not be null");
		this.cache = map;
	}

	MapCacheListFunction(Map<T, R> map) {
		Assert.notNull(map,"Map must not be null");
		this.cache = map;
	}


	@Override
	public List<R> apply(List<T> ts) {
		if (!cache.isEmpty()) {
			return ts.stream().map(cache::get).filter(Objects::nonNull).toList();
		}

		return new ArrayList<>();
	}

	@Override
	public List<R> cache(List<R> values, Function<R, T> uniqueKeyGetter) {
		values.forEach(element -> cache.put(uniqueKeyGetter.apply(element), element));

		return values;
	}
}
