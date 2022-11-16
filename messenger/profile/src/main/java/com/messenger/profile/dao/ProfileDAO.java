package com.messenger.profile.dao;

import com.messenger.profile.model.Profile;

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
     * @param profile represent {@link Profile}
     * @return the id of the user profile
     */
    Long create(final Profile profile);

    /**
     * Retrieves a particular user profile
     *
     * @param id represent the profile id of the user profile
     * @return the particular user profile
     */
    Profile get(final Long id);

    /**
     * Updates the profile
     *
     * @param profile represent {@link Profile}
     * @return the update status
     */
    boolean update(final Profile profile);

    /**
     * Retrieves the user details
     *
     * @param id represent the profile id
     * @return the user details
     */
    boolean delete(final Long id);
}