package com.messenger.conversation.controller;

import com.messenger.conversation.model.Conversation;
import com.messenger.conversation.model.Message;
import com.messenger.conversation.service.ConversationService;
import com.messenger.conversation.service.ConversationServiceImpl;
import com.messenger.exception.ConversationNotFoundException;
import com.messenger.exception.CustomException;
import com.messenger.exception.UserNotFoundException;
import com.messenger.profile.model.Status;
import com.messenger.validation.ConversationValidation.PartialDelete;
import com.messenger.validation.ConversationValidation.Insert;
import com.messenger.validation.ConversationValidation.Select;
import com.messenger.validation.ConversationValidation.Delete;
import com.messenger.validation.ProfileValidation;

import static com.messenger.validation.Validator.getInstance;

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
     * Sends the message in the conversation
     *
     * @param message represent the  {@link Message}
     * @return the conversation id
     */
    @Path("/send/message")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public JSONObject sendMessage(final Message message) {
        final JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("Message", getInstance().checkValidation(message, Insert.class)
                    ? CONVERSATION_SERVICE.sendMessage(message) : getInstance().getErrorMessage());
        } catch (UserNotFoundException userNotFoundException) {
            jsonObject.put(Status.INFO.toString(), "User not found");
        }
        return jsonObject;
    }

    /**
     * Retrieves a particular user conversation
     *
     * @param profileId      represent the profile id of the user
     * @param conversationId represent the conversation of the user
     * @return the particular user conversation messages
     */
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public JSONObject getConversation(@QueryParam("userId") final Long profileId,
                                      @QueryParam("conversationId") final Long conversationId) {
        final JSONObject jsonObject = new JSONObject();
        final Conversation conversation = new Conversation();

        conversation.setUserId(profileId);
        conversation.setConversationId(conversationId);

        try {
            jsonObject.put("Conversation", getInstance().checkValidation(conversation, Select.class)
                    ? CONVERSATION_SERVICE.getConversation(conversation.getUserId(), conversation.getConversationId())
                    : getInstance().getErrorMessage());
        } catch (ConversationNotFoundException conversationNotFoundException) {
            jsonObject.put(Status.INFO.toString(), "Conversation not found");
        }
        return jsonObject;
    }

    /**
     * Retrieves the all conversation of the user
     *
     * @param profileId represent the profile of the user
     * @return the user conversation ids
     */
    @Path("/get/all/conversation")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public JSONObject getAllConversations(@QueryParam("userId") final Long profileId) {
        final JSONObject jsonObject = new JSONObject();
        final Conversation conversation = new Conversation();

        conversation.setUserId(profileId);

        try {
            jsonObject.put("Conversations", getInstance().checkValidation(conversation, ProfileValidation.Select.class)
                    ? CONVERSATION_SERVICE.getAllConversations(conversation.getUserId())
                    : getInstance().getErrorMessage());
        } catch (CustomException customException) {
            jsonObject.put(Status.INFO.toString(), "Profile not found");
        }
        return jsonObject;
    }

    /**
     * Deletes the user conversation
     *
     * @param userId         represent the user profile
     * @param conversationId represent the conversation of the user
     * @return the delete status
     */
    @Path("/delete/conversation")
    @Produces(MediaType.APPLICATION_JSON)
    @DELETE
    public JSONObject deleteConversation(@QueryParam("userId") final Long userId,
                                         @QueryParam("conversationId") final Long conversationId) {
        final JSONObject jsonObject = new JSONObject();
        final Conversation conversation = new Conversation();

        conversation.setUserId(userId);
        conversation.setConversationId(conversationId);

        try {
            jsonObject.put("Conversation", getInstance().checkValidation(conversation, Delete.class)
                    ? CONVERSATION_SERVICE.deleteConversation(conversation.getUserId(), conversation.getConversationId())
                    : getInstance().getErrorMessage());
        } catch (ConversationNotFoundException conversationNotFoundException) {
            jsonObject.put(Status.INFO.toString(), "Conversation not found");
        }
        return jsonObject;
    }

    /**
     * Deletes the particular message in the conversation
     *
     * @param conversationId represent the conversation of the user
     * @param messageId      represent the message id in the conversation
     * @return the delete status
     */
    @Path("/delete/message")
    @Produces(MediaType.APPLICATION_JSON)
    @DELETE
    public JSONObject deleteMessage(@QueryParam("conversationId") final Long conversationId,
                                    @QueryParam("messageId") final Long messageId) {
        final JSONObject jsonObject = new JSONObject();
        final Conversation conversation = new Conversation();
        final Message message = new Message();

        conversation.setConversationId(conversationId);
        message.setMessageId(messageId);
        conversation.setMessage(message);

        try {
            jsonObject.put("Conversation", getInstance().checkValidation(conversation, PartialDelete.class)
                    ? CONVERSATION_SERVICE.deleteMessage(conversation.getConversationId(), messageId)
                    : getInstance().getErrorMessage());
        } catch (ConversationNotFoundException conversationNotFoundException) {
            jsonObject.put(Status.INFO.toString(), "Conversation not found");
        }
        return jsonObject;
    }
}