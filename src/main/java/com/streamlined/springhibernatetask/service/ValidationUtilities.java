package com.streamlined.springhibernatetask.service;

import java.util.Set;
import java.util.stream.Collectors;

import com.streamlined.springhibernatetask.exception.InvalidEntityDataException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

@UtilityClass
@Log4j2
public class ValidationUtilities {

    public void checkIfValid(Validator validator, Object entity) {
        Set<ConstraintViolation<Object>> violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            String violationDescription = getViolations(violations);
            String entityType = entity.getClass().getSimpleName();
            LOGGER.error("Incorrect data for type {}: {}", entityType, violationDescription);
            throw new InvalidEntityDataException(
                    "Incorrect data for type %s: %s".formatted(entityType, violationDescription));
        }
    }

    private <T> String getViolations(Set<ConstraintViolation<T>> violations) {
        return violations.stream().map(ValidationUtilities::formatViolation).collect(Collectors.joining(",", "[", "]"));
    }

    private <T> String formatViolation(ConstraintViolation<T> violation) {
        return "Error %s: property '%s' has invalid value '%s'".formatted(violation.getMessage(),
                violation.getPropertyPath(), violation.getInvalidValue());
    }

}
