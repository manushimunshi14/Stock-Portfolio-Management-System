package DatabaseReader;

import Database.DatabaseManager;
import System.User.Admin;
import System.User.Customer;
import System.User.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Userdb {
    private DatabaseManager dbManager;
    private DatabaseReader utility;
    private DatabaseFetcher fetcher;

    public Userdb(DatabaseManager dbManager) {
        this.dbManager = dbManager;
        this.fetcher = new DatabaseFetcher(this.dbManager );
        this.utility = new DatabaseReader(this.dbManager);
    }

    public List<User> getUserList() {
        String query = "SELECT u.id, u.user_name, u.display_name, u.manager_id, u.password_id, m.manager " +
                "FROM user u " +
                "JOIN manager m ON u.manager_id = m.id";
        List<Map<String, Object>> result = utility.getAllRows(query);
        List<User> users = new ArrayList<>();
        for (Map<String, Object> row : result) {
            users.add(createNewUser(row));
        }
        return users;
    }

    public User createNewUser(Map<String, Object> row) {
        int userId = ((Long) row.get("id")).intValue();
        boolean isManager = (Boolean) row.get("manager");
        String name = (String) row.get("display_name");
        String username = (String) row.get("user_name");
        int managerId = ((Long) row.get("manager_id")).intValue();
        int passwordId = ((Long) row.get("password_id")).intValue();

        if (isManager) {
            return new Admin(userId, name, username, managerId, passwordId);
        } else {
            return new Customer(userId, name, username, managerId, passwordId);
        }
    }

    public Map<String, Object> getUserDetails(int userId) {
        String query = "SELECT u.id, u.user_name, u.display_name, u.manager_id, u.password_id, m.manager " +
                "FROM user u JOIN manager m ON u.manager_id = m.id WHERE u.id = ?";
        return fetcher.getSingleRow(query, userId,dbManager);
    }


    public Customer getCustomerById(int userId) {
        String query = "SELECT u.id, u.user_name, u.display_name, u.manager_id, u.password_id, m.manager " +
                "FROM user u JOIN manager m ON u.manager_id = m.id WHERE u.id = ?";

        Map<String, Object> userDetails = fetcher.getSingleRow(query, userId, dbManager);

        if (userDetails != null && !userDetails.isEmpty()) {
            boolean isManager = (Boolean) userDetails.get("manager");
            if (!isManager) {
                return (Customer) createNewUser(userDetails);
            }
        }
        return null;
    }

    public List<Customer> getCustomerList() {
        List<User> users = getUserList();
        List<Customer> customerList = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Customer) {
                customerList.add((Customer) user);
            }

        }
        return customerList;
    }

    public void printUserList() {
        List<User> users = getUserList();
        for (User customer : users) {
            if (customer instanceof Admin) {
                Admin admin = (Admin) customer;
                System.out.println("Admin Name: " + admin.getName() +
                        ", Username: " + admin.getUsername() +
                        ", ID: " + admin.getId()
                );
            } else if (customer instanceof Customer) {
                Customer customerUser = (Customer) customer;
                System.out.println("Customer Name: " + customerUser.getName() +
                        ", Username: " + customerUser.getUsername() +
                        ", ID: " + customerUser.getId()
                );
            }
        }
    }

    public String getUserType(int userId) {
        String query = "SELECT m.manager FROM user u JOIN manager m ON u.manager_id = m.id WHERE u.id = ?";
        Map<String, Object> result = fetcher.getSingleRow(query, userId,dbManager);
        if (result != null && !result.isEmpty()) {
            boolean isManager = (Boolean) result.get("manager");
            return isManager ? "Admin" : "Customer";
        }
        return null;
    }



    public List<Integer> getAllUserIds() {
        List<Integer> userIds = new ArrayList<>();
        String query = "SELECT DISTINCT user_id FROM purchasedstocks";

        try (Connection conn = dbManager.getDbConnector();
             PreparedStatement pStmt = conn.prepareStatement(query);
             ResultSet resultSet = pStmt.executeQuery()) {

            while (resultSet.next()) {
                userIds.add(resultSet.getInt("user_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userIds;
    }

    public boolean getDerivativeValue(int userId) {
        String query = "SELECT derivative FROM derivative_user WHERE user_id = ?";
        try (Connection conn = dbManager.getDbConnector();
             PreparedStatement pStmt = conn.prepareStatement(query)) {

            pStmt.setInt(1, userId);

            try (ResultSet resultSet = pStmt.executeQuery()) {
                if (resultSet.next()) {
                    int result = resultSet.getInt("derivative");
                    return result == 1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void main(String[] args) {
        DatabaseManager dbManager = new DatabaseManager();
        Userdb userdb = new Userdb(dbManager);

        userdb.printUserList();
    }




}
