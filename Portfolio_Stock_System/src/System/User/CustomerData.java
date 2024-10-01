package System.User;

import System.Portfolio.Portfolio;
import System.Stock.Stock;
import System.Stock.StockPurchaseDetails;

import java.util.HashMap;
import java.util.Map;

public class CustomerData {
    protected Customer customer;
    private double profit;

    public CustomerData(Customer customer,double profit) {
        this.customer = customer;
        this.profit = profit;
    }

    // Method to return customer attributes - name, profits,
    public String getCustomerName(){
        return customer.getName();
    }

//    public Map<Stock, Integer> getOwnedStocks(Customer customer) {
//        Map<Stock, Integer> ownedStocksWithQuantities = new HashMap<>();
//        Portfolio portfolio = customer.getAccount().getPortfolio();
//        Map<Stock, StockPurchaseDetails> ownedStocks = portfolio.getStockTransactHistory(customer.getId());
//
//        for (Map.Entry<Stock, StockPurchaseDetails> entry : ownedStocks.entrySet()) {
//            Stock stock = entry.getKey();
//            int quantity = entry.getValue().getQuantity();
//            ownedStocksWithQuantities.put(stock, quantity);
//        }
//
//        return ownedStocksWithQuantities;
//    }

    public double getRealizedProfit(){
        return customer.getAccount().getRealizedProfit();
    }

    public double getUnrealizedProfit(){
        return customer.getAccount().getUnrealizedProfit();
    }

    public double getBalance(){
        return customer.getBalance();
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }
}
