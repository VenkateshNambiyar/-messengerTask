package com.messenger.exception;

/**
 * Custom exception is a user-defined exception that occurs when we create our own exception.
 *
 * @author venkatesh
 * @version 1.0
 * @see java.lang.RuntimeException
 */
public class CustomException extends RuntimeException {

    /**
     * Constructs a custom exception with an exception.
     *
     * @param exception a message occurs when an exception is thrown
     */
    public CustomException(final String exception) {
        super(exception);
    }
}