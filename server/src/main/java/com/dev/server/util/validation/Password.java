package com.dev.server.util.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = PasswordValidator.class)
@Documented
public @interface Password {

    String message() default "must contains A-Z, a-z, 0-9";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}