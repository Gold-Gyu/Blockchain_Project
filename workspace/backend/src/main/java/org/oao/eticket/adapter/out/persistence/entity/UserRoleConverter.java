package org.oao.eticket.adapter.out.persistence.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.oao.eticket.application.domain.model.UserRole;

@Converter
class UserRoleConverter implements AttributeConverter<UserRole, String> {

  @Override
  public String convertToDatabaseColumn(final UserRole attr) {
    return attr.getRoleName();
  }

  @Override
  public UserRole convertToEntityAttribute(final String attr) {
    return UserRole.of(attr);
  }
}
