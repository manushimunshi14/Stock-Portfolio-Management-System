package DatabaseReader;

import Database.DatabaseManager;
import System.Stock.Stock;
import System.Stock.StockMarketHistory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Stockdb {
    private DatabaseReader utility;
    private DatabaseManager dbManager;


    public Stockdb(DatabaseManager dbManager) {
        this.dbManager = dbManager;
        this.utility = new DatabaseReader(dbManager);
    }

    public Stockdb() {
        this.dbManager = new DatabaseManager();
        this.utility = new DatabaseReader(dbManager);
    }

    public void updateStockPrice(int stockId, double newPrice) {
        String query = "UPDATE stockMarket SET last_sale = ?, last_update_time = ? WHERE stock_id = ? ORDER BY last_update_time DESC LIMIT 1";

        try (Connection conn = dbManager.getDbConnector();
             PreparedStatement pStmt = conn.prepareStatement(query)) {

            pStmt.setDouble(1, newPrice);
            pStmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            pStmt.setInt(3, stockId);

            int affectedRows = pStmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating stock price failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Stock> getAllStocks() {
        List<Stock> stocks = new ArrayList<>();
        String query = "SELECT s.id as stock_id, s.symbol, s.name, sm.last_sale, sm.last_update_time " +
                "FROM stocks s " +
                "JOIN (SELECT stock_id, last_sale, last_update_time " +
                "FROM stockMarket sm1 "+
                "WHERE last_update_time = (SELECT MAX(last_update_time) " +
                "FROM stockMarket sm2 WHERE sm1.stock_id = sm2.stock_id)) sm ON s.id = sm.stock_id";
        try (Connection conn = dbManager.getDbConnector();
             PreparedStatement pStmt = conn.prepareStatement(query);
             ResultSet resultSet = pStmt.executeQuery()) {

            while (resultSet.next()) {
                int stockId = resultSet.getInt("stock_id");
                String symbol = resultSet.getString("symbol");
                String name = resultSet.getString("name");
                double currPrice = resultSet.getDouble("last_sale");
                Timestamp lastUpdateTime = resultSet.getTimestamp("last_update_time"); // Fetching the timestamp

                stocks.add(new Stock(stockId, symbol, name, currPrice,lastUpdateTime));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stocks;
    }

    // fetch the whole table
    public List<StockMarketHistory> getStockHistory() {
        List<StockMarketHistory> historyList = new ArrayList<>();
        String query = "SELECT stock_id, last_sale, last_update_time FROM stockMarket";

        try (Connection conn = dbManager.getDbConnector();
             PreparedStatement pStmt = conn.prepareStatement(query);
             ResultSet resultSet = pStmt.executeQuery()) {

            while (resultSet.next()) {
                int stockId = resultSet.getInt("stock_id");
                double lastSale = resultSet.getDouble("last_sale");
                Timestamp lastUpdateTime = resultSet.getTimestamp("last_update_time");

                historyList.add(new StockMarketHistory(stockId, lastSale, lastUpdateTime));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historyList;
    }

    // fetch only one stock
    public List<StockMarketHistory> getStockHistory(int stockId) {
        List<StockMarketHistory> historyList = new ArrayList<>();
        String query = "SELECT stock_id, last_sale, last_update_time FROM stockMarket WHERE stock_id = ?";

        try (Connection conn = dbManager.getDbConnector();
             PreparedStatement pStmt = conn.prepareStatement(query)) {

            pStmt.setInt(1, stockId);

            try (ResultSet resultSet = pStmt.executeQuery()) {
                while (resultSet.next()) {
                    double lastSale = resultSet.getDouble("last_sale");
                    Timestamp lastUpdateTime = resultSet.getTimestamp("last_update_time");

                    historyList.add(new StockMarketHistory(stockId, lastSale, lastUpdateTime));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historyList;
    }


    public void printAllStocks() {
        List<Stock> stocks = getAllStocks();

        for (Stock stock : stocks) {
            System.out.println("Symbol: " + stock.getSymbol() + ", Name: " + stock.getStockName() +
                    ", Last Sale: " + stock.getCurrPrice() + ", Last Update Time: " + stock.getLastUpdateTime());
        }
    }

    public void insertNewStock(String symbol, String name, double price) {
        Connection conn = null;
        PreparedStatement pStmtStock = null;
        PreparedStatement pStmtStockMarket = null;
        ResultSet generatedKeys = null;

        String insertStockQuery = "INSERT INTO stocks (symbol, name) VALUES (?, ?)";
        String insertStockMarketQuery = "INSERT INTO stockMarket (stock_id, last_sale, last_update_time) VALUES (?, ?, ?)";

        try {
            conn = dbManager.getDbConnector();
            conn.setAutoCommit(false);

            // Insert into stocks table
            pStmtStock = conn.prepareStatement(insertStockQuery, Statement.RETURN_GENERATED_KEYS);
            pStmtStock.setString(1, symbol);
            pStmtStock.setString(2, name);
            int affectedRows = pStmtStock.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating stock failed, no rows affected.");
            }

            generatedKeys = pStmtStock.getGeneratedKeys();
            if (generatedKeys.next()) {
                int newStockId = generatedKeys.getInt(1);

                // Insert into stockMarket table
                pStmtStockMarket = conn.prepareStatement(insertStockMarketQuery);
                pStmtStockMarket.setInt(1, newStockId);
                pStmtStockMarket.setDouble(2, price);
                pStmtStockMarket.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                pStmtStockMarket.executeUpdate();
            } else {
                throw new SQLException("Creating stock failed, no ID obtained.");
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (pStmtStock != null) pStmtStock.close();
                if (pStmtStockMarket != null) pStmtStockMarket.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }


    public boolean deleteStock(int stockId) {
        String deleteStockMarketQuery = "DELETE FROM stockMarket WHERE stock_id = ?";
        String deleteStockQuery = "DELETE FROM stocks WHERE id = ?";

        try (Connection conn = dbManager.getDbConnector()) {
            conn.setAutoCommit(false);

            // Delete from stockMarket table
            try (PreparedStatement pStmtMarket = conn.prepareStatement(deleteStockMarketQuery)) {
                pStmtMarket.setInt(1, stockId);
                pStmtMarket.executeUpdate();
            }

            // Delete from stocks table
            try (PreparedStatement pStmtStock = conn.prepareStatement(deleteStockQuery)) {
                pStmtStock.setInt(1, stockId);
                pStmtStock.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
//            e.printStackTrace();
            return false;
        }
    }

}


