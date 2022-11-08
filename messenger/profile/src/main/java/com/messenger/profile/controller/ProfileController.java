package com.messenger.profile.controller;

import com.messenger.profile.model.UserDetail;
import com.messenger.profile.service.ProfileService;
import com.messenger.profile.service.ProfileServiceImpl;
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
@Path("/")
public class ProfileController {

    private static final ProfileService PROFILE_SERVICE = new ProfileServiceImpl();

    /**
     * Create a new user profile
     *
     * @param userDetail represent {@link UserDetail}
     * @return the id of the user profile
     */
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public JSONObject create(final UserDetail userDetail) {
        return new JSONObject(PROFILE_SERVICE.create(userDetail));
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
        return new JSONObject(PROFILE_SERVICE.get(id));
    }

    /**
     * Updates the profile
     *
     * @param userDetail represent {@link UserDetail}
     * @return the update status
     */
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    @PUT
    public JSONObject update(final UserDetail userDetail) {
        return new JSONObject(PROFILE_SERVICE.update(userDetail));
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
    public JSONObject delete(@QueryParam("id") Long id) {
        return new JSONObject(PROFILE_SERVICE.delete(id));
    }
}