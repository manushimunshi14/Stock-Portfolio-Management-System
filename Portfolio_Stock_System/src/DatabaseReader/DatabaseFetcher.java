package DatabaseReader;

import Database.DatabaseManager;
import System.Account.StockHolding;
import System.Stock.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseFetcher {
    private DatabaseManager dbManager;

    public DatabaseFetcher(DatabaseManager dbManager){
        this.dbManager = dbManager;
    }

    public List<Double> fetchLatestPrices(int stockId, int number,String selectColumn,String tableName, String orderBy) {
        List<Double> prices = new ArrayList<>();
        String query = "SELECT " + selectColumn + " FROM " + tableName +" WHERE stock_id = ? ORDER BY " + orderBy + " DESC LIMIT " + number;

        try (Connection conn = dbManager.getDbConnector();
             PreparedStatement pStmt = conn.prepareStatement(query)) {

            pStmt.setInt(1, stockId);

            try (ResultSet resultSet = pStmt.executeQuery()) {
                while (resultSet.next()) {
                    prices.add(resultSet.getDouble(selectColumn));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prices;
    }


    public Map<Integer, Double> fetchAllLatestPrices(String selectColumn, String tableName, String orderBy) {
        Map<Integer, Double> allLatestPrices = new HashMap<>();

        // Construct a query to fetch the latest price for each stock
        String query = "SELECT stock_id, MAX(" + selectColumn + ") as latest_price FROM " + tableName + " GROUP BY stock_id ORDER BY " + orderBy;

        try (Connection conn = dbManager.getDbConnector();
             PreparedStatement pStmt = conn.prepareStatement(query);
             ResultSet resultSet = pStmt.executeQuery()) {

            while (resultSet.next()) {
                int stockId = resultSet.getInt("stock_id");
                double latestPrice = resultSet.getDouble("latest_price");
                allLatestPrices.put(stockId, latestPrice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allLatestPrices;
    }



    public double fetchWalletBalance(int userId) {
        String query = "SELECT balance FROM wallet WHERE user_id = ?";
        double balance = 0.0;

        try (Connection conn = dbManager.getDbConnector();
             PreparedStatement pStmt = conn.prepareStatement(query)) {

            pStmt.setInt(1, userId);

            try (ResultSet resultSet = pStmt.executeQuery()) {
                if (resultSet.next()) {
                    balance = resultSet.getDouble("balance");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balance;
    }

    public List<StockHolding> fetchStockHoldings(int userId) {
        List<StockHolding> stockHoldings = new ArrayList<>();
        String query = "SELECT s.id, s.symbol, s.name, ps.selling_count FROM purchasedstocks ps JOIN stocks s ON ps.stock_id = s.id WHERE ps.user_id = ?";

        try (Connection conn = dbManager.getDbConnector();
             PreparedStatement pStmt = conn.prepareStatement(query)) {

            pStmt.setInt(1, userId);

            try (ResultSet resultSet = pStmt.executeQuery()) {
                while (resultSet.next()) {
                    int stockId = resultSet.getInt("id");
                    String symbol = resultSet.getString("symbol");
                    String name = resultSet.getString("name");
                    int sellingCount = resultSet.getInt("selling_count");

                    StockHolding holding = new StockHolding(stockId, symbol, name,sellingCount);
                    stockHoldings.add(holding);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stockHoldings;
    }

    public Stock getStockById(int stockId) {
        String query = "SELECT s.id as stock_id, s.symbol, s.name, sm.last_sale, sm.last_update_time " +
                "FROM stocks s " +
                "JOIN (SELECT stock_id, last_sale, last_update_time " +
                "FROM stockMarket sm1 " +
                "WHERE last_update_time = (SELECT MAX(last_update_time) " +
                "FROM stockMarket sm2 WHERE sm1.stock_id = sm2.stock_id)) sm ON s.id = sm.stock_id " +
                "WHERE s.id = ?";
        try (Connection conn = dbManager.getDbConnector();
             PreparedStatement pStmt = conn.prepareStatement(query)) {

            pStmt.setInt(1, stockId); // Set the stockId in the query

            try (ResultSet resultSet = pStmt.executeQuery()) {
                if (resultSet.next()) {
                    String symbol = resultSet.getString("symbol");
                    String name = resultSet.getString("name");
                    double currPrice = resultSet.getDouble("last_sale");
                    Timestamp lastUpdateTime = resultSet.getTimestamp("last_update_time");

                    return new Stock(stockId, symbol, name, currPrice, lastUpdateTime);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Return null if stock not found
    }


    public Map<String, Object> getSingleRow(String query, int userId,DatabaseManager dbManager) {
        Map<String, Object> rowData = new HashMap<>();
        try (Connection connection = dbManager.getDbConnector();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        rowData.put(metaData.getColumnName(i), resultSet.getObject(i));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowData;
    }

}
