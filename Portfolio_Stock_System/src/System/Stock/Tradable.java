package System.Stock;

import System.Stock.StockPurchaseDetails;

public interface Tradable {
    /*
       Handle different trading strategies
       Attributes: stock, quantity, price, tradeType such as buy or sell
     */
    StockPurchaseDetails getPurchaseDetails();
    String getSymbol();
    double getCurrPrice();
    public String getStockName();
    boolean isPurchase();
}
