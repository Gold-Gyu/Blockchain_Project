package org.oao.eticket.common.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@RestController
@Documented
public @interface WebAdapter {

  @AliasFor(annotation = Component.class)
  String value() default "";
}
