package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    // Oracle Database Connection URL
    // Default to localhost XE instance
    private static final String DEFAULT_URL = "jdbc:oracle:thin:@localhost:1521:orcl";
    private static final String DEFAULT_USER = "info";
    private static final String DEFAULT_PASSWORD = "pro";

    static {
        try {
            // Load Oracle Driver
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Oracle JDBC Driver not found. Please add ojdbc8.jar to WEB-INF/lib");
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        if (url == null || url.isEmpty()) url = DEFAULT_URL;
        if (user == null || user.isEmpty()) user = DEFAULT_USER;
        if (password == null) password = DEFAULT_PASSWORD;

        return DriverManager.getConnection(url, user, password);
    }
}
