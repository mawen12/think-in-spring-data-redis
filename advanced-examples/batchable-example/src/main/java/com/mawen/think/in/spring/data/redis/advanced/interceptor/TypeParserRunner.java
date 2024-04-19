package com.mawen.think.in.spring.data.redis.advanced.interceptor;

import java.util.HashMap;
import java.util.Map;

import com.mawen.think.in.spring.data.redis.advanced.interceptor.parser.IBaseKeyParser;
import com.mawen.think.in.spring.data.redis.advanced.interceptor.parser.IKeyBaseKeyParser;
import com.mawen.think.in.spring.data.redis.advanced.interceptor.parser.KeyParser;
import com.mawen.think.in.spring.data.redis.advanced.pojo.IBase;
import com.mawen.think.in.spring.data.redis.advanced.pojo.IKeyBase;
import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/18
 */
@Slf4j
public class TypeParserRunner {

	private static final Map<Class<?>, KeyParser<?, ?>> PARSER_MAP = new HashMap<>(3);

	static {
		PARSER_MAP.put(IBase.class, IBaseKeyParser.INSTANCE);
		PARSER_MAP.put(IKeyBase.class, IKeyBaseKeyParser.INSTANCE);
	}

	public static void register(Class<?> clazz, KeyParser<?, ?> keyParser) {
		PARSER_MAP.put(clazz, keyParser);
	}

	public static KeyParser<?, ?> getTypeParser(Class<?> clazz) {
		return PARSER_MAP.get(clazz);
	}
}
