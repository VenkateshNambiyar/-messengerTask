package com.messenger.profile.dao;

import com.messenger.connection.DataBaseConnection;
import com.messenger.exception.DataAlreadyExistException;
import com.messenger.exception.UserNotFoundException;
import com.messenger.profile.model.UserDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implements the dao services
 *
 * @author venkatesh
 * @version 1.0
 */
public class ProfileDAOImpl implements ProfileDAO {

    /**
     * {@inheritDoc}
     *
     * @param userDetail represent {@link UserDetail}
     * @return the id of the user profile
     */
    @Override
    public Long create(final UserDetail userDetail) {
        final StringBuilder insertQuery = new StringBuilder();

        insertQuery.append("INSERT INTO USER_DETAIL(EMAIL, MOBILE_NUMBER, USER_NAME, NAME, PASSWORD)")
                .append(" VALUES (?, ?, ?, ?, ?) RETURNING ID");

        try (Connection connection = DataBaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery.toString())) {
            preparedStatement.setString(1, userDetail.getEmail());
            preparedStatement.setString(2, userDetail.getMobileNumber());
            preparedStatement.setString(3, userDetail.getUserName());
            preparedStatement.setString(4, userDetail.getName());
            preparedStatement.setString(5, userDetail.getPassword());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() ? resultSet.getLong(1) : null;
            }
        } catch (SQLException exception) {
            throw new DataAlreadyExistException("User already exists");
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param id represent the profile id of the user profile
     * @return the particular user profile
     */
    @Override
    public UserDetail get(final Long id) {
        final String selectQuery = "SELECT ID, EMAIL, MOBILE_NUMBER, USER_NAME, NAME FROM USER_DETAIL WHERE ID = ?";

        try (Connection connection = DataBaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    final UserDetail userDetail = new UserDetail();

                    userDetail.setId(resultSet.getLong("ID"));
                    userDetail.setEmail(resultSet.getString("EMAIL"));
                    userDetail.setMobileNumber(resultSet.getString("MOBILE_NUMBER"));
                    userDetail.setUserName(resultSet.getString("USER_NAME"));
                    userDetail.setName(resultSet.getString("NAME"));

                    return userDetail;
                }
            }
            return null;
        } catch (SQLException exception) {
            throw new UserNotFoundException("User not found");
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param userDetail represent {@link UserDetail}
     * @return the update status
     */
    @Override
    public boolean update(final UserDetail userDetail) {
        final StringBuilder updateQuery = new StringBuilder();
        final String email = userDetail.getEmail();
        final String mobileNumber = userDetail.getMobileNumber();
        final String userName = userDetail.getUserName();
        final String name = userDetail.getName();
        final String password = userDetail.getPassword();
        final Long id = userDetail.getId();
        final UserDetail oldUserDetail = getUserDetail(id);

        updateQuery.append("UPDATE USER_DETAIL SET EMAIL = CASE WHEN EMAIL = ? IS NULL THEN ? ELSE ? END, ")
                .append("MOBILE_NUMBER = CASE WHEN MOBILE_NUMBER = ? IS NULL THEN ? ELSE ? END, ")
                .append("USER_NAME = CASE WHEN USER_NAME = ? IS NULL THEN ? ELSE ? END, ")
                .append("NAME = CASE WHEN NAME = ? IS NULL THEN ? ELSE ? END, ")
                .append("PASSWORD = CASE WHEN PASSWORD = ? IS NULL THEN ? ELSE ? END WHERE ID = ?");

        try (Connection connection = DataBaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery.toString())) {

            if (oldUserDetail != null) {
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, oldUserDetail.getEmail());
                preparedStatement.setString(3, email);
                preparedStatement.setString(4, mobileNumber);
                preparedStatement.setString(5, oldUserDetail.getMobileNumber());
                preparedStatement.setString(6, mobileNumber);
                preparedStatement.setString(7, userName);
                preparedStatement.setString(8, oldUserDetail.getUserName());
                preparedStatement.setString(9, userName);
                preparedStatement.setString(10, name);
                preparedStatement.setString(11, oldUserDetail.getName());
                preparedStatement.setString(12, name);
                preparedStatement.setString(13, password);
                preparedStatement.setString(14, oldUserDetail.getPassword());
                preparedStatement.setString(15, password);
                preparedStatement.setLong(16, id);

                return preparedStatement.executeUpdate() > 0;
            }
            return false;
        } catch (SQLException exception) {
            throw new UserNotFoundException("User not found");
        }
    }

    /**
     * Retrieves the user details
     *
     * @param id represent the profile id
     * @return the user details
     */
    private UserDetail getUserDetail(final Long id) {
        final StringBuilder selectQuery = new StringBuilder();

        selectQuery.append("SELECT ID, EMAIL, MOBILE_NUMBER, USER_NAME, NAME, PASSWORD FROM USER_DETAIL ")
                .append("WHERE ID = ?");

        try (Connection connection = DataBaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery.toString())) {
            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    final UserDetail oldDetail = new UserDetail();

                    oldDetail.setId(resultSet.getLong("ID"));
                    oldDetail.setEmail(resultSet.getString("EMAIL"));
                    oldDetail.setMobileNumber(resultSet.getString("MOBILE_NUMBER"));
                    oldDetail.setUserName(resultSet.getString("USER_NAME"));
                    oldDetail.setName(resultSet.getString("NAME"));
                    oldDetail.setPassword(resultSet.getString("PASSWORD"));

                    return oldDetail;
                }
            }
            return null;
        } catch (SQLException exception) {
            throw new UserNotFoundException("User not found");
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param id represent the profile id of the user
     * @return the delete status
     */
    @Override
    public boolean delete(final Long id) {
        final String deleteQuery = "DELETE FROM USER_DETAIL WHERE ID = ? ";

        try (Connection connection = DataBaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setLong(1, id);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw new UserNotFoundException("User not found");
        }
    }
}