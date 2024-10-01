package System.User;

import Database.DatabaseManager;
import DatabaseReader.StockTransactdb;
import DatabaseReader.Stockdb;
import System.Stock.StockManager;
import System.Stock.StockMarketHistory;
import System.Stock.TransactionDetails;

import java.util.ArrayList;
import java.util.List;

public class Admin extends User {
    /*
        Admin should be able to modify all the stock in the market,
        change availability, price, check user detail, get report of the
        stock market (price change in the last 20 days etc.)
     */
    private Stockdb stockdb;
    private StockTransactdb stockTransactdb;
    private StockManager stockManager;

    // Updated constructor
    public Admin(int id, String name, String username, int managerId, int passwordId) {
        super(id, name, username, managerId, passwordId);
        this.stockdb = new Stockdb();
        this.stockTransactdb = new StockTransactdb(new DatabaseManager());
        this.stockManager = new StockManager(stockdb);
    }


    //TODO
    //MANUAL CHANGE PRICE
    //SEE REPORT OF CUSTOMER PROFIT, STOCK PRICE CHANGING


    public List<StockMarketHistory> getStockHistory() {
        return stockdb.getStockHistory();
    }

    // Method to get the stock history for a specific stock
    public List<StockMarketHistory> getStockHistory(int stockId) {
        return stockdb.getStockHistory(stockId);
    }


    public List<CustomerData> getCustomerProfit() {
        List<CustomerData> customerProfits = new ArrayList<>();

        return customerProfits;
    }


    public void printUserTransactions(int userId) {
        List<TransactionDetails> transactions = stockTransactdb.getStocksTransact(userId);

        for (TransactionDetails transact : transactions) {
            System.out.println("User ID: " + transact.getUserId() +
                    ", Stock ID: " + transact.getStockId() +
                    ", Count: " + transact.getCount() +
                    ", Transaction Price: " + transact.getTransactionPrice() +
                    ", Trading Date: " + transact.getTradingDate() +
                    ", Total Consumption: " + transact.getTotalConsumption());
        }
    }


    public List<TransactionDetails> getUserTransactions(int userId) {
        return stockTransactdb.getStocksTransact(userId);
    }

    public void addNewStock(String symbol, String name, double price){
        stockManager.addNewStock(symbol,name,price);
    }

    public void deleteStock(int stockId){
        stockManager.deleteStock(stockId);
    }


    public void updateStockPrice(int stockId, double newPrice) {
        stockManager.updateStockPrice(stockId, newPrice);

    }

}
