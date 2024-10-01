package Services;

import java.sql.*;

import Database.DatabaseManager;
import PasswordAuthentication.PasswordAuthentication;
import System.Account.LoginResult;

public class AuthenticationService {

  public static boolean signup(String username, String password, String displayName) {
    return isUsernameUnique(username) && isDisplayNameValid(displayName) && isPasswordValid(password);
  }

  public static boolean login(String username, String password) {
    DatabaseManager dbManager = new DatabaseManager();
    try {
      String query = "SELECT p.password FROM user u JOIN password p ON u.password_id = p.id WHERE u.user_name = ?";
      return dbManager.executeQuery(query, rs -> {
        if (rs.next()) {
          String storedPassword = rs.getString("password");
          PasswordAuthentication passwordAuth = new PasswordAuthentication();
          return passwordAuth.authenticate(password.toCharArray(), storedPassword);
        }
        return false;
      }, username);
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    } finally {
      dbManager.closeConnection();
    }
  }

  public static boolean isManager(int managerId) {
    DatabaseManager dbManager = new DatabaseManager();
    String query = "SELECT manager FROM manager WHERE id = ?";

    try {
      return dbManager.executeQuery(query, rs -> rs.next() && rs.getBoolean("manager"), managerId);
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    } finally {
      dbManager.closeConnection();
    }
  }

  public static int getUserId(String username) {
    DatabaseManager dbManager = new DatabaseManager();
    String query = "SELECT id FROM user WHERE user_name = ?";
    try {
      return dbManager.executeQuery(query, rs -> rs.next() ? rs.getInt("id") : 0, username);
    } catch (SQLException e) {
      e.printStackTrace();
      return 0;
    } finally {
      dbManager.closeConnection();
    }
  }

  public static String getDisplayName(String username) {
    DatabaseManager dbManager = new DatabaseManager();
    String query = "SELECT display_name FROM user WHERE user_name = ?";
    try {
      return dbManager.executeQuery(query, rs -> rs.next() ? rs.getString("display_name") : "", username);
    } catch (SQLException e) {
      e.printStackTrace();
      return "";
    } finally {
      dbManager.closeConnection();
    }
  }

  public static boolean isUsernameUnique(String username) {
    DatabaseManager dbManager = new DatabaseManager();


    try {
      String query = "SELECT COUNT(*) FROM user WHERE user_name = ?";
      boolean isUsernameUniqueInUserTable = dbManager.executeQuery(query, rs -> rs.next() && rs.getInt(1) == 0, username);
      query = "SELECT COUNT(*) FROM request WHERE user_name = ?";
      boolean isUsernameUniqueInRequestTable = dbManager.executeQuery(query, rs -> rs.next() && rs.getInt(1) == 0, username);
      return isUsernameUniqueInUserTable && isUsernameUniqueInRequestTable;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    } finally {
      dbManager.closeConnection();
    }
  }

  public static double getBalance(int userId) {
    DatabaseManager dbManager = new DatabaseManager();
    String query = "SELECT balance FROM wallet WHERE user_id = ?";
    try {
      return dbManager.executeQuery(query, rs -> rs.next() ? rs.getDouble("balance") : 0.0, userId);
    } catch (SQLException e) {
      e.printStackTrace();
      return 0.0;
    } finally {
      dbManager.closeConnection();
    }
  }

  public static LoginResult getLoginResult(String username) {
    DatabaseManager dbManager = new DatabaseManager();
    String query = "SELECT u.display_name, u.id AS user_id, u.password_id, u.manager_id " +
        "FROM user u " +
        "WHERE u.user_name = ?";

    try {
      // Execute the query and process the result set
      return dbManager.executeQuery(query, rs -> {
        if (rs.next()) {
          String displayName = rs.getString("display_name");
          int userId = rs.getInt("user_id");
          int passwordId = rs.getInt("password_id");
          int managerId = rs.getInt("manager_id");

          return new LoginResult(displayName, userId, passwordId, managerId);
        } else {
          // Handle the case where no user is found
          return null;
        }
      }, username);
    } catch (SQLException e) {
      System.out.println("SQLException: " + e.getMessage());
      System.out.println("SQLState: " + e.getSQLState());
      System.out.println("VendorError: " + e.getErrorCode());
      // Handle the exception as per your application's requirement
      return null;
    } finally {
      dbManager.closeConnection();
    }
  }

  public static boolean isUsernameValid(String username) {
    if (username.length() > 15) {
      return false;
    }
    return username.matches("[A-Za-z0-9]+");
  }


  public static boolean isDisplayNameValid(String displayName) {
    if (displayName.length() > 25) {
      return false;
    }
    return displayName.matches("[A-Za-z _.]+");
  }

  public static boolean isPasswordValid(String password) {
  if (password == null || password.length() < 6) {
        return false;
    }

    boolean hasUpper = password.matches(".*[A-Z].*");
    boolean hasLower = password.matches(".*[a-z].*");
    boolean hasDigit = password.matches(".*\\d.*");
    boolean hasSpecial = password.matches(".*[^a-zA-Z\\d].*");

    return hasUpper && hasLower && hasDigit && hasSpecial;
  }

  public static String hashPassword(String password) {
    // Use the PasswordAuthentication class to hash passwords.
    PasswordAuthentication passwordAuth = new PasswordAuthentication();
    passwordAuth = new PasswordAuthentication();
    return passwordAuth.hash(password.toCharArray());
  }
}
