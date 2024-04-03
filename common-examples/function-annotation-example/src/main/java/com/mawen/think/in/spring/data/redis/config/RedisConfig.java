package com.mawen.think.in.spring.data.redis.config;

import com.mawen.think.in.spring.data.redis.pojo.User;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Config Redis Common Config
 *
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/2
 */
@Slf4j
@Configuration
public class RedisConfig {

	@Bean
	public Jackson2JsonRedisSerializer<User> userJackson2JsonRedisSerializer() {
		return new Jackson2JsonRedisSerializer<>(User.class);
	}

	@Bean
	public RedisTemplate<String, User> redisTemplate(RedisConnectionFactory redisConnectionFactory, Jackson2JsonRedisSerializer<User> userJackson2JsonRedisSerializer) {
		RedisTemplate<String, User> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(userJackson2JsonRedisSerializer);
		log.info("Create RedisTemplate<String, User> with \n\tConnectionFactory: [{}], \n\tKeySerializer: [{}], \n\tValueSerializer: [{}].",
				redisConnectionFactory.getClass(), redisTemplate.getKeySerializer().getClass(), redisTemplate.getValueSerializer().getClass());
		return redisTemplate;
	}
}
