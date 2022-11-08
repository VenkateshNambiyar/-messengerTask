package com.messenger.conversation.service;

import com.messenger.conversation.model.Conversation;

import java.util.Map;

/**
 * Provides the service that messenger has available for conversation
 *
 * @author venkatesh
 * @version 1.0
 */
public interface ConversationService {

    /**
     * Creates the new conversation
     *
     * @param conversation represent {@link Conversation}
     * @return the conversation id
     */
    Map<String, Object> createConversation(final Conversation conversation);

    /**
     * Sends the group of messages into existing conversation
     *
     * @param conversation represent {@link Conversation}
     * @return the message status
     */
    Map<String, Object> existingConversation(final Conversation conversation);

    /**
     * Retrieves a particular user conversation
     *
     * @param conversationId represent the conversation id of the user
     * @return the particular user conversation message
     */
    Map<String, Object> getMessages(final Long conversationId);

    /**
     * Retrieves the all conversation of the user
     *
     * @param profileId represent the profile id of the user
     * @return return the conversation
     */
    Map<String, Object> getAllConversationIds(final Long profileId);

    /**
     * Deletes the user conversation
     *
     * @param conversationId represent the conversation of the user
     * @return the delete status
     */
    Map<String, Object> deleteConversation(final Long conversationId);
}