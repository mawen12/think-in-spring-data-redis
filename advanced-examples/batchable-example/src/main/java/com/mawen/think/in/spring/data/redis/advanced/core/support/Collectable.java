package com.mawen.think.in.spring.data.redis.advanced.core.support;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/18
 */
public interface Collectable {

	default <C extends Collectable, U> Stream<U> getStream(Function<C, List<U>> listGetter) {
		return listGetter.apply((C) this).stream();
	}

	default <C extends Collectable, U, R> Stream<R> getStream(Function<C, List<U>> listGetter, Function<U, R> mapper) {
		return listGetter.apply((C) this).stream().map(mapper);
	}

	default <U, A, R> R collect(Stream<U> stream, Collector<U, A, R> collector) {
		return stream.collect(collector);
	}

	default <U> List<U> collectToList(Stream<U> stream) {
		return collect(stream, Collectors.toList());
	}

	default <U> Set<U> collectToSet(Stream<U> stream) {
		return collect(stream, Collectors.toSet());
	}

	default <U> void forEach(Stream<U> stream, Consumer<U> action) {
		stream.forEach(action);
	}
}
