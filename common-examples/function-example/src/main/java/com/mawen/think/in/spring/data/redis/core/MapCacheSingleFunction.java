package com.mawen.think.in.spring.data.redis.core;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.util.Assert;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/22
 */
public class MapCacheSingleFunction<K, V> extends CacheableSingleFunction<K, V> {

	private Map<K, V> cache;

	private String identifier;


	public MapCacheSingleFunction(Map<K, V> cache, String identifier) {
		Assert.notNull(cache, "Cache must not be null");
		Assert.hasText(identifier, "Identifier must not be empty");

		this.cache = cache;
		this.identifier = identifier;
	}

	public MapCacheSingleFunction(Supplier<Map<K, V>> cacheSupplier, String identifier) {
		Assert.notNull(cacheSupplier, "Cache Supplier must not be null");
		Assert.hasText(identifier, "Identifier must not be empty");

		this.cache = cacheSupplier.get();
		Assert.notNull(this.cache, "Cache must not be null");
		this.identifier = identifier;
	}

	public MapCacheSingleFunction(String identifier) {
		this(new HashMap<>(), identifier);
	}


	@Override
	protected Function<K, V> cacheGetter() {
		return cache::get;
	}

	@Override
	protected BiConsumer<K, V> cacheSetter() {
		return cache::put;
	}

	@Override
	protected String getIdentifier() {
		return identifier;
	}
}
