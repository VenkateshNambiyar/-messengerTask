package com.messenger.conversation.service;

import com.messenger.conversation.model.Conversation;
import com.messenger.conversation.model.Message;
import com.messenger.profile.model.Status;

import java.util.Collection;

/**
 * Provides the conversation service
 *
 * @author venkatesh
 * @version 1.0
 */
public interface ConversationService {

    /**
     * Sends the message in the conversation
     *
     * @param message represent the {@link Message}
     * @return the conversation id
     */
    Long sendMessage(final Message message);

    /**
     * Retrieves a particular user conversation
     *
     * @param conversationId represent the conversation id of the user
     * @param userId         represent the user profile
     * @return the particular user conversation message
     */
    Conversation getConversation(final Long userId, final Long conversationId);

    /**
     * Retrieves the all conversation of the user
     *
     * @param userId represent the user profile
     * @return return the conversation
     */
    Collection<Conversation> getAllConversations(final Long userId);

    /**
     * Deletes the user conversation
     *
     * @param userId         represent the user profile
     * @param conversationId represent the conversation of the user
     * @return the delete status
     */
    Enum<Status> deleteConversation(final Long userId, final Long conversationId);

    /**
     * Deletes the particular message in the conversation
     *
     * @param conversationId represent the conversation of the user
     * @param messageId      represent the particular message in conversation
     * @return the delete status
     */
    Enum<Status> deleteMessage(final Long conversationId, final Long messageId);
}