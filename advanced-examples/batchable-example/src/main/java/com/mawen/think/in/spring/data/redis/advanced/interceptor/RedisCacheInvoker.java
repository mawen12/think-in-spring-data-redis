package com.mawen.think.in.spring.data.redis.advanced.interceptor;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.mawen.think.in.spring.data.redis.advanced.cache.RedisCacheStringStringListFunction;
import com.mawen.think.in.spring.data.redis.advanced.interceptor.support.BatchCacheableMethodInfo;
import com.mawen.think.in.spring.data.redis.advanced.interceptor.support.BatchCacheableMethodInfoFactory;
import com.mawen.think.in.spring.data.redis.advanced.interceptor.support.ParametersValueHolder;
import com.mawen.think.in.spring.data.redis.core.ListFunction;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/19
 */
@Slf4j
public class RedisCacheInvoker<R> {

	private final ProceedingJoinPoint joinPoint;

	private final StringRedisTemplate redisTemplate;

	private final BatchCacheableMethodInfo<R> methodInfo;

	private BiFunction<RedisTemplate<String, String>, Collection<String>, List<String>> redisMultiGetter = (redisTemplate, keys) -> redisTemplate.opsForValue().multiGet(keys);

	private BiConsumer<StringRedisTemplate, RedisCallback<Void>> redisMultiSetter = StringRedisTemplate::executePipelined;


	public RedisCacheInvoker(ProceedingJoinPoint joinPoint, StringRedisTemplate redisTemplate) {
		this.joinPoint = joinPoint;
		this.redisTemplate = redisTemplate;

		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		this.methodInfo = BatchCacheableMethodInfoFactory.build(methodSignature.getMethod());
	}

	public Object invoke() throws Throwable {
		if (!methodInfo.isValid()) {
			return joinPoint.proceed();
		}

		ParametersValueHolder parametersValueHolder = new ParametersValueHolder(joinPoint.getArgs(), methodInfo.getParamInfo());

		Function<List<String>, List<R>> methodCall = keys -> {
			try {
				Object result = joinPoint.proceed(parametersValueHolder.getMergedArgs(keys));
				return methodInfo.parseResult(result);
			}
			catch (Throwable e) {
				throw new RuntimeException(e);
			}
		};

		Function<BatchCacheableMethodInfo<R>, RedisCacheStringStringListFunction<R>> redisCacheGetter = batchCacheableMethodInfo -> new RedisCacheStringStringListFunction<>(
				keys -> redisMultiGetter.apply(redisTemplate, keys),
				map -> redisMultiSetter.accept(redisTemplate, new DefaultRedisCallback(map, methodInfo.getExpiration())),
				batchCacheableMethodInfo.getRawReturnType(),
				key -> batchCacheableMethodInfo.getAnnotationKey() + ":" + key,
				methodInfo.getMethodShortName()
		);

		ListFunction<String, R> redisCacheFunction = redisCacheGetter.apply(methodInfo);

		return ListFunction.of(redisCacheFunction, methodCall, methodInfo.getKeyParser().parse())
				.apply(parametersValueHolder.getKeys());
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
