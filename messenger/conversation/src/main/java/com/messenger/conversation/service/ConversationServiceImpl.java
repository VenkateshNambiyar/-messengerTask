package com.messenger.conversation.service;

import com.messenger.conversation.dao.ConversationDAO;
import com.messenger.conversation.dao.ConversationDAOImpl;
import com.messenger.conversation.model.Conversation;
import com.messenger.conversation.model.Message;
import com.messenger.exception.ConversationNotFoundException;
import com.messenger.profile.model.Status;

import java.sql.Timestamp;
import java.util.Collection;

/**
 * Implements the conversation service.
 *
 * @author venkatesh
 * @version 1.0
 */
public class ConversationServiceImpl implements ConversationService {

    private static final ConversationDAO CONVERSATION_DAO = new ConversationDAOImpl();

    /**
     * {@inheritDoc}
     *
     * @param message represent the {@link Message}
     * @return the conversation id
     */
    @Override
    public Long sendMessage(final Message message) {
        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        message.setTime(timestamp.toString());

        return CONVERSATION_DAO.sendMessage(message);
    }

    /**
     * {@inheritDoc}
     *
     * @param userId         represent the profile id
     * @param conversationId represent the conversation id of the user
     * @return the particular user conversation message
     */
    @Override
    public Conversation getConversation(final Long userId, final Long conversationId) {
        final Conversation conversation = new Conversation();

        conversation.setUserId(userId);
        conversation.setMessages(CONVERSATION_DAO.getMessages(userId, conversationId));

        if (conversation.getMessages().isEmpty()) {
            throw new ConversationNotFoundException("Conversation not found");
        }
        conversation.setConversationId(conversationId);
        conversation.setParticipants(CONVERSATION_DAO.getParticipants(conversationId));

        return conversation;
    }

    /**
     * {@inheritDoc}
     *
     * @param userId represent the user profile id
     * @return the conversation ids
     */
    @Override
    public Collection<Conversation> getAllConversations(final Long userId) {
        final Collection<Conversation> conversations = CONVERSATION_DAO.getAllConversations(userId);

        if (0 == conversations.size()) {
            throw new ConversationNotFoundException("Conversation not found");
        }
        return conversations;
    }

    /**
     * {@inheritDoc}
     *
     * @param userId         represent the user profile
     * @param conversationId represent the conversation of the user
     * @return the delete status
     */
    @Override
    public Enum<Status> deleteConversation(final Long userId, final Long conversationId) {
        if (!CONVERSATION_DAO.deleteConversation(userId, conversationId)) {
            throw new ConversationNotFoundException("Conversation not found");
        }
        return Status.DELETED;
    }

    /**
     * {@inheritDoc}
     *
     * @param conversationId represent the conversation of the user
     * @param messageId      represent the particular message in conversation
     * @return
     */
    @Override
    public Enum<Status> deleteMessage(final Long conversationId, final Long messageId) {
        if (!CONVERSATION_DAO.deleteMessage(conversationId, messageId)) {
            throw new ConversationNotFoundException("Conversation not found");
        }
        return Status.DELETED;
    }
}