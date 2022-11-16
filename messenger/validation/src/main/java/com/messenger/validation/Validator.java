package com.messenger.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.HibernateValidator;

import java.util.Set;
import java.util.StringJoiner;

/**
 * Validates the user details.
 *
 * @author Venkatesh N
 * @version 1.0
 */
public class Validator {

    private static Validator validator;
    private Set<ConstraintViolation<Object>> constraintViolations;

    /**
     * Create a private Constructor
     */
    private Validator() {
    }

    /**
     * Returns {@link Validator} instance.
     *
     * @return the validation instance
     */
    public static Validator getInstance() {

        if (validator == null) {
            validator = new Validator();
        }
        return validator;
    }

    /**
     * Validates the profile details
     *
     * @param object     represent the user object
     * @param validation represent the validation group classes
     * @return the constraints status
     */
    public boolean checkValidation(final Object object, final Class<?> validation) {
        try (ValidatorFactory validatorFactory = jakarta.validation.Validation.byProvider(HibernateValidator.class)
                .configure().buildValidatorFactory()) {
            final jakarta.validation.Validator validator = validatorFactory.getValidator();

            this.constraintViolations = validator.validate(object, validation);

            return constraintViolations.size() == 0;
        }
    }

    /**
     * Gets the error message
     *
     * @return the error message
     */
    public String getErrorMessage() {
        final StringJoiner errorMessage = new StringJoiner(" , ");

        for (final ConstraintViolation<Object> violation : constraintViolations) {
            errorMessage.add(violation.getMessage());
        }
        return errorMessage.toString();
    }
}
