package DatabaseReader;

import Database.DatabaseManager;
import System.Account.SignupRequest;
import System.User.Customer;

import java.sql.*;
import java.util.*;

public class Requestdb {
    private DatabaseManager dbManager;
    private static int requestNumberCounter = 0;

    public Requestdb(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    // Fetch all requests
    public List<Map<String, Object>> getAllRequests() {
        String query = "SELECT user_name, display_name, password, derivative FROM request";
        return getAllRequest(query);
    }

    // Common method to fetch all rows
    private List<Map<String, Object>> getAllRequest(String query) {
        List<Map<String, Object>> rows = new ArrayList<>();

        try (Connection connection = dbManager.getDbConnector();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object columnValue = resultSet.getObject(i);
                    row.put(columnName, columnValue);
                }
                rows.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.closeConnection();
        }

        return rows;
    }

    public List<Customer> getDerivativeRequest() {
        List<Customer> customers = new ArrayList<>();
        // Adjusted query to fetch required fields for Customer constructor
        String query = "SELECT u.id AS user_id, u.display_name AS name, u.user_name AS username, u.manager_id, u.password_id FROM request r JOIN user u ON r.user_name = u.user_name JOIN wallet w ON u.id = w.user_id WHERE r.derivative = 1";

        try (Connection connection = dbManager.getDbConnector();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("user_id");
                String name = resultSet.getString("name");
                String username = resultSet.getString("username");
                int managerId = resultSet.getInt("manager_id");
                int passwordId = resultSet.getInt("password_id");

                Customer customer = new Customer(id, name, username, managerId, passwordId);
                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.closeConnection();
        }

        return customers;
    }

    public List<SignupRequest> getSignupRequests() {
        List<SignupRequest> signupRequests = new ArrayList<>();
        String query = "SELECT r.id, r.display_name, r.user_name, r.password FROM request r WHERE r.derivative = 0";

        try (Connection connection = dbManager.getDbConnector();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int requestNumber = resultSet.getInt("id");
                String displayName = resultSet.getString("display_name");
                String userName = resultSet.getString("user_name");
                String password = resultSet.getString("password");

                SignupRequest signupRequest = new SignupRequest(requestNumber, displayName, userName, password);
                signupRequests.add(signupRequest);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManager.closeConnection();
        }

        return signupRequests;
    }


    public boolean deleteRequest(String username,String where) {
        String query = "DELETE FROM request WHERE " + where + " = ?";

        try (Connection connection = dbManager.getDbConnector();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Request deleted successfully for username: " + username);
                return true;
            } else {
                System.out.println("No request found for username: " + username);
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while deleting request: " + e.getMessage());
            e.printStackTrace();
        } finally {
            dbManager.closeConnection();
        }
        return false;
    }

    public boolean addToDerivative(String userName) {
        String getUserIdQuery = "SELECT id FROM user WHERE display_name = ?";
        String insertQuery = "INSERT INTO derivative_user (user_id, derivative) VALUES (?, 1)";

        try (Connection conn = dbManager.getDbConnector();
             PreparedStatement pstmtGetUserId = conn.prepareStatement(getUserIdQuery)) {

            pstmtGetUserId.setString(1, userName);
            ResultSet rs = pstmtGetUserId.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id");

                try (PreparedStatement pstmtInsert = conn.prepareStatement(insertQuery)) {
                    pstmtInsert.setInt(1, userId);
                    int rowsAffected = pstmtInsert.executeUpdate();

                    return rowsAffected > 0;
                }
            } else {
                System.out.println("No user found with username: " + userName);
                return false;
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            return false;
        }
    }

    public boolean addRequest(String userName, String customer_name,String password,int isDirevative) {
        String insertQuery = "INSERT INTO request (user_name, display_name, password, derivative) VALUES (?, ?, ?, ?)";

        try (Connection conn = dbManager.getDbConnector();
             PreparedStatement pStmt = conn.prepareStatement(insertQuery)) {

            pStmt.setString(1, userName);
            pStmt.setString(2, customer_name);
            pStmt.setString(3, password);
            pStmt.setInt(4, isDirevative);

            int affectedRows = pStmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}