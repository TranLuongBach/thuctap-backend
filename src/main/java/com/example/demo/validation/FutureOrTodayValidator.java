package com.example.demo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class FutureOrTodayValidator implements ConstraintValidator<FutureOrTodayDate, LocalDate> {

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {

        if (value == null) {
            return true; // cho phép null nếu không bắt buộc
        }

        return !value.isBefore(LocalDate.now());
    }
}