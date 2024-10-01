package System;

import Database.*;
import DatabaseReader.*;
import GUI.Utils.*;
import System.Portfolio.*;
import System.Stock.*;
import System.User.*;

import java.util.List;
import java.util.Scanner;

public class TestStockSystem {
    private static Scanner scanner = new Scanner(System.in);
    private static CustomerManager customerManager = new CustomerManager();
    private static ProfitCalculator profitCalculator = new ProfitCalculator();
    private static TimerManager timerManager = new TimerManager();
    private static RandomGenerator randomGenerator = new RandomGenerator();
    private static DatabaseManager dbManager = new DatabaseManager();
    private static StockTransactdb stockTransactdb = new StockTransactdb(dbManager);
    private static Stockdb stockdb = new Stockdb(dbManager);
    private static PortfolioManager portfolioManager = new PortfolioManager(stockdb);
    private static DatabaseFetcher fetcher = new DatabaseFetcher(dbManager);

    private static StockPriceChanger priceChanger = new StockPriceChanger(customerManager, profitCalculator, timerManager, randomGenerator, portfolioManager);


    public static void printStockMarketHistory() {
        List<StockMarketHistory> historyList = stockdb.getStockHistory(1);

        for (StockMarketHistory history : historyList) {
            System.out.println("Stock ID: " + history.getStockId() +
                    ", Last Sale: " + history.getLastSale() +
                    ", Last Update Time: " + history.getLastUpdateTime());
        }
    }


    public static void main(String[] args) {
        Customer user = (Customer) portfolioManager.getUser(2);
//        portfolioManager.loadAndAssignPortfolios();
        priceChanger.stockPriceChange();
//        startTrading(portfolioManager);
        scanner.close();

//        Admin admin = new Admin(1, "Admin", "Admin1", 1, 1);
//        stockdb.deleteStock(6);

    }


}
