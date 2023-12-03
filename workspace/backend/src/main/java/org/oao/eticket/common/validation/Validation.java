package org.oao.eticket.common.validation;

import static jakarta.validation.Validation.*;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

public class Validation {

  private static final Validator validator = buildDefaultValidatorFactory().getValidator();

  public static <T> void validate(T subject) {
    final var violations = validator.validate(subject);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }
}
