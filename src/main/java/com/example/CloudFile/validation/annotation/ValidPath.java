package com.example.CloudFile.validation.annotation;

import com.example.CloudFile.validation.validator.PathConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PathConstraintValidator.class)
public @interface ValidPath {
        String message() default "Невалидный путь";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
}
