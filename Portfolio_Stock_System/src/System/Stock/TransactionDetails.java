package System.Stock;

import java.sql.Timestamp;

public class TransactionDetails {
    private int userId;
    private int stockId;
    private int count;
    private double transactionPrice;
    private Timestamp tradingDate;
    private String symbol;
    private double totalConsumption;

    public TransactionDetails(int userId, int stockId, int count, String symbol,
                              double transactionPrice, Timestamp tradingDate,
                              double totalConsumption) {
        this.userId = userId;
        this.stockId = stockId;
        this.count = count;
        this.symbol = symbol;
        this.transactionPrice = transactionPrice;
        this.tradingDate = tradingDate;
        this.totalConsumption = totalConsumption;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStockId() {
        return stockId;
    }

    public String getStockSymbol(){
        return symbol;
    }


    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getTransactionPrice() {
        return transactionPrice;
    }

    public void setTransactionPrice(double transactionPrice) {
        this.transactionPrice = transactionPrice;
    }

    public Timestamp getTradingDate() {
        return tradingDate;
    }

    public void setTradingDate(Timestamp tradingDate) {
        this.tradingDate = tradingDate;
    }

    public double getTotalConsumption() {
        return totalConsumption;
    }

    public void setTotalConsumption(double totalConsumption) {
        this.totalConsumption = totalConsumption;
    }
}
