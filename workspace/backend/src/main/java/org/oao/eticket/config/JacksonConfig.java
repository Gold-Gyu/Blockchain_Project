package org.oao.eticket.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.oao.eticket.application.domain.model.AuthTokenId;
import org.oao.eticket.application.domain.model.User;
import org.oao.eticket.common.jackson.deserializer.AuthTokenDeserializer;
import org.oao.eticket.common.jackson.deserializer.UserIdDeserializer;
import org.oao.eticket.common.jackson.serializer.AuthTokenIdSerializer;
import org.oao.eticket.common.jackson.serializer.UserIdSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {

  private SimpleModule domainEntitySerializers() {
    return new SimpleModule()
        .addSerializer(new UserIdSerializer())
        .addSerializer(new AuthTokenIdSerializer());
  }

  private SimpleModule domainEntityDeserializers() {
    return new SimpleModule()
        .addDeserializer(User.UserID.class, new UserIdDeserializer())
        .addDeserializer(AuthTokenId.class, new AuthTokenDeserializer());
  }

  @Bean
  ObjectMapper objectMapper(final Jackson2ObjectMapperBuilder builder) {
    final var objectMapper = builder.createXmlMapper(false).build();

    return objectMapper
        .registerModule(new JavaTimeModule())
        .registerModule(domainEntitySerializers())
        .registerModule(domainEntityDeserializers());
  }
}
