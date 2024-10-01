package System.Portfolio;

import Database.DatabaseManager;
import DatabaseReader.DatabaseFetcher;
import DatabaseReader.StockTransactdb;
import System.Account.StockHolding;
import System.Stock.Stock;
import System.Stock.StockPurchaseDetails;
import System.Stock.TransactionDetails;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Set;

public class Portfolio {
    private DatabaseManager dbManager = new DatabaseManager();
    private DatabaseFetcher fetcher;
    private List<TransactionDetails> stockTransactHistory;
    private StockTransactdb transactdb = new StockTransactdb(dbManager);
    private List<StockHolding> stockOwned;

    public Portfolio() {
        stockTransactHistory = new ArrayList<>();
        stockOwned = new ArrayList<>();
    }


    public Portfolio(int userId) {
        this.fetcher = new DatabaseFetcher(dbManager);
        this.stockOwned = fetcher.fetchStockHoldings(userId);
    }

    public void addStock(int userId, Stock stock, int quantity, double purchasePrice) {
        Timestamp tradingDate = new Timestamp(System.currentTimeMillis());
        double totalConsumption = quantity * purchasePrice;

        TransactionDetails newTransaction = new TransactionDetails(userId, stock.getStockId(), quantity, stock.getSymbol(),purchasePrice, tradingDate, totalConsumption);

        stockTransactHistory.add(newTransaction);
    }

    public void removeStock(Stock stock, int quantity) {
        boolean stockFound = false;
        for (StockHolding holding : stockOwned) {
            if (holding.getStockId() == stock.getStockId()) {
                stockFound = true;
                int currentQuantity = holding.getCount();
                int updatedQuantity = currentQuantity - quantity;

                if (updatedQuantity <= 0) {
                    stockOwned.remove(holding);
                } else {
                    holding.setCount(updatedQuantity);
                }
                break;
            }
        }
        if (!stockFound) {
            System.out.println("Stock not found in customer portfolio");
        }
    }

//    public Map<Stock, StockPurchaseDetails> getStockTransactHistory(int userId) {
//        stockTransactHistory = PortfolioManager.loadUserPurchasedStocks(userId);
//
//        return stockTransactHistory;
//    }

    public List<TransactionDetails> getStockTransaction(int userId){
        stockTransactHistory = transactdb.getStocksTransact(userId);
        return stockTransactHistory;
    }

    public List<Stock> getPurchasedStock(int userId) {
        this.stockOwned = fetcher.fetchStockHoldings(userId);
        List<Stock> purchasedStock = new ArrayList<>();

        for (StockHolding holding : stockOwned) {
            Stock stock = new Stock(holding.getStockId(), holding.getSymbol(), holding.getName());
            purchasedStock.add(stock);
        }
        return purchasedStock;
    }

    public int getPurchasedStockCount(int stockId) {
        int totalCount = 0;

        for (StockHolding holding : stockOwned) {
            if (holding.getStockId() == stockId) {
                totalCount += holding.getCount();
            }
        }
        return totalCount;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Customer Portfolio:\n");
        for (TransactionDetails transaction : stockTransactHistory) {
            stringBuilder.append("Stock ID: ").append(transaction.getStockId())
                    .append(", Purchase Price: ").append(transaction.getTransactionPrice())
                    .append(", Quantity: ").append(transaction.getCount())
                    .append(", Trading Date: ").append(transaction.getTradingDate())
                    .append(", Total Consumption: ").append(transaction.getTotalConsumption())
                    .append("\n");
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        // 创建 Portfolio 实例
        Portfolio portfolio = new Portfolio(2); // 假设 2 是用户 ID
//        portfolio.getStockTransactHistory(2);
        portfolio.getStockTransaction(2);
        // 获取并打印 allStocksPurchased 中的内容
        List<Stock> stocksList = portfolio.getPurchasedStock(2);
        for (Stock eStock : stocksList) {
            System.out.println(eStock.toString());
        }

            System.out.println(portfolio.getPurchasedStockCount(1));
    }

}

