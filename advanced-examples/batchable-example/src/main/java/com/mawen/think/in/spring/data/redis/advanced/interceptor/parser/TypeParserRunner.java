package com.mawen.think.in.spring.data.redis.advanced.interceptor.parser;

import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Function;

import lombok.extern.slf4j.Slf4j;

import org.springframework.util.Assert;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/18
 */
@Slf4j
public class TypeParserRunner {

	private static final String LOG_MESSAGE = "All TypeParser{} can not parse Class [{}]";

	public static TypeParserRunner RUNNER = new TypeParserRunner(List.of(
			IBaseTypeParser.INSTANCE,
			IKeyBaseTypeParser.INSTANCE
	));

	private final List<TypeParser<?>> typeParser;


	private TypeParserRunner(List<TypeParser<?>> typeParsers) {
		Assert.notNull(typeParsers, "typeParsers must not be null");
		this.typeParser = typeParsers;
	}

	public boolean canParse(Type type) {
		for (TypeParser<?> typeParser : typeParser) {
			if (typeParser.canParse(type)) {
				return true;
			}
		}
		logTrace(type.getTypeName());
		return false;
	}

	public Function<?, String> parse(Type type) {
		for (TypeParser<?> parser : typeParser) {
			if (parser.canParse(type)) {
				return parser.parse();
			}
		}
		logTrace(type.getTypeName());
		return null;
	}

	private void logTrace(String typeName) {
		if (log.isTraceEnabled()) {
			log.trace(LOG_MESSAGE, typeParser, typeName);
		}
	}
}
