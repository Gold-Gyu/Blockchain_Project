package org.oao.eticket.common.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.oao.eticket.application.domain.model.AuthTokenId;

import java.io.IOException;

public class AuthTokenDeserializer extends JsonDeserializer<AuthTokenId> {
  @Override
  public AuthTokenId deserialize(final JsonParser jsonParser, final DeserializationContext context)
      throws IOException {
    return AuthTokenId.of(jsonParser.readValueAs(String.class));
  }
}
