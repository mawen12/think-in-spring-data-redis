package com.mawen.think.in.sring.data.redis.usecase.pojo;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/2
 */
public record User(Long id, String name, Integer age) implements Serializable {

	@Serial
	private static final long serialVersionUID = 8056510510395152633L;


}
