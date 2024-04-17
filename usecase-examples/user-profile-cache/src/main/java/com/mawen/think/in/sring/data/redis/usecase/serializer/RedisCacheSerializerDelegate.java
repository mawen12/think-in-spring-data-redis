package com.mawen.think.in.sring.data.redis.usecase.serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.core.serializer.Deserializer;
import org.springframework.core.serializer.Serializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.Assert;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/17
 */
public class RedisCacheSerializerDelegate<T> implements Serializer<T>, Deserializer<T> {

	private final RedisSerializer<T> serializer;

	public RedisCacheSerializerDelegate(RedisSerializer<T> serializer) {
		Assert.notNull(serializer, "RedisSerializer must not be null");
		this.serializer = serializer;
	}

	@Override
	public void serialize(T object, OutputStream outputStream) throws IOException {
		byte[] bytes = serializer.serialize(object);
		outputStream.write(bytes);
		outputStream.flush();
	}

	@Override
	public T deserialize(InputStream inputStream) throws IOException {
		return serializer.deserialize(inputStream.readAllBytes());
	}
}
