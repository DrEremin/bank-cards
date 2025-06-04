package com.example.bankcards.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidRoleNameValidator.class)
public @interface ValidRoleName {

    String message() default "Недопустимое имя роли пользователя";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
