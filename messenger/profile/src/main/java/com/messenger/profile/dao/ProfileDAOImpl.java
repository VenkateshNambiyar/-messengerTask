package com.messenger.profile.dao;

import com.messenger.connection.DataBaseConnection;
import com.messenger.exception.DataAlreadyExistException;
import com.messenger.exception.ProfileNotFoundException;
import com.messenger.exception.UserNotFoundException;
import com.messenger.profile.model.Profile;

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
     * @param profile represent {@link Profile}
     * @return the id of the user profile
     */
    @Override
    public Long create(final Profile profile) {
        final StringBuilder insertQuery = new StringBuilder();

        insertQuery.append("INSERT INTO PROFILE(EMAIL, MOBILE_NUMBER, USER_NAME, NAME, PASSWORD)")
                .append(" VALUES (?, ?, ?, ?, ?) RETURNING ID");

        try (Connection connection = DataBaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery.toString())) {
            preparedStatement.setString(1, profile.getEmail().trim());
            preparedStatement.setString(2, profile.getMobileNumber().trim());
            preparedStatement.setString(3, profile.getUserName().trim());
            preparedStatement.setString(4, profile.getName().trim());
            preparedStatement.setString(5, profile.getPassword().trim());

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
    public Profile get(final Long id) {
        final String selectQuery = "SELECT ID, EMAIL, MOBILE_NUMBER, USER_NAME, NAME FROM PROFILE WHERE ID = ?";

        try (Connection connection = DataBaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    final Profile profile = new Profile();

                    profile.setId(resultSet.getLong("ID"));
                    profile.setEmail(resultSet.getString("EMAIL"));
                    profile.setMobileNumber(resultSet.getString("MOBILE_NUMBER"));
                    profile.setUserName(resultSet.getString("USER_NAME"));
                    profile.setName(resultSet.getString("NAME"));

                    return profile;
                }
            }
            return null;
        } catch (SQLException exception) {
            throw new ProfileNotFoundException("User not found");
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param profile represent {@link Profile}
     * @return the update status
     */
    @Override
    public boolean update(final Profile profile) {
        final StringBuilder updateQuery = new StringBuilder();
        final String email = profile.getEmail();
        final String mobileNumber = profile.getMobileNumber();
        final String userName = profile.getUserName();
        final String name = profile.getName();
        final String password = profile.getPassword();
        final Long id = profile.getId();
        final Profile userDetail = getUserDetail(id);

        updateQuery.append("UPDATE PROFILE SET EMAIL = CASE WHEN EMAIL = ? IS NULL THEN ? ELSE ? END, ")
                .append("MOBILE_NUMBER = CASE WHEN MOBILE_NUMBER = ? IS NULL THEN ? ELSE ? END, ")
                .append("USER_NAME = CASE WHEN USER_NAME = ? IS NULL THEN ? ELSE ? END, ")
                .append("NAME = CASE WHEN NAME = ? IS NULL THEN ? ELSE ? END, ")
                .append("PASSWORD = CASE WHEN PASSWORD = ? IS NULL THEN ? ELSE ? END WHERE ID = ?");

        try (Connection connection = DataBaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery.toString())) {

            if (userDetail != null) {
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, userDetail.getEmail());
                preparedStatement.setString(3, email);
                preparedStatement.setString(4, mobileNumber);
                preparedStatement.setString(5, userDetail.getMobileNumber());
                preparedStatement.setString(6, mobileNumber);
                preparedStatement.setString(7, userName);
                preparedStatement.setString(8, userDetail.getUserName());
                preparedStatement.setString(9, userName);
                preparedStatement.setString(10, name);
                preparedStatement.setString(11, userDetail.getName());
                preparedStatement.setString(12, name);
                preparedStatement.setString(13, password);
                preparedStatement.setString(14, userDetail.getPassword());
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
    private Profile getUserDetail(final Long id) {
        final StringBuilder selectQuery = new StringBuilder();

        selectQuery.append("SELECT ID, EMAIL, MOBILE_NUMBER, USER_NAME, NAME, PASSWORD FROM PROFILE ")
                .append("WHERE ID = ?");

        try (Connection connection = DataBaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery.toString())) {
            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    final Profile userDetail = new Profile();

                    userDetail.setId(resultSet.getLong("ID"));
                    userDetail.setEmail(resultSet.getString("EMAIL"));
                    userDetail.setMobileNumber(resultSet.getString("MOBILE_NUMBER"));
                    userDetail.setUserName(resultSet.getString("USER_NAME"));
                    userDetail.setName(resultSet.getString("NAME"));
                    userDetail.setPassword(resultSet.getString("PASSWORD"));

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
     * @param id represent the profile id of the user
     * @return the delete status
     */
    @Override
    public boolean delete(final Long id) {
        final String deleteQuery = "DELETE FROM PROFILE WHERE ID = ? ";

        try (Connection connection = DataBaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setLong(1, id);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw new UserNotFoundException("User not found");
        }
    }
}