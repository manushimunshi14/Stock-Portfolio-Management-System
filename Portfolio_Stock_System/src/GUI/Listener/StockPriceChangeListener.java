package GUI.Listener;

import System.Stock.Stock;

public interface StockPriceChangeListener {
  void onStockPriceChangeComplete(Stock currentStock, double newPrice);
}
