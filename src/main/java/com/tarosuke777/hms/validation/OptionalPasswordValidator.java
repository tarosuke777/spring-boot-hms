package com.tarosuke777.hms.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OptionalPasswordValidator implements ConstraintValidator<OptionalPassword, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }

        // 入力がある場合のみ、文字数とパターンをチェック
        boolean isValidLength = value.length() >= 4 && value.length() <= 20;
        boolean isValidPattern = value.matches("^[a-zA-Z0-9]+$");

        return isValidLength && isValidPattern;
    }
}
