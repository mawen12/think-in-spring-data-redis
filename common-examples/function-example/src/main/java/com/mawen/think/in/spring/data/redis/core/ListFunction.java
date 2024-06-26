package com.mawen.think.in.spring.data.redis.core;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/3
 */
@FunctionalInterface
public interface ListFunction<T, R> extends SingleFunction<List<T>, List<R>> {

	Logger log = LoggerFactory.getLogger(ListFunction.class);

	default Function<List<T>, List<R>> compose(Function<List<T>, List<R>> fallbackGetter, Function<R, T> uniqueKeyGetter) {
		return ts -> {
			List<R> cacheResult = apply(ts);

			if (cacheResult.size() == ts.size()) {
				if (log.isTraceEnabled()) {
					log.trace("{} Cache Hit All! Key size: {}.", getIdentifier(), cacheResult.size());
				}
				return cacheResult;
			}

			if (log.isTraceEnabled()) {
				if (cacheResult.isEmpty()) {
					log.trace("{} Cache Miss! Key size: {}.", getIdentifier(), ts.size());
				}
				else {
					log.trace("{} Cache Hit Part! Hit Key size: {}, Miss Key Size: {}.", getIdentifier(), cacheResult.size(), ts.size() - cacheResult.size());
				}
			}

			Set<T> cacheKeys = cacheResult.stream().map(uniqueKeyGetter).collect(Collectors.toSet());
			List<T> nonExistInCacheKeys = ts.stream().filter(t -> !cacheKeys.contains(t)).toList();

			List<R> dbResult = fallbackGetter.apply(nonExistInCacheKeys);

			return Stream.of(cacheResult, dbResult).filter(Objects::nonNull).flatMap(Collection::stream).toList();
		};
	}

	default String getIdentifier() {
		return null;
	};

	static <T, R> Function<List<T>, List<R>> of(ListFunction<T, R> getter, Function<List<T>, List<R>> fallbackGetter, Function<R, T> uniqueKeyGetter) {
		return getter.compose(fallbackGetter, uniqueKeyGetter);
	}

	static <T, R> Function<List<T>, List<R>> ofMap(Function<List<T>, List<R>> fallbackGetter, Function<R, T> uniqueKeyGetter) {
		MapCacheListFunction<T, R> listFunction = new MapCacheListFunction<>() {};
		return of(listFunction,fallbackGetter,uniqueKeyGetter);
	}
}
