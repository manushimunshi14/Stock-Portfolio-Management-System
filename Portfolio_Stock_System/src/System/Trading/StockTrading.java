package System.Trading;

import Database.DatabaseManager;
import DatabaseReader.DatabaseFetcher;
import DatabaseReader.StockTransactdb;
import DatabaseReader.Stockdb;
import System.Account.CustomerAccount;
import System.Portfolio.Portfolio;
import System.Portfolio.ProfitCalculator;
import System.Stock.Stock;
import System.Stock.TransactionDetails;
import System.User.Customer;
import System.User.CustomerManager;

import java.util.List;
import java.util.Scanner;

public class StockTrading {
    private static Scanner scanner = new Scanner(System.in);
    private static CustomerManager customerManager = new CustomerManager();
    private static DatabaseManager dbManager = new DatabaseManager();
    private static StockTransactdb stockTransactdb = new StockTransactdb(dbManager);
    private static Stockdb stockdb = new Stockdb(dbManager);
    private static DatabaseFetcher fetcher = new DatabaseFetcher(dbManager);
    private static ProfitCalculator profitCalculator = new ProfitCalculator();

    public void handleTrading(Customer customer) {
        // Show list of available stocks
        List<Stock> availableStocks = stockdb.getAllStocks();
        for (Stock stock : availableStocks) {
            System.out.println("Symbol: " + stock.getSymbol() + ", Name: " + stock.getStockName() + ", Price: " + stock.getCurrPrice());
        }

        // Get user input for stock to buy
        System.out.println("Enter the symbol of the stock you want to buy:");
        String symbol = scanner.nextLine();

        // Find the chosen stock
        Stock stockToBuy = availableStocks.stream().filter(s -> s.getSymbol().equals(symbol)).findFirst().orElse(null);

        if (stockToBuy == null) {
            System.out.println("Stock not found.");
            return;
        }

        // Get quantity to buy
        System.out.println("How many units of " + symbol + " do you want to buy?");
        int quantityToBuy = scanner.nextInt();
        scanner.nextLine();

        // Perform the buying
        buyStock(customer, stockToBuy, quantityToBuy);
    }

    public static void buyStock(Customer customer, Stock stock, int quantity) {
        double latestPrice = stock.getCurrPrice();
        double totalPurchaseAmount = latestPrice * quantity;

        // Assuming customer has a method to check if they have sufficient balance
        if (!customerManager.validPurchase(customer, stock, quantity)) {
            System.out.println("Insufficient balance to complete this transaction.");
            return;
        }

        // Update customer's portfolio
//        Portfolio portfolio = portfolioManager.getUserPortfolio(customer.getId());
        Portfolio portfolio = customer.getPortfolio();
//        portfolio.getStockTransactHistory(customer.getId());
        portfolio.getStockTransaction(customer.getId());
        portfolio.addStock(customer.getId(), stock, quantity, latestPrice);

        // Update balance and number of stocks owned
        stockTransactdb.recordStockTransaction(customer.getId(), stock.getStockId(), quantity, latestPrice, false);
        stockTransactdb.updateHolding(customer.getId(), stock.getStockId(), quantity, false);

        CustomerAccount customerAccount = customer.getAccount();
        customerAccount.withdraw(totalPurchaseAmount);
        customerAccount.updateAccountBalance(customer.getId());

        System.out.println("Purchased " + quantity + " units of " + stock.getStockName());
        System.out.println("Updated balance: " + customerAccount.getBalance());
    }


    public static void sellStock(Customer customer, Portfolio portfolio, Stock stock, int quantity) {
        List<TransactionDetails> transactions = portfolio.getStockTransaction(customer.getId());
        TransactionDetails targetDetails = null;

        // Find the transaction details for the specific stock
        for (TransactionDetails details : transactions) {
            if (details.getStockId() == stock.getStockId()) {
                targetDetails = details;
                break;
            }
        }

        if (targetDetails != null && targetDetails.getCount() >= quantity) {
            List<Double> prices = fetcher.fetchLatestPrices(stock.getStockId(), 1, "last_sale", "stockMarket", "last_update_time");
            if (!prices.isEmpty() && prices.get(0) > 0) {
                double latestPrice = prices.get(0);
                double totalSaleAmount = latestPrice * quantity;
                portfolio.removeStock(stock, quantity);
                System.out.println("Sold " + quantity + " units of " + stock.getStockName());


                // Update balance and number of stocks owned
                stockTransactdb.recordStockTransaction(customer.getId(), stock.getStockId(), quantity, latestPrice, true);
                stockTransactdb.updateHolding(customer.getId(), stock.getStockId(), quantity, true);

                CustomerAccount customerAccount = customer.getAccount();
                customerAccount.deposit(totalSaleAmount);
                customerAccount.updateAccountBalance(customer.getId());

                double currRealized = customerAccount.getRealizedProfit();

                double newRealizedProfit = currRealized + profitCalculator.calculateRealizedProfit(customer.getId(), latestPrice, stock.getStockId());
                newRealizedProfit = Math.round(newRealizedProfit * 100.0) / 100.0;
                customerAccount.updateRealizedProfit(newRealizedProfit);
                System.out.println("Customer Realized Profit:" + newRealizedProfit);

                // TODO maybe move the notification to ProfitTracker
                if (newRealizedProfit >= 10000) {
                    // Notify user
//                    derivativeOption(customer);
                }

                stock.setCurrPrice(latestPrice);
                System.out.println("Transaction recorded at price: " + stock.getCurrPrice());
                System.out.println("Updated balance: " + customerAccount.getBalance());
            } else {
                System.out.println("Unable to fetch the latest price for " + stock.getSymbol());
            }
        } else {
            System.out.println("Not enough stock to sell. Available quantity: " + (targetDetails != null ? targetDetails.getCount() : 0));
        }
    }




}
