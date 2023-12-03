package org.oao.eticket.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

  @Value("${eticket.redis.waiting.host}")
  private String host;

  @Value("${eticket.redis.waiting.port}")
  private int port;

  @Value("${eticket.redis.waiting.database}")
  private String waiting;

  @Value("${eticket.redis.ticketing.database}")
  private String ticketing;

  @Bean("redisConnectionFactory3")
  public RedisConnectionFactory redisConnectionFactory3() {
    final var connectionFactory = new LettuceConnectionFactory(host, port);
    connectionFactory.setDatabase(Integer.parseInt(waiting));
    return connectionFactory;
  }

  @Bean("redisConnectionFactory4")
  public RedisConnectionFactory redisConnectionFactory4() {
    final var connectionFactory = new LettuceConnectionFactory(host, port);
    connectionFactory.setDatabase(Integer.parseInt(ticketing));
    return connectionFactory;
  }

  @Bean({"redisTemplate", "waitingStorage"})
  public RedisTemplate<String, Integer> waitingStorage() {
    RedisTemplate<String, Integer> waitingStorage = new RedisTemplate<>();
    waitingStorage.setConnectionFactory(redisConnectionFactory3());
    waitingStorage.setKeySerializer(new StringRedisSerializer());
    waitingStorage.setValueSerializer(new Jackson2JsonRedisSerializer<>(Integer.class));

    return waitingStorage;
  }

  @Bean(name = "ticketingStorage")
  public RedisTemplate<String, Integer> ticketingStorage() {
    RedisTemplate<String, Integer> ticketingStorage = new RedisTemplate<>();
    ticketingStorage.setConnectionFactory(redisConnectionFactory4());
    ticketingStorage.setKeySerializer(new StringRedisSerializer());
    ticketingStorage.setValueSerializer(new Jackson2JsonRedisSerializer<>(Integer.class));

    return ticketingStorage;
  }
}
