package com.messenger.profile.dao;

import com.messenger.profile.model.UserDetail;

/**
 * Provides the dao service for messenger profile
 *
 * @author venkatesh
 * @version 1.0
 */
public interface ProfileDAO {

    /**
     * Create a new user profile
     *
     * @param userDetail represent {@link UserDetail}
     * @return the id of the user profile
     */
    Long create(final UserDetail userDetail);

    /**
     * Retrieves a particular user profile
     *
     * @param id represent the profile id of the user profile
     * @return the particular user profile
     */
    UserDetail get(final Long id);

    /**
     * Updates the profile
     *
     * @param userDetail represent {@link UserDetail}
     * @return the update status
     */
    boolean update(final UserDetail userDetail);

    /**
     * Retrieves the user details
     *
     * @param id represent the profile id
     * @return the user details
     */
    boolean delete(final Long id);
}