package System.Stock;

import Database.DatabaseManager;
import DatabaseReader.Stockdb;
import DatabaseReader.Userdb;
import GUI.Utils.RandomGenerator;
import GUI.Utils.TimerManager;
import System.Portfolio.PortfolioManager;
import System.Portfolio.ProfitCalculator;
import System.User.Customer;
import System.User.CustomerManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class StockPriceChanger {
    private TimerManager timerManager;
    private RandomGenerator randomGenerator;
    private CustomerManager customerManager;
    private ProfitCalculator profitCalculator;
    private PortfolioManager portfolioManager;
    private DatabaseManager dbManager = new DatabaseManager();
    private Stockdb stockdb = new Stockdb(dbManager);
    private Userdb userdb = new Userdb(dbManager);
    private static final long DURATION = 300000;

    public StockPriceChanger(CustomerManager customerManager, ProfitCalculator profitCalculator, TimerManager timerManager, RandomGenerator randomGenerator, PortfolioManager portfolioManager) {
        this.timerManager = timerManager;
        this.randomGenerator = randomGenerator;
        this.customerManager = customerManager;
        this.profitCalculator = profitCalculator;
        this.portfolioManager = portfolioManager;
    }

    // Change price of the stock in the market every duration, and update customer unrealized profit
    public void stockPriceChange() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                List<Stock> allStocks = stockdb.getAllStocks();
                Set<Integer> affectedCustomer = new HashSet<>();
                for (Stock stock : allStocks) {
                    double prePrice = stock.getCurrPrice();
                    double change = randomGenerator.generateRandom(prePrice, 0.00, 0.10);

                    double newPrice = prePrice + change;
                    newPrice = Math.round(newPrice * 100.0) / 100.0;
                    double percentChange = ((newPrice - prePrice) / prePrice) * 100;
                    System.out.println("Updated stock price: " + stock.getSymbol() + " - "
                            + newPrice + " change: " + Math.round(percentChange * 100.0) / 100.0);

                    insertNewPriceInDatabase(stock.getStockId(), newPrice);
                    affectedCustomer.addAll(getCustHoldingStock(stock.getStockId()));
                }
                //TODO maybe move the update to when clicking profit button
//                 Only update customers that hold stock
                for (Integer customerId : affectedCustomer) {
                    updateCustomerUnrealizedProfit(customerId);
                }
            }
        };
        timerManager.setChangeRate(task, 0, DURATION);
    }

    public void stopStockPriceChange() {
        timerManager.stopTimer();
    }

    private void insertNewPriceInDatabase(int stockId, double newPrice) {
        String insertQuery = "INSERT INTO stockMarket (stock_id, last_sale, last_update_time) VALUES (?, ?, ?)";

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = dateFormat.format(new Date());

            dbManager.executeUpdate(insertQuery, false, stockId, newPrice, currentTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Set<Integer> getCustHoldingStock(int stockId) {
        Set<Integer> customerIds = new HashSet<>();
        String query = "SELECT user_id FROM purchasedstocks WHERE stock_id = ?";

        try (Connection conn = dbManager.getDbConnector();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, stockId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int userId = rs.getInt("user_id");
                    customerIds.add(userId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customerIds;
    }

    public void updateCustomerUnrealizedProfit(int customerId) {
        Customer customer = userdb.getCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found for ID: " + customerId);
            return;
        }

        double newUnrealizedProfit = profitCalculator.calculateUnrealizedProfit(customer);
        newUnrealizedProfit = Math.round(newUnrealizedProfit * 100.0) / 100.0;
        customer.getAccount().updateUnrealizedProfit(newUnrealizedProfit);

        System.out.println("Customer ID: " + customer.getId() + ", Updated Unrealized Profit: " + newUnrealizedProfit);
    }

}
