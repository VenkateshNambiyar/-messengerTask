package com.messenger.exception;

/**
 * When it is unable to connect to the database, an exception is thrown.
 *
 * @author venkatesh
 * @version 1.0
 * @see com.messenger.exception.CustomException
 */
public class ConnectionNotFoundException extends CustomException {

    /**
     * Constructs an exception, when connection not found in the database.
     *
     * @param exception occurs when it is failed to get the connection
     */
    public ConnectionNotFoundException(final String exception) {
        super(exception);
    }
}