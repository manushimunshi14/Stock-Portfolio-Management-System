package GUI.Listener;

import System.Stock.Stock;

public interface AddStockListener {
  void onConfirm(String symbol, String name, String price);

  void onCancel();
}
