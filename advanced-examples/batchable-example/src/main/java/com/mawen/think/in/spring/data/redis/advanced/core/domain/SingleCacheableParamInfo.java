package com.mawen.think.in.spring.data.redis.advanced.core.domain;

import java.util.function.Function;

import com.mawen.think.in.spring.data.redis.advanced.util.JsonUtil;
import lombok.Getter;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/19
 */
@Getter
public class SingleCacheableParamInfo extends AbstractCacheableParamInfo {

	private final Function<Object, String> serializer;


	public SingleCacheableParamInfo() {
		this.serializer = JsonUtil::serialize;
	}

	@Override
	boolean isValid() {
		return true;
	}
}
