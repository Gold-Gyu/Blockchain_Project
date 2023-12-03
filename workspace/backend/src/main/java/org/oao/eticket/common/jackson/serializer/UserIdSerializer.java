package org.oao.eticket.common.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.oao.eticket.application.domain.model.User;

import java.io.IOException;

public class UserIdSerializer extends JsonSerializer<User.UserID> {

  @Override
  public void serialize(
          final User.UserID value, final JsonGenerator gen, final SerializerProvider serializers)
      throws IOException {

    if (value == null) {
      gen.writeNull();
    } else {
      gen.writeString(value.toString());
    }
  }

  @Override
  public Class<User.UserID> handledType() {
    return User.UserID.class;
  }
}
