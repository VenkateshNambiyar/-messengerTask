package com.messenger.conversation.dao;

import com.messenger.conversation.model.Conversation;
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
     * @param messages represent the {@link Message}
     * @return the conversation id
     */
    Long sendMessage(final Message messages);

    /**
     * Retrieves the participant in the conversation
     *
     * @param id represent the conversation id
     * @return the participant user_name
     */
    Collection<String> getParticipants(final Long id);

    /**
     * Retrieves a particular user conversation
     *
     * @param userId         represent the profile id of the user
     * @param conversationId represent the conversation of the user
     * @return the particular user conversation
     */
    Collection<Message> getMessages(final Long userId, final Long conversationId);

    /**
     * Retrieves the all conversation of the user
     *
     * @param userId represent the user
     * @return the user conversations
     */
    Collection<Conversation> getAllConversations(final Long userId);

    /**
     * Deletes the user conversation
     *
     * @param userId         represent the user profile
     * @param conversationId represent the conversation the user
     * @return the delete status
     */
    boolean deleteConversation(final Long userId, final Long conversationId);

    /**
     * Deletes the particular message in conversation
     *
     * @param conversationId represent the conversation of the user
     * @param messageId      represent the message id in the conversation
     * @return the delete status
     */
    boolean deleteMessage(final Long conversationId, final Long messageId);
}