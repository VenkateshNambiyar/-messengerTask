package com.messenger.connection;

import com.messenger.exception.ConnectionNotFoundException;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * Connects to the database and provide services.
 *
 * @author Venkatesh N
 * @version 1.0
 */
public class DataBaseConnection {

    private static DataBaseConnection connectDataBase;

    /**
     * Create a private Constructor
     */
    private DataBaseConnection() {
    }

    /**
     * Returns {@link DataBaseConnection} instance.
     *
     * @return the connection database instance
     */
    public static DataBaseConnection getInstance() {

        if (connectDataBase == null) {
            connectDataBase = new DataBaseConnection();
        }
        return connectDataBase;
    }

    /**
     * Gets the connection {@link Connection}
     *
     * @return the {@link Connection}
     */
    public Connection getConnection() {
        final Properties properties = new Properties();
        try (InputStream inputStream = this.getClass().getClassLoader()
                .getResourceAsStream("ConnectionDetails.properties")) {

            properties.load(inputStream);
            Class.forName("org.postgresql.Driver");

            return DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("username"),
                    properties.getProperty("password"));
        } catch (Exception exception) {
            throw new ConnectionNotFoundException("Database not connected");
        }
    }
}