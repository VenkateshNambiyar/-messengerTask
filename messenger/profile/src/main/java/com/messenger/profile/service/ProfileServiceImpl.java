package com.messenger.profile.service;

import com.messenger.exception.DataAlreadyExistException;
import com.messenger.exception.UserNotFoundException;
import com.messenger.profile.dao.ProfileDAO;
import com.messenger.profile.dao.ProfileDAOImpl;
import com.messenger.profile.model.Status;
import com.messenger.profile.model.UserDetail;
import com.messenger.validation.Profile.Update;
import com.messenger.validation.Profile.Select;
import com.messenger.validation.Profile.Insert;
import com.messenger.validation.Profile.Delete;
import com.messenger.validation.UserDetailValidation;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
     * @param userDetail represent {@link UserDetail}
     * @return the id of the user profile
     */
    @Override
    public Map<String, Object> create(final UserDetail userDetail) {
        final List<String> validationMessages = UserDetailValidation.validateDetails(userDetail, Insert.class);
        final Map<String, Object> status = new LinkedHashMap<>();

        try {

            if (validationMessages.contains("valid")) {
                final Long profileId = PROFILE_DAO.create(userDetail);

                if (profileId == null) {
                    status.put("Info", Status.FAILED);
                } else {
                    status.put("Profile successfully created", PROFILE_DAO.create(userDetail));
                }
            } else {
                status.put("Info", validationMessages);
            }
        } catch (DataAlreadyExistException dataAlreadyExistException) {
            status.put("Info", "Data already exists");
        }
        return status;
    }

    /**
     * {@inheritDoc}
     *
     * @param id represent the profile id of the user profile
     * @return the particular user profile
     */
    @Override
    public Map<String, Object> get(final Long id) {
        final UserDetail userDetail = new UserDetail();

        userDetail.setId(id);
        final List<String> validationMessages = UserDetailValidation.validateDetails(userDetail, Select.class);
        final Map<String, Object> status = new LinkedHashMap<>();

        try {

            if (validationMessages.contains("valid")) {
                status.put("Profile", PROFILE_DAO.get(userDetail.getId()));
            } else {
                status.put("Info", validationMessages);
            }
        } catch (UserNotFoundException userNotFoundException) {
            status.put("Info", "User not found");
        }
        return status;
    }

    /**
     * {@inheritDoc}
     *
     * @param userDetail represent {@link UserDetail}
     * @return the update status
     */
    @Override
    public Map<String, Object> update(final UserDetail userDetail) {
        final List<String> validationMessages = UserDetailValidation.validateDetails(userDetail, Update.class);
        final Map<String, Object> status = new LinkedHashMap<>();

        try {

            if (validationMessages.contains("valid")) {

                if (PROFILE_DAO.update(userDetail)) {
                    status.put("Profile successfully updated", Status.UPDATED);
                } else {
                    status.put("Info", Status.FAILED);
                }
            } else {
                status.put("Info", validationMessages);
            }
        } catch (UserNotFoundException userNotFoundException) {
            status.put("Info", "User not found");
        }
        return status;
    }

    /**
     * {@inheritDoc}
     *
     * @param id represent the profile id of the user profile
     * @return the delete status
     */
    @Override
    public Map<String, Object> delete(final Long id) {
        final UserDetail userDetail = new UserDetail();

        userDetail.setId(id);
        final List<String> validationMessages = UserDetailValidation.validateDetails(userDetail, Delete.class);
        final Map<String, Object> status = new LinkedHashMap<>();

        try {

            if (validationMessages.contains("valid")) {

                if (PROFILE_DAO.delete(userDetail.getId())) {
                    status.put("Profile successfully deleted", Status.DELETED);
                } else {
                    status.put("Info", Status.FAILED);
                }
            } else {
                status.put("Info", validationMessages);
            }
        } catch (UserNotFoundException userNotFoundException) {
            status.put("Info", "User not found");
        }
        return status;
    }
}