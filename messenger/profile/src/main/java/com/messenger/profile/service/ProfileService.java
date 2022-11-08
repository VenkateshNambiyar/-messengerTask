package com.messenger.profile.service;

import com.messenger.profile.model.UserDetail;

import java.util.Map;

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
     * @param userDetail represent {@link UserDetail}
     * @return the id of the user profile
     */
    Map<String, Object> create(final UserDetail userDetail);

    /**
     * Retrieves a particular user profile
     *
     * @param id represent the profile id of the user
     * @return the particular user profile
     */
    Map<String, Object> get(final Long id);

    /**
     * Updates a profile
     *
     * @param userDetail represent {@link UserDetail}
     * @return the update status
     */
    Map<String, Object> update(final UserDetail userDetail);

    /**
     * Deletes a profile
     *
     * @param id represent the profile id of the user
     * @return the delete status
     */
    Map<String, Object> delete(final Long id);
}