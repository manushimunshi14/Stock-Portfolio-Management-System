package System.Stock;

import DatabaseReader.Stockdb;

public class StockManager {
    private Stockdb stockdb;

    public StockManager(Stockdb stockdb) {
        this.stockdb = stockdb;
    }

    public void addNewStock(String symbol, String name, double price) {
        stockdb.insertNewStock(symbol, name, price);
    }

    public void deleteStock(int stockId) {
        stockdb.deleteStock(stockId);
    }

    public void updateStockPrice(int stockId, double newPrice) {
        stockdb.updateStockPrice(stockId, newPrice);
    }
}
