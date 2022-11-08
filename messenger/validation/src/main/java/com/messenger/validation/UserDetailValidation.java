package com.messenger.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.HibernateValidator;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Validates the user details.
 *
 * @author Venkatesh N
 * @version 1.0
 */
public class UserDetailValidation {

    /**
     * Validating the details of the user.
     *
     * @param object represents the UserDetails
     * @param validation represent the group by class
     * @return the validation message.
     */
    public static List<String> validateDetails(final Object object, final Class<?> validation) {
        final List<String> validationMessages = new LinkedList<>();

        try (ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class).configure()
                .buildValidatorFactory()) {
            final Validator validator = validatorFactory.getValidator();
            final Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, validation);

            if (constraintViolations.size() > 0) {

                for (final ConstraintViolation<Object> violation : constraintViolations) {
                    validationMessages.add(violation.getMessage());
                }
            } else {
                validationMessages.add("valid");
            }
        }
        return validationMessages;
    }
}