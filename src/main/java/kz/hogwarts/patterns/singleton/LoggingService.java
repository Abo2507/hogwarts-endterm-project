package kz.hogwarts.patterns.singleton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Singleton Pattern: Logging Service
 * Provides centralized logging functionality across the application
 */
public class LoggingService {

    private static LoggingService instance;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private LoggingService() {}

    public static synchronized LoggingService getInstance() {
        if (instance == null) {
            instance = new LoggingService();
        }
        return instance;
    }

    public void info(String message) {
        log("INFO", message);
    }


    public void error(String message) {
        log("ERROR", message);
    }

    public void debug(String message) {
        log("DEBUG", message);
    }

    private void log(String level, String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        System.out.println(String.format("[%s] [%s] %s", timestamp, level, message));
    }
}
