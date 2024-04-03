package com.mawen.think.in.spring.data.redis.serializer;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/3
 */
public class LongRedisSerializer implements RedisSerializer<Long> {

	private RedisSerializer<String> delegate;

	public LongRedisSerializer() {
		this.delegate = new StringRedisSerializer();
	}

	public LongRedisSerializer(RedisSerializer<String> delegate) {
		this.delegate = delegate != null ? delegate : new StringRedisSerializer();
	}

	@Override
	public byte[] serialize(Long value) throws SerializationException {
		return delegate.serialize(String.valueOf(value));
	}

	@Override
	public Long deserialize(byte[] bytes) throws SerializationException {
		String value = delegate.deserialize(bytes);
		return value == null ? null : Long.valueOf(value);
	}
}
