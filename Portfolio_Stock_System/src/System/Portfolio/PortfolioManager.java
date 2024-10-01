package System.Portfolio;

import Database.DatabaseManager;
import DatabaseReader.*;
import System.Account.StockHolding;
import System.Stock.Stock;
import System.Stock.StockPurchaseDetails;
import System.User.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import java.sql.*;

public class PortfolioManager {

    private Map<Integer, User> users;
    private Map<User, Portfolio> userPortfolios;
    private Stockdb stockdb;
    private DatabaseManager dbManager = new DatabaseManager();
    private Userdb userdb = new Userdb(dbManager);
    private DatabaseFetcher fetcher = new DatabaseFetcher(dbManager);
    private Map<Integer, Stock> stockCache;
    private Map<List<Stock>, Integer> allStocksPurchased;

    public PortfolioManager(Stockdb stockdb) {
        this.stockdb = stockdb;
        this.users = new HashMap<>();
        this.userPortfolios = new HashMap<>();
        this.stockCache = new HashMap<>();
        loadAllStocks();
    }

    public PortfolioManager() {
        this.stockdb = new Stockdb();
        this.users = new HashMap<>();
        this.userPortfolios = new HashMap<>();
        this.stockCache = new HashMap<>();
        loadAllStocks();
    }

    public static Map<Stock, StockPurchaseDetails> loadUserPurchasedStocks(int userId) {
        Map<Stock, StockPurchaseDetails> stocks = new HashMap<>();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection conn = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;

        try {
            conn = databaseManager.getDbConnector();
            String sql = "SELECT s.id, s.symbol, s.name, spr.count,transaction_price, spr.trading_date FROM stockspurchasedrecord spr JOIN stocks s ON spr.stock_id = s.id WHERE spr.user_id = ?";

            pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, userId);
            rs = pStmt.executeQuery();

            while (rs.next()) {
                int stockId = rs.getInt("id");
                String symbol = rs.getString("symbol");
                String name = rs.getString("name");
                double lastSale = rs.getDouble("transaction_price");
                int count = rs.getInt("count");
                Timestamp lastUpdateTime = rs.getTimestamp("trading_date");
                stocks.put(new Stock(stockId, symbol, name, lastSale, lastUpdateTime),
                        new StockPurchaseDetails(count, lastSale));
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        } finally {
            databaseManager.closeConnection();
        }
        return stocks;
    }

    public Map<List<Stock>, Integer> getAllStocksPurchased() {
        return allStocksPurchased;
    }

    private void loadAllStocks() {
        List<Stock> stocks = stockdb.getAllStocks();
        for (Stock stock : stocks) {
            stockCache.put(stock.getStockId(), stock);
        }
    }

//    public void loadAndAssignPortfolios() {
//        Map<Integer,Double> allLatestPrices = fetcher.fetchAllLatestPrices("transaction_price", "stockspurchasedrecord", "trading_date");
//        List<Integer> userIds = userdb.getAllUserIds();
//
//        for (int userId : userIds) {
//            List<StockHolding> stockHoldings = fetcher.fetchStockHoldings(userId);
//            for (StockHolding holding : stockHoldings) {
//                Customer user = (Customer) getUser(userId);
//                int stockId = holding.getStockId();
//                Stock stock = stockCache.get(stockId);
//                if (stock == null) {
//                    continue;
//                }
//                int quantity = holding.getSellingCount();
//                double purchasePrice = allLatestPrices.getOrDefault(stockId, 0.0);
//                Portfolio portfolio = userPortfolios.computeIfAbsent(user, k -> new Portfolio());
//                portfolio.addStock(stock, quantity, purchasePrice);
//                user.setPortfolio(portfolio);
//
//                System.out.println("Added stock: " + stock.getStockName() + " to user ID: "
//                        + userId + " with quantity " + quantity + " and price: " + purchasePrice);
//            }
//        }
//    }

    public User getUser(int userId) {
        User user = users.get(userId);
        if (user == null) {
            String userType = getUserType(userId);
            Map<String, Object> userDetails = userdb.getUserDetails(userId);
            String name = (String) userDetails.get("display_name");
            String username = (String) userDetails.get("user_name");
            int managerId = ((Long) userDetails.get("manager_id")).intValue();
            int passwordId = ((Long) userDetails.get("password_id")).intValue();

            if ("Admin".equals(userType)) {
                user = new Admin(userId, name, username, managerId, passwordId);
            } else {
                user = new Customer(userId, name, username, managerId, passwordId);
            }
            users.put(userId, user);
        }
        return user;
    }

    public Map<User, Portfolio> getAllUserPortfolios() {
        return userPortfolios;
    }

    public Portfolio getUserPortfolio(int userId) {
        User user = users.get(userId);
        return userPortfolios.get(user);
    }

    public String getUserType(int userId) {
        return userdb.getUserType(userId);
    }


}

