package kz.hogwarts.patterns.singleton;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Singleton Pattern: Database Configuration Manager
 * Ensures only one instance of database configuration exists throughout the application
 */
@Component
public class DatabaseConfig {

    private static DatabaseConfig instance;

    @Value("${spring.datasource.url}")
    private String databaseUrl;

    @Value("${spring.datasource.username}")
    private String username;


    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    private DatabaseConfig() {}

    public static synchronized DatabaseConfig getInstance() {
        if (instance == null) {
            instance = new DatabaseConfig();
        }
        return instance;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public String getConfig() {
        return String.format("Database: %s, User: %s, Driver: %s",
                databaseUrl, username, driverClassName);
    }
}