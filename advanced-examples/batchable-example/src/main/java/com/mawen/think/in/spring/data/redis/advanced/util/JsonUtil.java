package com.mawen.think.in.spring.data.redis.advanced.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

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


	public static <T> List<T> deserializeToList(String json, Class<T> clazz) {
		try {
			return deserializeToCollectionOrThrow(json, List.class, clazz);
		}
		catch (JsonProcessingException e) {
			return null;
		}
	}

	public static <T> Set<T> deserializeToSet(String json, Class<T> clazz) {
		try {
			return deserializeToCollectionOrThrow(json, Set.class, clazz);
		}
		catch (JsonProcessingException e) {
			return null;
		}
	}

	public static <C extends Collection<T>, T> C deserializeToCollectionOrThrow(String json, Class<C> collectionClazz, Class<T> clazz) throws JsonProcessingException {
		CollectionType type = TypeFactory.defaultInstance().constructCollectionType(collectionClazz, clazz);
		return MAPPER.readValue(json, type);
	}

	public static <T> Map<String, T> deserializeToStringMap(String json, Class<T> clazz) {
		try {
			return deserializeToMapOrThrow(json, Map.class, String.class, clazz);
		}
		catch (JsonProcessingException e) {
			return null;
		}
	}

	public static <K, V> Map<K, V> deserializeToMap(String json, Class<K> keyType, Class<V> valueType) {
		try {
			return deserializeToMapOrThrow(json, Map.class, keyType, valueType);
		}
		catch (JsonProcessingException e) {
			return null;
		}
	}

	public static <M extends Map<K, V>, K, V> M deserializeToMapOrThrow(String json, Class<M> mapClazz, Class<K> keyType, Class<V> valueType) throws JsonProcessingException {
		MapType type = TypeFactory.defaultInstance().constructMapType(mapClazz, keyType, valueType);
		return MAPPER.readValue(json, type);
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
