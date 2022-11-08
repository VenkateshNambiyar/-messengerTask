package com.messenger.conversation.controller;

import com.messenger.conversation.model.Conversation;
import com.messenger.conversation.service.ConversationService;
import com.messenger.conversation.service.ConversationServiceImpl;
import org.json.simple.JSONObject;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
public class ConversationController {

    private static final ConversationService CONVERSATION_SERVICE = new ConversationServiceImpl();

    /**
     * Creates the user conversation
     *
     * @param conversation represent {@link Conversation}
     * @return the conversation id
     */
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public JSONObject createConversation(final Conversation conversation) {
        return new JSONObject(CONVERSATION_SERVICE.createConversation(conversation));
    }

    /**
     * Sends the message to the existing conversation
     *
     * @param conversation represent {@link Conversation}
     * @return the conversation messages status
     */
    @Path("/existing/conversation")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public JSONObject existingConversation(final Conversation conversation) {
        return new JSONObject(CONVERSATION_SERVICE.existingConversation(conversation));
    }

    /**
     * Retrieves a particular user messages
     *
     * @param id represent the conversation of the user
     * @return the particular user conversation
     */
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public JSONObject getMessages(@QueryParam("id") final Long id) {
        return new JSONObject(CONVERSATION_SERVICE.getMessages(id));
    }

    /**
     * Retrieves the all conversation of the user
     *
     * @param id represent the conversation of the user
     * @return the user conversation
     */
    @Path("/get/all/conversation")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public JSONObject getAll(@QueryParam("id") final Long id) {
        return new JSONObject(CONVERSATION_SERVICE.getAllConversationIds(id));
    }

    /**
     * Deletes the user conversation
     *
     * @param id represent the conversation of the user
     * @return the delete status
     */
    @Path("/delete/conversation")
    @Produces(MediaType.APPLICATION_JSON)
    @DELETE
    public JSONObject delete(@QueryParam("id") Long id) {
        return new JSONObject(CONVERSATION_SERVICE.deleteConversation(id));
    }
}