package org.oao.eticket.common.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.oao.eticket.application.domain.model.AuthTokenId;

import java.io.IOException;

public class AuthTokenIdSerializer extends JsonSerializer<AuthTokenId> {

  @Override
  public Class<AuthTokenId> handledType() {
    return AuthTokenId.class;
  }

  @Override
  public void serialize(
      final AuthTokenId value, final JsonGenerator gen, final SerializerProvider serializers)
      throws IOException {
    if (value == null) {
      gen.writeNull();
    } else {
      gen.writeString(value.toString());
    }
  }
}
