package com.mawen.think.in.spring.data.redis.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/17
 */
public final class JsonUtil {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	@Nullable
	public static <T> T deserialize(@Nullable String json, Class<T> clazz) {
		try {
			return StringUtils.hasText(json) ? deserializeOrThrow(json, clazz) : null;
		}
		catch (JsonProcessingException e) {
			return null;
		}
	}

	public static <T> T deserializeOrThrow(String json, Class<T> clazz) throws JsonProcessingException {
		return MAPPER.readValue(json, clazz);
	}

	@Nullable
	public static <T> String serialize(@Nullable T obj) {
		try {
			return obj != null ? serializeOrThrow(obj) : null;
		}
		catch (JsonProcessingException e) {
			return null;
		}
	}

	public static <T> String serializeOrThrow(T obj) throws JsonProcessingException {
		return MAPPER.writeValueAsString(obj);
	}
}
