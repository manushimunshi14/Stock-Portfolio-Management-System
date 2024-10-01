package System.Portfolio;

import Database.DatabaseManager;
import DatabaseReader.DatabaseFetcher;
import DatabaseReader.StockTransactdb;
import System.Account.StockHolding;
import System.Stock.TransactionDetails;
import System.User.Customer;

import java.util.List;

public class ProfitCalculator {
    /*
        Calculate unrealized and realized profit
     */

    DatabaseManager dbManager = new DatabaseManager();
    DatabaseFetcher fetcher = new DatabaseFetcher(dbManager);
    StockTransactdb stockTransactdb = new StockTransactdb(dbManager);

    public double calculateRealizedProfit(int userId, double currentPrice, int stockId) {
        double realizedProfit = 0;
        List<TransactionDetails> transactionDetails = stockTransactdb.getStocksTransact(userId);

        // Filter and find the latest transaction for the specified stockId
        TransactionDetails latestTransaction = null;
        for (TransactionDetails detail : transactionDetails) {
            if (detail.getStockId() == stockId && detail.getCount() > 0) {
                if (latestTransaction == null || detail.getTradingDate().after(latestTransaction.getTradingDate())) {
                    latestTransaction = detail;
                }
            }
        }

        // Calculate profit for the latest transaction
        if (latestTransaction != null) {
            int quantity = latestTransaction.getCount();
            double sellingPrice = latestTransaction.getTransactionPrice();
            realizedProfit = (currentPrice - sellingPrice) * quantity;
        }

        return realizedProfit;
    }

    public double calculateUnrealizedProfit(Customer customer) {
        double unrealizedProfit = 0;
        List<TransactionDetails> transactHistory = customer.getPortfolio().getStockTransaction(customer.getId());
        List<StockHolding> stockHoldings = fetcher.fetchStockHoldings(customer.getId());

        if (stockHoldings != null) {
            for (StockHolding stockHolding : stockHoldings) {
                int stockId = stockHolding.getStockId();
                int quantity = stockHolding.getCount();

                double buyingPrice = -1.0;

                // Fetch the stock last price when buy-in (amount>0), compare with stock current price, times with quantity in stock holding.
                for (TransactionDetails detail : transactHistory) {
                    if (detail.getStockId() == stockId && detail.getCount() > 0) {
                        buyingPrice = detail.getTransactionPrice();
                    }
                }

                // Fetch latest prices for the given stock ID
                if (buyingPrice != -1.0) {
                    List<Double> prices = fetcher.fetchLatestPrices(stockId, 1, "last_sale", "stockMarket", "last_update_time");

                    if (!prices.isEmpty()) {
                        double currPrice = prices.get(0);
                        double stockUnrealizedProfit = (currPrice - buyingPrice) * quantity;
                        unrealizedProfit += stockUnrealizedProfit;
                    }
                }
            }
        }


        return unrealizedProfit;
    }


}
