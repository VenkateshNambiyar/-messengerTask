package com.messenger.profile.service;

import com.messenger.profile.model.Status;
import com.messenger.profile.model.Profile;

/**
 * Provides the service for messenger profile
 *
 * @author venkatesh
 * @version 1.0
 */
public interface ProfileService {

    /**
     * Creates a new user profile
     *
     * @param userDetail represent {@link Profile}
     * @return the id of the user profile
     */
    Long create(final Profile userDetail);

    /**
     * Retrieves a particular user profile
     *
     * @param id represent the profile id of the user
     * @return the particular user profile
     */
    Profile get(final Long id);

    /**
     * Updates a profile
     *
     * @param profile represent {@link Profile}
     * @return the update status
     */
    Enum<Status> update(final Profile profile);

    /**
     * Updates a particular profile details
     *
     * @param profile represent {@link Profile}
     * @return the update status
     */
    Enum<Status> updateParticularDetail(final Profile profile);

    /**
     * Deletes a profile
     *
     * @param id represent the profile id of the user
     * @return the delete status
     */
    Enum<Status> delete(final Long id);
}