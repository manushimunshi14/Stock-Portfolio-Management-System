package GUI.Listener;

import System.Stock.Stock;

public interface StockTransactionCompleteListener {
  void onTransactionComplete(Stock currentStock, int count);
}
