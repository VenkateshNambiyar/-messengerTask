package com.messenger.conversation.service;

import com.messenger.conversation.dao.ConversationDAO;
import com.messenger.conversation.dao.ConversationDAOImpl;
import com.messenger.conversation.model.Conversation;
import com.messenger.conversation.model.Message;
import com.messenger.exception.UserNotFoundException;
import com.messenger.profile.model.Status;
import com.messenger.profile.model.UserDetail;
import com.messenger.validation.Conversation.Delete;
import com.messenger.validation.Conversation.Select;
import com.messenger.validation.Conversation.Send;
import com.messenger.validation.Profile;
import com.messenger.validation.UserDetailValidation;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
     * @param conversation represent {@link Conversation}
     * @return the conversation id
     */
    @Override
    public Map<String, Object> createConversation(Conversation conversation) {
        final List<String> validationMessages = UserDetailValidation.validateDetails(conversation, Send.class);
        final Map<String, Object> status = new LinkedHashMap<>();

        try {

            if (validationMessages.contains("valid")) {
                final Long senderId = conversation.getSender().getId();
                final Long receiverId = conversation.getReceiver().getId();
                final Long conversationId = generateConversationId(senderId, receiverId);

                status.put("Conversation created", sendMessages(conversationId, conversation.getMessages()));
            } else {
                status.put("Info", validationMessages);
            }
        } catch (UserNotFoundException userNotFoundException) {
            status.put("Info", "User not found");
        }
        return status;
    }

    /**
     * Generates the conversation id
     *
     * @param senderId   represent the sender details
     * @param receiverId represent the receiver details
     * @return the conversation id
     */
    private Long generateConversationId(final Long senderId, final Long receiverId) {
        final Long conversationId = CONVERSATION_DAO.getConversationId(senderId, receiverId);

        return conversationId == null ? CONVERSATION_DAO.createConversation(senderId, receiverId) : conversationId;
    }

    /**
     * Sends the group of messages in the conversation
     *
     * @param conversationId represent the conversation id of the user
     * @param messageDetails represent the group of messages
     * @return the message status
     */
    private Object sendMessages(final Long conversationId, final Collection<Message> messageDetails) {
        final Collection<Message> messages = new LinkedList<>();

        for (final Message message : messageDetails) {
            final Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            message.setBody(message.getBody());
            message.setTime(timestamp);
            messages.add(message);
        }
        return CONVERSATION_DAO.sendMessages(conversationId, messages) ? conversationId : Status.FAILED;
    }

    /**
     * {@inheritDoc}
     *
     * @param conversation represent {@link Conversation}
     * @return the conversation id
     */
    @Override
    public Map<String, Object> existingConversation(final Conversation conversation) {
        final List<String> validationMessages = UserDetailValidation.validateDetails(conversation, Send.class);
        final Map<String, Object> status = new LinkedHashMap<>();

        try {

            if (validationMessages.contains("valid")) {
                status.put("Message successfully sent", sendMessages(conversation.getId(),
                        conversation.getMessages()));
            } else {
                status.put("Info", validationMessages);
            }
        } catch (UserNotFoundException userNotFoundException) {
            status.put("Info", "User conversation not found");
        }
        return status;
    }

    /**
     * {@inheritDoc}
     *
     * @param id represent the conversation id of the user
     * @return the particular user conversation message
     */
    @Override
    public Map<String, Object> getMessages(final Long id) {
        final Conversation conversation = new Conversation();

        conversation.setId(id);
        final List<String> validationMessages = UserDetailValidation.validateDetails(conversation, Select.class);
        final Map<String, Object> status = new LinkedHashMap<>();

        try {

            if (validationMessages.contains("valid")) {
                final Collection<Message> messages = CONVERSATION_DAO.getMessages(conversation.getId());

                if (messages.isEmpty()) {
                    status.put("Info", "No messages found");
                } else {
                    status.put("Conversations", messages);
                }
            } else {
                status.put("Info", validationMessages);
            }
        } catch (UserNotFoundException userNotFoundException) {
            status.put("Info", "User conversation not found");
        }
        return status;
    }

    /**
     * {@inheritDoc}
     *
     * @param id represent the user profile id
     * @return the conversation
     */
    @Override
    public Map<String, Object> getAllConversationIds(final Long id) {
        final UserDetail userDetail = new UserDetail();

        userDetail.setId(id);
        final List<String> validationMessages = UserDetailValidation.validateDetails(userDetail, Profile.Select.class);
        final Map<String, Object> status = new LinkedHashMap<>();

        try {

            if (validationMessages.contains("valid")) {
                final Collection<Long> messageIds = CONVERSATION_DAO.getAllConversationIds(userDetail.getId());

                if (messageIds.isEmpty()) {
                    status.put("Info", "No conversation found");
                } else {
                    status.put("ConversationList", messageIds);
                }
            } else {
                status.put("Info", validationMessages);
            }
        } catch (UserNotFoundException userNotFoundException) {
            status.put("Info", "User conversation not found");
        }
        return status;
    }

    /**
     * {@inheritDoc}
     *
     * @param id represent the user conversation
     * @return the delete status
     */
    @Override
    public Map<String, Object> deleteConversation(final Long id) {
        final Conversation conversation = new Conversation();

        conversation.setId(id);
        final List<String> validationMessages = UserDetailValidation.validateDetails(conversation, Delete.class);
        final Map<String, Object> status = new LinkedHashMap<>();

        try {

            if (validationMessages.contains("valid")) {

                if (CONVERSATION_DAO.deleteConversation(conversation.getId())) {
                    status.put("Conversation successfully", Status.DELETED);
                } else {
                    status.put("Info", Status.FAILED);
                }
            } else {
                status.put("Info", validationMessages);
            }
        } catch (UserNotFoundException userNotFoundException) {
            status.put("Info", "User conversation not found");
        }
        return status;
    }
}