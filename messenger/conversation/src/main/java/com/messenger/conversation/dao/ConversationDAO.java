package com.messenger.conversation.dao;

import com.messenger.conversation.model.Message;

import java.util.Collection;

/**
 * Provides the dao service for messenger conversation
 *
 * @author venkatesh
 * @version 1.0
 */
public interface ConversationDAO {

    /**
     * Sends the user messages
     *
     * @param conversationId represent the conversation id of the user
     * @param messages       represent the group of messages
     * @return the status of the messages
     */
    boolean sendMessages(final Long conversationId, final Collection<Message> messages);

    /**
     * Retrieves a conversation id
     *
     * @param senderId   represent the sender details
     * @param receiverId represent the receiver details
     * @return the conversation id
     */
    Long getConversationId(final Long senderId, final Long receiverId);

    /**
     * Creates a conversation and retrieve the conversation id
     *
     * @param senderId   represent the sender details
     * @param receiverId represent the receiver details
     * @return the conversation id
     */
    Long createConversation(final Long senderId, final Long receiverId);

    /**
     * Retrieves a particular user conversation
     *
     * @param id represent the conversation of the user
     * @return the particular user conversation
     */
    Collection<Message> getMessages(final Long id);

    /**
     * Retrieves the all conversation of the user
     *
     * @param profileId represent the user
     * @return the user conversations
     */
    Collection<Long> getAllConversationIds(final Long profileId);

    /**
     * Deletes the user conversation
     *
     * @param id represent the conversation of the user
     * @return the delete status
     */
    boolean deleteConversation(final Long id);
}