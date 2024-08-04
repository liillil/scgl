package com.naver.goods.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class JedisConfig {
	@Value("${spring.redis.host}")
	private String host;
	@Value("${spring.redis.port}")
	private Integer port;
	 @Bean
	 public JedisPoolConfig jedisPoolConfig() {
	   JedisPoolConfig poolConfig = new JedisPoolConfig();
	   poolConfig.setMaxTotal(100);
	   poolConfig.setMaxIdle(20);
	   poolConfig.setMinIdle(10);
	   poolConfig.setMaxWaitMillis(3000);
	   // 其他配置项设置
	   return poolConfig;
	 }
	
	 @Bean
	 public JedisConnectionFactory jedisConnectionFactory(JedisPoolConfig poolConfig) {
	   RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
	   config.setHostName(host);
	   config.setPort(port);
	   // 其他 Redis 配置项设置
	   JedisClientConfiguration clientConfig = JedisClientConfiguration.builder()
	       .usePooling().poolConfig(poolConfig)
	       .build();
	   return new JedisConnectionFactory(config, clientConfig);
	 }
	
	 @Bean
	 public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory connectionFactory) {
	   RedisTemplate<String, Object> template = new RedisTemplate<>();
	   template.setConnectionFactory(connectionFactory);
	   template.setKeySerializer(new StringRedisSerializer());
	   template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
	   return template;
	 }
}

