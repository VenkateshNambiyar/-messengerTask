package com.messenger.exception;

/**
 * When the user not found in the database, an exception is thrown.
 *
 * @author venkatesh
 * @version 1.0
 * @see com.messenger.exception.CustomException
 */
public class ProfileNotFoundException extends CustomException {

    /**
     * Constructs an exception, when the profile not found in the database.
     *
     * @param exception occurs when the profile not found in the database
     */
    public ProfileNotFoundException(final String exception) {
        super(exception);
    }
}
