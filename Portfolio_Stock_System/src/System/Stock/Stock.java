package System.Stock;

import java.util.List;
import java.util.Map;

import Database.DatabaseManager;
import DatabaseReader.DatabaseReader;

import java.sql.Timestamp;
import java.util.Objects;

public class Stock implements Tradable {
    /*
    Single stock
    Attributes: Stock name, symbol, currentPrice
     */

    private Timestamp lastUpdateTime;
    private int stockId;
    private String symbol;
    private String stockName;
    private double currPrice;

    public Stock(int stockId, String symbol, String stockName, double currPrice, Timestamp latUpdateTime) {
        this.symbol = symbol;
        this.stockName = stockName;
        this.currPrice = currPrice;
        this.stockId = stockId;
        this.lastUpdateTime = latUpdateTime;
    }

    public Stock(int stockId, String symbol, String stockName) {
        this.symbol = symbol;
        this.stockName = stockName;
        this.stockId = stockId;
    }

    public Stock(String symbol, String stockName, double currPrice) {
        this.symbol = symbol;
        this.stockName = stockName;
        this.currPrice = currPrice;
    }

    public Timestamp getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Timestamp lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public int getStockId() {
        return stockId;
    }

    @Override
    public StockPurchaseDetails getPurchaseDetails() {
        return null;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public double getCurrPrice() {
        return currPrice;
    }


    @Override
    public String getStockName() {
        return stockName;
    }

    @Override
    public boolean isPurchase() {
        return false;
    }

    @Override
    public String toString() {
        return "Stock ID: " + this.stockId + ", Stock name: " + this.stockName + ", Symbol: " + this.symbol
                + ", Current Price: " + this.currPrice;
    }

    public void setCurrPrice(double currPrice) {
        this.currPrice = currPrice;
    }

    public List<Map<String, Object>> getStockSales() {
        DatabaseManager dbManager = new DatabaseManager();
        DatabaseReader dbReader = new DatabaseReader(dbManager);

        String query = "SELECT last_sale, last_update_time FROM stockMarket WHERE stock_id = ?";

        return dbReader.getAllRowsWithParameters(query, new Object[] { this.stockId });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stock)) return false;
        Stock stock = (Stock) o;
        return this.stockId == stock.stockId && this.symbol.equals(stock.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stockId, symbol);
    }

    public static void main(String[] args) {
        // 创建 Stock 对象的实例
        // 注意：这里需要提供有效的参数，例如 stockId, symbol, stockName 等
        Stock stock = new Stock(1, "AAPL", "Apple Inc.");


    }

}
