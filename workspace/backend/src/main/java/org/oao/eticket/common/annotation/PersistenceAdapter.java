package org.oao.eticket.common.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
@Documented
public @interface PersistenceAdapter {

  @AliasFor(annotation = Component.class)
  String value() default "";
}
