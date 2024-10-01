package DatabaseReader;

import Database.DatabaseManager;
import System.Portfolio.ProfitDetails;

import java.sql.*;

public class Profitdb {
    private DatabaseManager dbManager;

    public Profitdb(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    // Method to insert or update profit data for a user
    public boolean updateRealizedProfit(int userId, double realizedProfit) {
        String query = "UPDATE customer_profit SET realizedProfit = ? WHERE user_id = ?";

        try (Connection conn = dbManager.getDbConnector();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDouble(1, realizedProfit);
            pstmt.setInt(2, userId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            return false;
        }
    }

    public boolean updateUnrealizedProfit(int userId, double unrealizedProfit) {
        String query = "UPDATE customer_profit SET unrealizedProfit = ? WHERE user_id = ?";

        try (Connection conn = dbManager.getDbConnector();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDouble(1, unrealizedProfit);
            pstmt.setInt(2, userId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            return false;
        }
    }



    public ProfitDetails getUserProfit(int userId) {
        String query = "SELECT realizedProfit, unrealizedProfit FROM customer_profit WHERE user_id = ?";

        try (Connection conn = dbManager.getDbConnector();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    double realizedProfit = rs.getDouble("realizedProfit");
                    double unrealizedProfit = rs.getDouble("unrealizedProfit");
                    return new ProfitDetails(userId, realizedProfit, unrealizedProfit);
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return null;
    }

    public Double getRealizedProfit(int userId) {
        String query = "SELECT realizedProfit FROM customer_profit WHERE user_id = ?";

        try (Connection conn = dbManager.getDbConnector();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("realizedProfit");
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return null;
    }


    public Double getUnrealizedProfit(int userId) {
        String query = "SELECT unrealizedProfit FROM customer_profit WHERE user_id = ?";

        try (Connection conn = dbManager.getDbConnector();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("unrealizedProfit");
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return null;
    }

}

