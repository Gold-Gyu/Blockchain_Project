package org.oao.eticket.config;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

public class AuthRedisContainer implements BeforeAllCallback {

  private static final String REDIS_IMAGE = "redis:7.2";
  private static final int EXPOSED_PORT = 6379;
  private static final int MAPPED_PORT = 60001;

  private final GenericContainer<?> container =
      new GenericContainer<>(DockerImageName.parse(REDIS_IMAGE));

  @Override
  public void beforeAll(final ExtensionContext context) {
    container.setPortBindings(List.of(MAPPED_PORT + ":" + EXPOSED_PORT));
    container.start();
    System.setProperty("eticket.redis.auth.host", container.getHost());
    System.setProperty("eticket.redis.auth.port", Integer.toString(MAPPED_PORT));
    System.setProperty("eticket.redis.auth.database", "1");
  }
}
