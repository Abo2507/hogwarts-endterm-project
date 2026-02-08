package kz.hogwarts.utils;

import kz.hogwarts.exception.InvalidInputException;

public class ValidationUtils {

    public static void validateNotNull(Object object, String fieldName) {
        if (object == null) {
            throw new InvalidInputException(fieldName + " cannot be null");
        }
    }

    public static void validateNotEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidInputException(fieldName + " cannot be empty");
        }
    }

    public static void validatePositive(Integer value, String fieldName) {
        if (value == null || value <= 0) {
            throw new InvalidInputException(fieldName + " must be positive");
        }
    }

    public static void validateRange(Integer value, int min, int max, String fieldName) {
        if (value == null || value < min || value > max) {
            throw new InvalidInputException(
                    fieldName + " must be between " + min + " and " + max);
        }
    }
}