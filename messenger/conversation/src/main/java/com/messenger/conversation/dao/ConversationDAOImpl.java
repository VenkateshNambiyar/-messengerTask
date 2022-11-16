package com.messenger.conversation.dao;

import com.messenger.connection.DataBaseConnection;
import com.messenger.conversation.model.Conversation;
import com.messenger.conversation.model.Message;
import com.messenger.exception.ConversationNotFoundException;
import com.messenger.exception.ProfileNotFoundException;
import com.messenger.exception.UserNotFoundException;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Manages the connection with the data source to obtain and store data.
 *
 * @author venkatesh
 * @version 1.0
 */
public class ConversationDAOImpl implements ConversationDAO {

    /**
     * {@inheritDoc}
     *
     * @param message represent the {@link Message}
     * @return the status of the messages
     */
    @Override
    public Long sendMessage(final Message message) {
        final Long senderId = message.getSenderId();
        final Long receiverId = message.getReceiverId();
        final Long conversationId = getConversationId(senderId, receiverId);
        final StringBuilder insertQuery = new StringBuilder();

        insertQuery.append("INSERT INTO MESSAGE (BODY, TIME, SENDER_ID, RECEIVER_ID) VALUES (?, ?, ?, ?)")
                .append(" RETURNING ID");

        try (PreparedStatement preparedStatement = DataBaseConnection.getInstance().getConnection()
                .prepareStatement(insertQuery.toString())) {
            preparedStatement.setString(1, message.getBody().trim());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(message.getTime()));
            preparedStatement.setLong(3, senderId);
            preparedStatement.setLong(4, receiverId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                final Long messageId = resultSet.next() ? resultSet.getLong("ID") : null;

                return createMessage(conversationId, messageId);
            }
        } catch (SQLException exception) {
            throw new UserNotFoundException("Profile not found");
        }
    }

    /**
     * Creates a message
     *
     * @param conversationId represent the conversation of the user
     * @param messageId      represent the message in the conversation
     * @return the message id
     */
    private Long createMessage(final Long conversationId, final Long messageId) {
        final String insertQuery = "INSERT INTO CONVERSATION_MESSAGE (CONVERSATION_ID, MESSAGE_ID) VALUES (?, ?);";

        try (PreparedStatement preparedStatement = DataBaseConnection.getInstance().getConnection()
                .prepareStatement(insertQuery)) {
            if (messageId == null) {
                throw new ConversationNotFoundException("Conversation not found");
            }
            preparedStatement.setLong(1, conversationId);
            preparedStatement.setLong(2, messageId);

            return preparedStatement.executeUpdate() > 0 ? conversationId : null;
        } catch (SQLException exception) {
            throw new ProfileNotFoundException("Profile not found");
        }
    }

    /**
     * Creates a conversation and retrieve the conversation id
     *
     * @param senderId   represent the sender details
     * @param receiverId represent the receiver details
     * @return the conversation id
     */
    private Long createConversation(final Long senderId, final Long receiverId) {
        final String insertQuery = "INSERT INTO CONVERSATION (PARTICIPANT) VALUES (?) RETURNING ID ";

        try (Connection connection = DataBaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            final Long[] participant = new Long[2];

            participant[0] = senderId;
            participant[1] = receiverId;
            final Array array = connection.createArrayOf("long", participant);

            preparedStatement.setArray(1, array);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() ? resultSet.getLong("ID") : null;
            }
        } catch (SQLException exception) {
            throw new ProfileNotFoundException("Profile not found");
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param senderId   represent the sender details
     * @param receiverId represent the receiver details
     * @return the conversation id
     */
    public Long getConversationId(final Long senderId, final Long receiverId) {
        final StringBuilder selectQuery = new StringBuilder();

        selectQuery.append("SELECT ID FROM CONVERSATION WHERE (PARTICIPANT[1] = ? AND PARTICIPANT[2] = ?) OR ")
                .append("(PARTICIPANT[2] = ? AND PARTICIPANT[1] = ?)");

        try (Connection connection = DataBaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery.toString())) {
            preparedStatement.setLong(1, senderId);
            preparedStatement.setLong(2, receiverId);
            preparedStatement.setLong(3, senderId);
            preparedStatement.setLong(4, receiverId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() ? Long.valueOf(resultSet.getLong("ID"))
                        : createConversation(senderId, receiverId);
            }
        } catch (SQLException exception) {
            throw new ProfileNotFoundException("Profile not found");
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param id represent the conversation id
     * @return the participant user_name
     */
    @Override
    public Collection<String> getParticipants(final Long id) {
        final String selectQuery = "SELECT PARTICIPANT FROM CONVERSATION WHERE CONVERSATION.ID = ?";

        try (PreparedStatement preparedStatement = DataBaseConnection.getInstance().getConnection()
                .prepareStatement(selectQuery)) {
            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    final Array participants = resultSet.getArray("PARTICIPANT");
                    final Long[] participantIds = (Long[]) participants.getArray();

                    return getParticipants(participantIds);
                }
            }
            return null;
        } catch (Exception e) {
            throw new ConversationNotFoundException("Conversation not found");
        }
    }

    /**
     * Retrieves the participant user_name
     *
     * @param participantIds represent the id of the participants
     * @return the participant user_name
     */
    private Collection<String> getParticipants(final Long[] participantIds) {
        final Collection<String> participants = new LinkedList<>();
        final String selectQuery = "SELECT USER_NAME FROM PROFILE WHERE(ID = ? OR ID = ?)";

        try (PreparedStatement preparedStatement = DataBaseConnection.getInstance().getConnection()
                .prepareStatement(selectQuery)) {
            preparedStatement.setLong(1, participantIds[0]);
            preparedStatement.setLong(2, participantIds[1]);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    participants.add(resultSet.getString("USER_NAME"));
                }
            }
            return participants;
        } catch (Exception e) {
            throw new ConversationNotFoundException("Conversation not found");
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param conversationId represent the conversation of the user
     * @param userId         represent the profile of the user
     * @return the particular user conversation
     */
    @Override
    public Collection<Message> getMessages(final Long userId, final Long conversationId) {
        final Collection<Message> messages = new LinkedList<>();
        final StringBuilder selectQuery = new StringBuilder();

        selectQuery.append("SELECT MESSAGE.ID, BODY, TIME, SENDER_ID, PROFILE.ID AS PROFILE_ID, USER_NAME FROM ")
                .append("MESSAGE INNER JOIN CONVERSATION_MESSAGE ON MESSAGE.ID = CONVERSATION_MESSAGE.MESSAGE_ID ")
                .append("INNER JOIN PROFILE ON MESSAGE.SENDER_ID = PROFILE.ID ")
                .append("WHERE CONVERSATION_MESSAGE.CONVERSATION_ID = ? AND (SENDER_ID = ? OR RECEIVER_ID = ?) ")
                .append("ORDER BY MESSAGE.ID DESC");

        try (PreparedStatement preparedStatement = DataBaseConnection.getInstance().getConnection()
                .prepareStatement(selectQuery.toString())) {
            preparedStatement.setLong(1, conversationId);
            preparedStatement.setLong(2, userId);
            preparedStatement.setLong(3, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    final Message message = new Message();

                    message.setMessageId(resultSet.getLong("ID"));
                    message.setBody(resultSet.getString("BODY"));
                    message.setTime(resultSet.getTimestamp("TIME").toString());
                    message.setSenderName(userId == resultSet.getLong("SENDER_ID") ? "You"
                            : resultSet.getString("USER_NAME"));
                    messages.add(message);
                }
            }
            return messages;
        } catch (SQLException exception) {
            throw new ConversationNotFoundException("Conversation not found");
        }
    }

    /**
     * Retrieves the messages in the conversation
     *
     * @param userId         represent the user profile
     * @param conversationId represent the conversation conversationId of the user
     * @param selectQuery    represent the postgresql query to retrieve the messages
     * @return the messages
     */
    private Message getMessages(final Long userId, final Long conversationId, final String selectQuery) {
        try (PreparedStatement preparedStatement = DataBaseConnection.getInstance().getConnection()
                .prepareStatement(selectQuery)) {
            preparedStatement.setLong(1, conversationId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    final Message message = new Message();

                    message.setMessageId(resultSet.getLong("ID"));
                    message.setBody(resultSet.getString("BODY"));
                    message.setTime(resultSet.getTimestamp("TIME").toString());
                    message.setSenderName(userId == resultSet.getLong("SENDER_ID") ? "You"
                            : resultSet.getString("USER_NAME"));

                    return message;
                }
            }
            return null;
        } catch (SQLException exception) {
            throw new ConversationNotFoundException("Conversation not found");
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param userId represent the profile id of the user
     * @return the user conversations
     */
    @Override
    public Collection<Conversation> getAllConversations(final Long userId) {
        final Collection<Conversation> conversations = new LinkedList<>();
        final StringBuilder selectConversation = new StringBuilder();
        final StringBuilder selectMessage = new StringBuilder();

        selectConversation.append("SELECT CONVERSATION.ID FROM CONVERSATION INNER JOIN PROFILE ON ")
                .append("PARTICIPANT[1] = PROFILE.ID OR PARTICIPANT[2] = PROFILE.ID WHERE PROFILE.ID = ? ");

        selectMessage.append("SELECT MESSAGE.ID, BODY, TIME, SENDER_ID, PROFILE.ID AS PROFILE_ID, USER_NAME ")
                .append("FROM MESSAGE INNER JOIN CONVERSATION_MESSAGE ON MESSAGE.ID = CONVERSATION_MESSAGE.MESSAGE_ID ")
                .append("INNER JOIN PROFILE ON MESSAGE.SENDER_ID = PROFILE.ID ")
                .append("WHERE CONVERSATION_MESSAGE.CONVERSATION_ID = ? ORDER BY MESSAGE.ID DESC LIMIT 1 OFFSET 0 ");

        try (PreparedStatement preparedStatement = DataBaseConnection.getInstance().getConnection()
                .prepareStatement(selectConversation.toString())) {
            preparedStatement.setLong(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    final Conversation conversation = new Conversation();

                    conversation.setConversationId(resultSet.getLong("ID"));
                    conversation.setParticipants(getParticipants(conversation.getConversationId()));
                    conversation.setMessage(getMessages(userId, conversation.getConversationId(),
                            selectMessage.toString()));
                    conversations.add(conversation);
                }
            }
            return conversations;
        } catch (SQLException exception) {
            throw new ConversationNotFoundException("Conversation not found");
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param userId         represent the user profile
     * @param conversationId represent the conversation id of the user
     * @return the delete status
     */
    public boolean deleteConversation(final Long userId, final Long conversationId) {
        final StringBuilder deleteQuery = new StringBuilder();

        deleteQuery.append("DELETE FROM CONVERSATION_MESSAGE USING CONVERSATION WHERE CONVERSATION_ID = ? AND ")
                .append("(PARTICIPANT[1] = ? OR PARTICIPANT[2] = ?)");

        try (PreparedStatement preparedStatement = DataBaseConnection.getInstance().getConnection()
                .prepareStatement(deleteQuery.toString())) {
            preparedStatement.setLong(1, conversationId);
            preparedStatement.setLong(2, userId);
            preparedStatement.setLong(3, userId);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw new ConversationNotFoundException("Conversation not found");
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param conversationId represent the conversation id of the user
     * @param messageId      represent the message id of the conversation
     * @return the delete status
     */
    public boolean deleteMessage(final Long conversationId, final Long messageId) {
        final StringBuilder deleteQuery = new StringBuilder();

        deleteQuery.append("DELETE FROM MESSAGE USING CONVERSATION_MESSAGE WHERE MESSAGE.ID = MESSAGE_ID AND ")
                .append("CONVERSATION_ID = ? AND MESSAGE_ID = ? ");

        try (PreparedStatement preparedStatement = DataBaseConnection.getInstance().getConnection()
                .prepareStatement(deleteQuery.toString())) {
            preparedStatement.setLong(1, conversationId);
            preparedStatement.setLong(2, messageId);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw new ConversationNotFoundException("Conversation not found");
        }
    }
}