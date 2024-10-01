package System.Account;

public class StockHolding {
    private int stockId;
    private String symbol;
    private String name;
    private int count;

    public StockHolding(int stockId, String symbol, String name, int count) {
        this.stockId = stockId;
        this.symbol = symbol;
        this.name = name;
        this.count = count;
    }

    // Getters
    public int getStockId() {
        return stockId;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
