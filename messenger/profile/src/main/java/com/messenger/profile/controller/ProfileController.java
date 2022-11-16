package com.messenger.profile.controller;

import com.messenger.exception.DataAlreadyExistException;
import com.messenger.exception.UserNotFoundException;
import com.messenger.profile.model.Status;
import com.messenger.profile.model.Profile;
import com.messenger.profile.service.ProfileService;
import com.messenger.profile.service.ProfileServiceImpl;
import com.messenger.validation.ProfileValidation.PartialUpdate;
import com.messenger.validation.ProfileValidation.Update;
import com.messenger.validation.ProfileValidation.Select;
import com.messenger.validation.ProfileValidation.Insert;
import com.messenger.validation.ProfileValidation.Delete;

import static com.messenger.validation.Validator.getInstance;

import org.json.simple.JSONObject;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * Controls the payload
 *
 * @author venkatesh
 * @version 1.0
 */
public class ProfileController {

    private static final ProfileService PROFILE_SERVICE = new ProfileServiceImpl();

    /**
     * Create a new user profile
     *
     * @param profile represent {@link Profile}
     * @return the id of the user profile
     */
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public JSONObject create(final Profile profile) {
        final JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("Profile", getInstance().checkValidation(profile, Insert.class)
                    ? PROFILE_SERVICE.create(profile) : getInstance().getErrorMessage());
        } catch (DataAlreadyExistException dataAlreadyExistException) {
            jsonObject.put(Status.INFO.toString(), "Data already exists");
        }
        return jsonObject;
    }

    /**
     * Retrieves a particular user profile
     *
     * @param id represent the profile id of the user profile
     * @return the particular user profile
     */
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public JSONObject get(@QueryParam("id") final Long id) {
        final JSONObject jsonObject = new JSONObject();
        final Profile profile = new Profile();

        profile.setId(id);

        try {
            jsonObject.put("Profile", getInstance().checkValidation(profile, Select.class)
                    ? PROFILE_SERVICE.get(profile.getId()) : getInstance().getErrorMessage());
        } catch (UserNotFoundException userNotFoundException) {
            jsonObject.put(Status.INFO.toString(), "User not found");
        }
        return jsonObject;
    }

    /**
     * Updates the profile
     *
     * @param profile represent {@link Profile}
     * @return the update status
     */
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    @PUT
    public JSONObject update(final Profile profile) {
        final JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("Profile", getInstance().checkValidation(profile, Update.class)
                    ? PROFILE_SERVICE.update(profile) : getInstance().getErrorMessage());
        } catch (UserNotFoundException userNotFoundException) {
            jsonObject.put(Status.INFO.toString(), "User not found");
        }
        return jsonObject;
    }

    /**
     * Updates the particular detail of the user
     *
     * @param profile represent {@link Profile}
     * @return the update status
     */
    @Path("/partial/update")
    @Produces(MediaType.APPLICATION_JSON)
    @PUT
    public JSONObject updateParticularDetail(final Profile profile) {
        final JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("Profile", getInstance().checkValidation(profile, PartialUpdate.class)
                    ? PROFILE_SERVICE.updateParticularDetail(profile) : getInstance().getErrorMessage());
        } catch (DataAlreadyExistException dataAlreadyExistException) {
            jsonObject.put(Status.INFO.toString(), "Data already exists");
        }
        return jsonObject;
    }

    /**
     * Deletes the profile
     *
     * @param id represent the profile id of the user
     * @return the delete status
     */
    @Path("/delete")
    @Produces(MediaType.APPLICATION_JSON)
    @DELETE
    public JSONObject delete(@QueryParam("id") final Long id) {
        final JSONObject jsonObject = new JSONObject();
        final Profile profile = new Profile();

        profile.setId(id);

        try {
            jsonObject.put("Profile", getInstance().checkValidation(profile, Delete.class)
                    ? PROFILE_SERVICE.delete(profile.getId()) : getInstance().getErrorMessage());
        } catch (UserNotFoundException userNotFoundException) {
            jsonObject.put(Status.INFO.toString(), "User not found");
        }
        return jsonObject;
    }
}