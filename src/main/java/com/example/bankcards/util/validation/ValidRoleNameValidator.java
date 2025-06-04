package com.example.bankcards.util.validation;

import com.example.bankcards.entity.RoleName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValidRoleNameValidator implements ConstraintValidator<ValidRoleName, String> {

    public static final Set<String> roleNames = Stream.of(RoleName.values())
            .map(RoleName::name)
            .collect(Collectors.toSet());

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s == null || roleNames.contains(s);
    }
}
