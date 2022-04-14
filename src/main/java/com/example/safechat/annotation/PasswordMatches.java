package com.example.safechat.annotation;

import com.example.safechat.validator.PasswordMatchesValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Documented
public @interface PasswordMatches {
    String message() default "Invalid email";

    Class<?>[] groups() default{};

    Class<? extends Payload>[] payload() default {};
}
