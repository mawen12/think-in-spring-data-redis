package com.mawen.think.in.spring.data.redis.core;

import java.util.List;
import java.util.function.Function;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/3
 */
@FunctionalInterface
public interface CacheableListFunction<T, R> extends ListFunction<T, R> {

	@Override
	default Function<List<T>, List<R>> compose(Function<List<T>, List<R>> fallbackGetter, Function<R, T> uniqueKeyGetter) {
		return ListFunction.super.compose(fallbackGetter.andThen(this::doCache), uniqueKeyGetter);
	}

	default List<R> doCache(List<R> ts) {
		// put into cache
		System.out.println("Cache result: " + ts);
		return ts;
	}
}
