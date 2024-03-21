package com.dev.server.util.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class GeneralValidator {
    private final Validator validator;

    public Set<ConstraintViolation<Object>> validate(Object objectToValidate) {
        return validator.validate(objectToValidate);
    }
}
