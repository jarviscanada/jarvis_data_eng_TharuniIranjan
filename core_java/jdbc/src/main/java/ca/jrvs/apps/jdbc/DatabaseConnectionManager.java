package ca.jrvs.apps.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DatabaseConnectionManager {
    private static final String configFile = "config.properties";
    private final String url;
    private final Properties properties;

    public static String getProperty(String property, String configFile) {
        Properties properties = new Properties();
        try (InputStream inputStream = DatabaseConnectionManager.class.getClassLoader().getResourceAsStream(configFile)) {
            properties.load(inputStream);
            return properties.getProperty(property);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public DatabaseConnectionManager(String databaseName) {
        String host = DatabaseConnectionManager.getProperty("psql.host", DatabaseConnectionManager.configFile);
        String username = DatabaseConnectionManager.getProperty("psql.username", DatabaseConnectionManager.configFile);
        String password = DatabaseConnectionManager.getProperty("psql.password", DatabaseConnectionManager.configFile);

        this.url = "jdbc:postgresql://"+host+"/"+databaseName;
        this.properties = new Properties();
        this.properties.setProperty("user", username);
        this.properties.setProperty("password", password);
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.url, this.properties);
    }

}
