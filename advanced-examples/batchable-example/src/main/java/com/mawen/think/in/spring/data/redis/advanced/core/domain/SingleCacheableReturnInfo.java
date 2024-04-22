package com.mawen.think.in.spring.data.redis.advanced.core.domain;

import java.lang.reflect.Method;
import java.util.function.Function;

import com.mawen.think.in.spring.data.redis.advanced.util.JsonUtil;
import lombok.Getter;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/19
 */
@Getter
public class SingleCacheableReturnInfo<R> extends AbstractCacheableReturnInfo {

	private final Class<R> returnType;

	private final Function<R, String> serializer;

	private final Function<String, R> deserializer;


	SingleCacheableReturnInfo(Method method) {
		super(method);

		this.returnType = (Class<R>) method.getReturnType();
		this.serializer = JsonUtil::serialize;
		this.deserializer = isCollection() ? getListDeserializer() : isMap() ? getMapDeserializer() : getDefaultSerializer();
	}


	@Override
	boolean isValid() {
		return true;
	}

	private Function<String, R> getMapDeserializer() {
		return json -> (R) JsonUtil.deserializeToMap(json, rawTypes.get(0), rawTypes.get(1));
	}

	private Function<String, R> getListDeserializer() {
		return json -> (R) JsonUtil.deserialize(json, rawTypes.get(0));
	}

	private Function<String, R> getDefaultSerializer() {
		return json -> JsonUtil.deserialize(json, returnType);
	}
}
