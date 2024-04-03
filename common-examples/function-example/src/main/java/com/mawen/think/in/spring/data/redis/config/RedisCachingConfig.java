package com.mawen.think.in.spring.data.redis.config;

import java.lang.reflect.Field;

import com.mawen.think.in.spring.data.redis.pojo.User;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisElementReader;
import org.springframework.data.redis.serializer.RedisElementWriter;
import org.springframework.data.redis.serializer.RedisSerializationContext;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/3
 */
@Slf4j
@Configuration
public class RedisCachingConfig {

	@Bean
	public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory, Jackson2JsonRedisSerializer<User> userJackson2JsonRedisSerializer) {

		RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration
				.defaultCacheConfig()
				.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(userJackson2JsonRedisSerializer))
//				.disableCachingNullValues() // do allow null store in redis cache
				;

		RedisCacheManager redisCacheManager = RedisCacheManager.RedisCacheManagerBuilder
				.fromConnectionFactory(redisConnectionFactory)
				.cacheDefaults(redisCacheConfiguration)
				.build();

		log.info("Create RedisCacheManager with \n\tConnectionFactory: [{}], \n\tKeySerializer#Reader: [{}], \n\tKeySerializer#Writer: [{}], \n\tValueSerializer#Reader: [{}], \n\tValueSerializer#Writer: [{}].",
				redisConnectionFactory.getClass(),
				extractSerializerName(redisCacheConfiguration.getKeySerializationPair().getReader()), extractSerializerName(redisCacheConfiguration.getKeySerializationPair().getWriter()),
				extractSerializerName(redisCacheConfiguration.getValueSerializationPair().getReader()), extractSerializerName(redisCacheConfiguration.getValueSerializationPair().getWriter()));

		return redisCacheManager;
	}

	private String extractSerializerName(RedisElementReader<?> redisElementReader) {
		try {
			if (redisElementReader != null) {
				Field serializer = redisElementReader.getClass().getDeclaredField("serializer");
				serializer.setAccessible(true);
				return serializer.get(redisElementReader).getClass().getName();
			}
		}
		catch (NoSuchFieldException e) {
			log.warn("RedisElementReader: [{}] does not contains Field[serializer]", redisElementReader.getClass());
		}
		catch (IllegalAccessException e) {
			log.warn("Illegal Access Field: [{}]", e.getMessage());
		}
		return null;
	}

	private String extractSerializerName(RedisElementWriter<?> redisElementWriter) {
		try {
			if (redisElementWriter != null) {
				Field serializer = redisElementWriter.getClass().getDeclaredField("serializer");
				serializer.setAccessible(true
				);
				return serializer.get(redisElementWriter).getClass().getName();
			}
		}
		catch (NoSuchFieldException e) {
			log.warn("RedisElementReader: [{}] does not contains Field[serializer]", redisElementWriter.getClass());
		}
		catch (IllegalAccessException e) {
			log.warn("Illegal Access Field: [{}]", e.getMessage());
		}
		return null;
	}
}
