package com.leo.boot.cache.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
public class RedisConfig {

	/**
	 * 自定义RedisTemplate
	 * key使用StringRedisSerializer
	 * value使用json序列化，默认为JDK序列化JdkSerializationRedisSerializer
	 */
	@Bean
	public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
		ObjectMapper om = new ObjectMapper();

		Jackson2JsonRedisSerializer<?> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
		jackson2JsonRedisSerializer.setObjectMapper(om);

		RedisTemplate<Object, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		template.setDefaultSerializer(jackson2JsonRedisSerializer);
		return template;
	}

}