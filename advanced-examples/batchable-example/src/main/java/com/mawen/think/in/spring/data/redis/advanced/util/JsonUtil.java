package com.mawen.think.in.spring.data.redis.advanced.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/17
 */
public final class JsonUtil {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	public static <T> T deserialize(String json, Class<T> clazz) {
		try {
			return deserializeOrThrow(json, clazz);
		}
		catch (JsonProcessingException e) {
			return null;
		}
	}

	public static <T> T deserializeOrThrow(String json, Class<T> clazz) throws JsonProcessingException {
		return MAPPER.readValue(json, clazz);
	}

	public static <T> String serialize(T obj) {
		try {
			return serializeOrThrow(obj);
		}
		catch (JsonProcessingException e) {
			return null;
		}
	}

	public static <T> String serializeOrThrow(T obj) throws JsonProcessingException {
		return MAPPER.writeValueAsString(obj);
	}
}
