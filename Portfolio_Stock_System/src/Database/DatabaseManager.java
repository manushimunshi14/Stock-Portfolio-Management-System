package Database;

import FileProcess.DatabaseConfigReader;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DatabaseManager {
    private Connection dbConnector;
    private String url;
    private String user;
    private String password;
    private int lastInsertedId;

    public int getLastInsertedId() {
        return this.lastInsertedId;
    }

    public DatabaseManager() {
        String filePath = "Portfolio_Stock_System/src/Database/databaseConfig.txt";
        Map<String, String> dbConfig = new HashMap<>();
        try {
            dbConfig = DatabaseConfigReader.readDatabaseConfig(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.url = "jdbc:mysql://" + dbConfig.get("Server") + ":" + dbConfig.get("Port number") + "/"
                + dbConfig.get("Name");
        this.user = dbConfig.get("Username");
        this.password = dbConfig.get("Password");
    }

    public void closeConnection() {
        try {
            if (this.dbConnector != null) {
                this.dbConnector.close();
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    public Connection getDbConnector() {
        try {
            // reopen if closed
            if (dbConnector == null || dbConnector.isClosed()) {
                Properties properties = new Properties();
                properties.setProperty("user", user);
                properties.setProperty("password", password);
                properties.setProperty("useSSL", "false");
                dbConnector = DriverManager.getConnection(url, properties);
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return dbConnector;
    }

    public void executeUpdate(String sql, boolean returnKeys, Object... params) throws SQLException {
        try (Connection conn = getDbConnector();
             PreparedStatement pStmt = returnKeys
                     ? conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
                     : conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                pStmt.setObject(i + 1, params[i]);
            }
            pStmt.executeUpdate();
            if (returnKeys) {
                getLastInsertedId(pStmt);
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
            System.out.println("query: " + sql);
            throw e;
        }
    }


    private void getLastInsertedId(PreparedStatement stmt) throws SQLException {
        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                this.lastInsertedId = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Creating record failed, no ID obtained.");
            }
        }
    }

    public <T> T executeQuery(String sql, ResultSetHandler<T> handler, Object... params) throws SQLException {
        try (Connection conn = getDbConnector();
                PreparedStatement pStmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            for (int i = 0; i < params.length; i++) {
                pStmt.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = pStmt.executeQuery()) {
                return handler.handle(rs);
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
            throw e;
        }
    }
}
