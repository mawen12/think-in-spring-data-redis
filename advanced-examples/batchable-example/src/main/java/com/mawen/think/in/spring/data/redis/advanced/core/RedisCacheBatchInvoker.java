package com.mawen.think.in.spring.data.redis.advanced.core;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import com.mawen.think.in.spring.data.redis.advanced.core.cache.RedisCacheStringStringListFunction;
import com.mawen.think.in.spring.data.redis.advanced.core.domain.BatchCacheableMethodInfo;
import com.mawen.think.in.spring.data.redis.core.ListFunction;
import org.aspectj.lang.ProceedingJoinPoint;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/23
 */
public class RedisCacheBatchInvoker<R> extends AbstractRedisCacheInvoker {

	private BatchCacheableMethodInfo<R> methodInfo;

	private Function<Collection<String>, List<String>> redisMGetter;

	private Consumer<Map<String, String>> redisMSetter;


	public RedisCacheBatchInvoker(ProceedingJoinPoint joinPoint, StringRedisTemplate redisTemplate) {
		super(joinPoint, redisTemplate);

		this.methodInfo = new BatchCacheableMethodInfo<>(method);
		this.redisMGetter = keys -> redisTemplate.opsForValue().multiGet(keys);
		this.redisMSetter = map -> redisTemplate.executePipelined(new DefaultRedisCallback(map, methodInfo.getExpiration()));
	}


	@Override
	public Object invoke() throws Throwable {

		if (!methodInfo.isValid()) {
			return joinPoint.proceed();
		}

		ParametersValueHolder valueHolder = new ParametersValueHolder(joinPoint.getArgs(), methodInfo.getParamInfo());

		Function<List<String>, List<R>> function = ListFunction.of(getRedisCacheCall(), getMethodCall(valueHolder), methodInfo.getKeyParser().parse());

		return function.apply(valueHolder.getKeys());
	}

	private Function<List<String>, List<R>> getMethodCall(ParametersValueHolder parametersValueHolder) {
		return keys -> {
			try {
				Object result = joinPoint.proceed(parametersValueHolder.getMergedArgs(keys));
				return methodInfo.parseResult(result);
			}
			catch (Throwable e) {
				throw new RuntimeException(e);
			}
		};
	}

	private RedisCacheStringStringListFunction<R> getRedisCacheCall() {
		return new RedisCacheStringStringListFunction<>(
				redisMGetter,
				redisMSetter,
				methodInfo.getRawReturnType(),
				key -> methodInfo.getAnnotationKey() + ":" + key,
				methodInfo.getMethodShortName()
		);
	}

	private record DefaultRedisCallback(Map<String, String> map, Expiration expiration) implements RedisCallback<Void> {

		@Nullable
		@Override
		public Void doInRedis(@NonNull RedisConnection connection) throws DataAccessException {
			StringRedisConnection stringRedisConnection = (StringRedisConnection) connection;

			BiConsumer<String, String> consumer;
			if (expiration == null) {
				consumer = stringRedisConnection::set;
			} else {
				consumer = (k, v) -> stringRedisConnection.setEx(k, expiration.getExpirationTime(), v);
			}

			map.forEach(consumer);
			return null;
		}
	}
}
