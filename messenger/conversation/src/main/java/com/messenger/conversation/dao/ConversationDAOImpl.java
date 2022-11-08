package com.messenger.conversation.dao;

import com.messenger.connection.DataBaseConnection;
import com.messenger.conversation.model.Message;
import com.messenger.exception.UserNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
     * @param conversationId represent the conversation id of the user
     * @param messages       represent the group of messages
     * @return the status of the messages
     */
    @Override
    public boolean sendMessages(final Long conversationId, final Collection<Message> messages) {
        final Collection<Long> messageIds = generateMessageIds(messages);
        final String insertQuery = "INSERT INTO CONVERSATION_MESSAGE (CONVERSATION_ID, MESSAGE_ID) VALUES (?, ?);";

        try (Connection connection = DataBaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            for (final Long messageId : messageIds) {
                preparedStatement.setLong(1, conversationId);
                preparedStatement.setLong(2, messageId);
                preparedStatement.addBatch();
            }
            return preparedStatement.executeBatch().length > 0;
        } catch (SQLException exception) {
            throw new UserNotFoundException("User not found");
        }
    }

    /**
     * Puts the messages and retrieves a message id of the conversation
     *
     * @param messages represent the group of message
     * @return the message id
     */
    private Collection<Long> generateMessageIds(final Collection<Message> messages) {
        final String insertQuery = "INSERT INTO MESSAGE (BODY, TIME) VALUES (?, ?) RETURNING ID;";
        final Collection<Long> messageIds = new LinkedList<>();

        try (Connection connection = DataBaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            for (final Message message : messages) {
                preparedStatement.setString(1, message.getBody());
                preparedStatement.setTimestamp(2, message.getTime());

                try (ResultSet resultSet = preparedStatement.executeQuery()) {

                    while (resultSet.next()) {
                        messageIds.add(resultSet.getLong(1));
                    }
                }
            }
            return messageIds;
        } catch (SQLException exception) {
            throw new UserNotFoundException("User not found");
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param senderId   represent the sender details
     * @param receiverId represent the receiver details
     * @return the conversation id
     */
    @Override
    public Long createConversation(final Long senderId, final Long receiverId) {
        final String insertQuery = "INSERT INTO CONVERSATION (SENDER_ID, RECEIVER_ID) VALUES (?, ?) RETURNING ID ";

        try (Connection connection = DataBaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setLong(1, senderId);
            preparedStatement.setLong(2, receiverId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() ? resultSet.getLong(1) : null;
            }
        } catch (SQLException exception) {
            throw new UserNotFoundException("User not found");
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param senderId   represent the sender details
     * @param receiverId represent the receiver details
     * @return the conversation id
     */
    @Override
    public Long getConversationId(final Long senderId, final Long receiverId) {
        final StringBuilder selectQuery = new StringBuilder();

        selectQuery.append("SELECT ID FROM CONVERSATION WHERE (SENDER_ID = ? AND RECEIVER_ID = ?) OR ")
                .append ("SENDER_ID = ? AND RECEIVER_ID = ?)");

        try (Connection connection = DataBaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery.toString())) {
            preparedStatement.setLong(1, senderId);
            preparedStatement.setLong(2, receiverId);
            preparedStatement.setLong(3, receiverId);
            preparedStatement.setLong(4, senderId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() ? resultSet.getLong("ID") : null;
            }
        } catch (SQLException exception) {
            throw new UserNotFoundException("User not found");
        }
    }

    /**
     * Retrieves a particular user messages
     *
     * @param id represent the conversation of the user
     * @return the particular user conversation
     */
    @Override
    public Collection<Message> getMessages(final Long id) {
        final StringBuilder selectQuery = new StringBuilder();
        final Collection<Message> messages = new LinkedList<>();

        selectQuery.append("SELECT BODY, TIME FROM MESSAGE INNER JOIN CONVERSATION_MESSAGE ON ")
                .append(" MESSAGE.ID = CONVERSATION_MESSAGE.MESSAGE_ID WHERE CONVERSATION_MESSAGE.CONVERSATION_ID = ?");

        try (Connection connection = DataBaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery.toString())) {
            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    final Message message = new Message();

                    message.setBody(resultSet.getString("BODY"));
                    message.setTime(resultSet.getTimestamp("TIME"));
                    messages.add(message);
                }
            }
            return messages;
        } catch (SQLException exception) {
            throw new UserNotFoundException("User not found");
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param profileId represent the profile id of the user
     * @return the user conversations
     */
    public Collection<Long> getAllConversationIds(final Long profileId) {
        final Collection<Long> conversationIds = new LinkedList<>();
        final String selectQuery = "SELECT ID FROM CONVERSATION WHERE SENDER_ID = ? OR RECEIVER_ID = ?";

        try (Connection connection = DataBaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setLong(1, profileId);
            preparedStatement.setLong(2, profileId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    conversationIds.add(resultSet.getLong("ID"));
                }
            }
            return conversationIds;
        } catch (SQLException exception) {
            throw new UserNotFoundException("User not found");
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param conversationId represent the conversation id of the user
     * @return the delete status
     */
    public boolean deleteConversation(final Long conversationId) {
        final String deleteQuery = "DELETE FROM CONVERSATION WHERE ID = ?";

        try (Connection connection = DataBaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setLong(1, conversationId);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw new UserNotFoundException("User not found");
        }
    }
}