package System.Stock;

import java.sql.Timestamp;

public class StockMarketHistory {
    private int stockId;
    private double lastSale;
    private Timestamp lastUpdateTime;

    public StockMarketHistory(int stockId, double lastSale, Timestamp lastUpdateTime) {
        this.stockId = stockId;
        this.lastSale = lastSale;
        this.lastUpdateTime = lastUpdateTime;
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public double getLastSale() {
        return lastSale;
    }

    public void setLastSale(double lastSale) {
        this.lastSale = lastSale;
    }

    public Timestamp getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Timestamp lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
