package com.messenger.profile.service;

import com.messenger.exception.DataAlreadyExistException;
import com.messenger.exception.ProfileNotFoundException;
import com.messenger.exception.UserNotFoundException;
import com.messenger.profile.dao.ProfileDAO;
import com.messenger.profile.dao.ProfileDAOImpl;
import com.messenger.profile.model.Status;
import com.messenger.profile.model.Profile;

/**
 * Implements the profile service.
 *
 * @author venkatesh
 * @version 1.0
 */
public class ProfileServiceImpl implements ProfileService {

    private static final ProfileDAO PROFILE_DAO = new ProfileDAOImpl();

    /**
     * {@inheritDoc}
     *
     * @param profile represent {@link Profile}
     * @return the id of the user profile
     */
    @Override
    public Long create(final Profile profile) {
        final Long profileId = PROFILE_DAO.create(profile);

        if (profileId == null) {
            throw new DataAlreadyExistException("Data already exists");
        }
        return profileId;
    }

    /**
     * {@inheritDoc}
     *
     * @param id represent the profile id of the user profile
     * @return the particular user profile
     */
    @Override
    public Profile get(final Long id) {
        final Profile profile = PROFILE_DAO.get(id);

        if (profile == null) {
            throw new ProfileNotFoundException("Profile not found");
        }
        return profile;
    }

    /**
     * {@inheritDoc}
     *
     * @param profile represent {@link Profile}
     * @return the update status
     */
    @Override
    public Enum<Status> update(final Profile profile) {
        return PROFILE_DAO.update(profile) ? Status.UPDATED : Status.FAILED;
    }

    /**
     * {@inheritDoc}
     *
     * @param profile represent {@link Profile}
     * @return the update status
     */
    @Override
    public Enum<Status> updateParticularDetail(final Profile profile) {
        if (!PROFILE_DAO.update(profile)) {
            throw new DataAlreadyExistException("Data already exists");
        }
        return Status.UPDATED;
    }

    /**
     * {@inheritDoc}
     *
     * @param id represent the profile id of the user profile
     * @return the delete status
     */
    @Override
    public Enum<Status> delete(final Long id) {
        if (!PROFILE_DAO.delete(id)) {
            throw new UserNotFoundException("user not found");
        }
        return Status.DELETED;
    }
}