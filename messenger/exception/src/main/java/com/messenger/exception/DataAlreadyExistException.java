package com.messenger.exception;

/**
 * When the Details already existed in the database, an exception is thrown.
 *
 * @author venkatesh
 * @version 1.0
 * @see com.messenger.exception.CustomException
 */
public class DataAlreadyExistException extends CustomException {

    /**
     * Constructs an exception when the data already exist in the database.
     *
     * @param exception occurs when the data already existed in the database
     */
    public DataAlreadyExistException(final String exception) {
        super(exception);
    }
}
