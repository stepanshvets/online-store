package com.dev.server.util.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {
    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        return password.matches(".*[^a-z].*")
                && password.matches(".*[^A-Z].*")
                && password.matches(".*\\d.");
    }
}
