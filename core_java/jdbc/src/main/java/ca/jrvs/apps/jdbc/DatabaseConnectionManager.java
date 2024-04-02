package ca.jrvs.apps.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DatabaseConnectionManager {
    private static final String CONFIGFILE = "config.properties";
    private final String url;
    private final Properties properties;
    private static Logger logger = LoggerFactory.getLogger(DatabaseConnectionManager.class);

    /***
     * connects to the specified db using the given information
     */
    public DatabaseConnectionManager() {
        String host = DatabaseConnectionManager.getProperty("psql.host");
        String username = DatabaseConnectionManager.getProperty("psql.username");
        String password = DatabaseConnectionManager.getProperty("psql.password");
        String databaseName = DatabaseConnectionManager.getProperty("psql.database");

        this.url = "jdbc:postgresql://"+host+"/"+databaseName;
        this.properties = new Properties();
        this.properties.setProperty("user", username);
        this.properties.setProperty("password", password);
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.url, this.properties);
    }

    /**
     * Extracts data from the config file
     * @param property what type of value we are looking for
     * @return the value found in the config file
     */
    public static String getProperty(String property) {
        Properties properties = new Properties();
        try (InputStream inputStream = DatabaseConnectionManager.class.getClassLoader().getResourceAsStream(CONFIGFILE)) {
            properties.load(inputStream);
            return properties.getProperty(property);
        } catch (IOException e) {
            //e.printStackTrace();
            logger.error("Failed to retrieve config property " + property + " in DatabaseConnectionManager-> " + e);
            return null;
        }
    }

}
