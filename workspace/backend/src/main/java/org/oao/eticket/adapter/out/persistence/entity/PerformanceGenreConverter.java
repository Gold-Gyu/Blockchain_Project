package org.oao.eticket.adapter.out.persistence.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.oao.eticket.application.domain.model.PerformanceGenre;

@Converter
class PerformanceGenreConverter implements AttributeConverter<PerformanceGenre, String> {

  @Override
  public String convertToDatabaseColumn(final PerformanceGenre attr) {
    return attr.getGenre();
  }

  @Override
  public PerformanceGenre convertToEntityAttribute(final String dbData) {
    return PerformanceGenre.of(dbData);
  }
}
