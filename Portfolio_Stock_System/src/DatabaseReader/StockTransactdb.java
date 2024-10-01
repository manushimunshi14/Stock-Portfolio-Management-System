package DatabaseReader;

import Database.DatabaseManager;
import System.Portfolio.Portfolio;
import System.Stock.TransactionDetails;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;


public class StockTransactdb {
    private DatabaseManager dbManager;

    public StockTransactdb(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public List<TransactionDetails> getStocksTransact() {
        List<TransactionDetails> transactions = new ArrayList<>();
        String query = "SELECT sr.user_id, sr.stock_id, sr.count, sr.transaction_price, sr.trading_date, sr.total_consumption, s.symbol " +
                "FROM stockspurchasedrecord sr " +
                "JOIN stocks s ON sr.stock_id = s.id " +
                "JOIN user u ON sr.user_id = u.id ";

        try (Connection conn = dbManager.getDbConnector();
             PreparedStatement pStmt = conn.prepareStatement(query);
             ResultSet resultSet = pStmt.executeQuery()) {

            while (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                int stockId = resultSet.getInt("stock_id");
                int count = resultSet.getInt("count");
                String symbol = resultSet.getString("symbol");
                double transactionPrice = resultSet.getDouble("transaction_price");
                Timestamp tradingDate = resultSet.getTimestamp("trading_date");
                double totalConsumption = resultSet.getDouble("total_consumption");

                transactions.add(new TransactionDetails(userId, stockId, count,symbol,
                        transactionPrice, tradingDate,
                        totalConsumption));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }


    public List<TransactionDetails> getStocksTransact(int userId) {
        List<TransactionDetails> transactions = new ArrayList<>();
        String query = "SELECT sr.user_id, sr.stock_id, sr.count, sr.transaction_price, sr.trading_date, sr.total_consumption, s.symbol " +
                "FROM stockspurchasedrecord sr " +
                "JOIN stocks s ON sr.stock_id = s.id " +
                "JOIN user u ON sr.user_id = u.id " +
                "WHERE sr.user_id = ?";

        try (Connection conn = dbManager.getDbConnector();
             PreparedStatement pStmt = conn.prepareStatement(query)) {

            pStmt.setInt(1, userId); // Set the user ID

            try (ResultSet resultSet = pStmt.executeQuery()) {
                while (resultSet.next()) {
                    int stockId = resultSet.getInt("stock_id");
                    int count = resultSet.getInt("count");
                    String symbol = resultSet.getString("symbol");
                    double transactionPrice = resultSet.getDouble("transaction_price");
                    Timestamp tradingDate = resultSet.getTimestamp("trading_date");
                    double totalConsumption = resultSet.getDouble("total_consumption");

                    transactions.add(new TransactionDetails(userId, stockId, count,symbol,
                            transactionPrice, tradingDate,
                            totalConsumption));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }


    public void recordStockTransaction(int userId, int stockId, int quantity, double transactionPrice, boolean isSell) {
        String query = "INSERT INTO stockspurchasedrecord (user_id, stock_id, count, transaction_price, trading_date, total_consumption) VALUES (?, ?, ?, ?, ?, ?)";

        int maxQuantity = Integer.MAX_VALUE;
        if (Math.abs(quantity) > maxQuantity) {
            return;
        }

        try (Connection conn = dbManager.getDbConnector();
             PreparedStatement pStmt = conn.prepareStatement(query)) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = dateFormat.format(new Date());
            int amount = isSell ? -quantity : quantity;
            double totalConsumption = transactionPrice * quantity * (isSell ? 1 : -1);

            pStmt.setInt(1, userId);
            pStmt.setInt(2, stockId);
            pStmt.setInt(3, amount);
            pStmt.setDouble(4, transactionPrice);
            pStmt.setString(5, currentTime);
            pStmt.setDouble(6, totalConsumption);

            pStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateHolding(int userId, int stockId, int quantity, boolean isSell) {
        String checkQuery = "SELECT selling_count FROM purchasedstocks WHERE user_id = ? AND stock_id = ?";
        String updateQuery = "UPDATE purchasedstocks SET selling_count = ? WHERE user_id = ? AND stock_id = ?";
        String insertQuery = "INSERT INTO purchasedstocks (user_id, stock_id, selling_count) VALUES (?, ?, ?)";
        String deleteQuery = "DELETE FROM purchasedstocks WHERE user_id = ? AND stock_id = ?";

        try (Connection conn = dbManager.getDbConnector();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {

            checkStmt.setInt(1, userId);
            checkStmt.setInt(2, stockId);

            try (ResultSet resultSet = checkStmt.executeQuery()) {
                if (resultSet.next()) {
                    int currentCount = resultSet.getInt("selling_count");
                    int newCount = isSell ? currentCount - quantity : currentCount + quantity;

                    if (newCount == 0) {
                        // If the count is zero, delete the record
                        try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
                            deleteStmt.setInt(1, userId);
                            deleteStmt.setInt(2, stockId);
                            deleteStmt.executeUpdate();
                        }
                    } else {
                        // Otherwise, update the record
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                            updateStmt.setInt(1, newCount);
                            updateStmt.setInt(2, userId);
                            updateStmt.setInt(3, stockId);
                            updateStmt.executeUpdate();
                        }
                    }
                } else if (!isSell) {
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                        insertStmt.setInt(1, userId);
                        insertStmt.setInt(2, stockId);
                        insertStmt.setInt(3, quantity);
                        insertStmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }





}

