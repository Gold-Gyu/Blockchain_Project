package org.oao.eticket.config;

import org.oao.eticket.application.domain.model.PerformanceScheduleSeatTable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

  private RedisTemplate<String, String> createStringRedisTemplate(
      final RedisConnectionFactory connectionFactory) {
    final var redisTemplate = new RedisTemplate<String, String>();
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(new StringRedisSerializer());
    redisTemplate.setHashKeySerializer(new StringRedisSerializer());
    redisTemplate.setHashValueSerializer(new StringRedisSerializer());
    redisTemplate.setConnectionFactory(connectionFactory);
    return redisTemplate;
  }

  private RedisTemplate<String, Object> createObjectRedisTemplate(
      final RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(connectionFactory);
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(
        new Jackson2JsonRedisSerializer<>(PerformanceScheduleSeatTable.class)); // JSON 직렬화 설정
    redisTemplate.setHashKeySerializer(new StringRedisSerializer());
    redisTemplate.setHashValueSerializer(
        new Jackson2JsonRedisSerializer<>(PerformanceScheduleSeatTable.class)); // JSON 직렬화 설정
    return redisTemplate;
  }

  @Bean("eticketAuthRedisConnectionFactory")
  RedisConnectionFactory eticketAuthRedisConnectionFactory(
      @Value("${eticket.redis.auth.host}") final String host,
      @Value("${eticket.redis.auth.port}") final int port,
      @Value("${eticket.redis.auth.database}") final String database) {
    final var connectionFactory = new LettuceConnectionFactory(host, port);
    connectionFactory.setDatabase(Integer.parseInt(database));
    return connectionFactory;
  }

  @Bean("ticketingRedisConnectionFactory")
  RedisConnectionFactory ticketingRedisConnectionFactory(
      @Value("${eticket.redis.ticketing.host}") final String host,
      @Value("${eticket.redis.ticketing.port}") final int port,
      @Value("${eticket.redis.ticketing.database}") final String database) {
    final var connectionFactory = new LettuceConnectionFactory(host, port);
    connectionFactory.setDatabase(Integer.parseInt(database));
    return connectionFactory;
  }

  @Bean("eticketReservationRedisConnectionFactory")
  RedisConnectionFactory eticketReservationRedisConnectionFactory(
      @Value("${eticket.redis.reservation.host}") final String host,
      @Value("${eticket.redis.reservation.port}") final int port,
      @Value("${eticket.redis.reservation.database}") final String database) {
    final var connectionFactory = new LettuceConnectionFactory(host, port);
    connectionFactory.setDatabase(Integer.parseInt(database));
    return connectionFactory;
  }

  @Bean("eticketAuthRedisTemplate")
  RedisTemplate<String, String> eticketAuthRedisTemplate(
      final RedisConnectionFactory eticketAuthRedisConnectionFactory) {
    return createStringRedisTemplate(eticketAuthRedisConnectionFactory);
  }

  @Bean("ticketingRedisTemplate")
  public RedisTemplate<String, Integer> ticketingRedisTemplate(
      final RedisConnectionFactory ticketingRedisConnectionFactory) {
    RedisTemplate<String, Integer> ticketingRedisTemplate = new RedisTemplate<>();
    ticketingRedisTemplate.setConnectionFactory(ticketingRedisConnectionFactory);
    ticketingRedisTemplate.setKeySerializer(new StringRedisSerializer());
    ticketingRedisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Integer.class));
    return ticketingRedisTemplate;
  }

  @Bean("eticketReservationRedisTemplate")
  RedisTemplate<String, Object> eticketReservationRedisTemplate(
      final RedisConnectionFactory eticketReservationRedisConnectionFactory) {
    return createObjectRedisTemplate(eticketReservationRedisConnectionFactory);
  }
}
