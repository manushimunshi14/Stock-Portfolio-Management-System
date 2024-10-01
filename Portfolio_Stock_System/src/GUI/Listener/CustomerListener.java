package GUI.Listener;

import java.util.List;

import javax.swing.JList;

import System.Stock.Stock;
import System.User.Customer;

public interface CustomerListener extends LogoutListener, CustomerProfitListener {

  double getUserBalance();

  String getUserDisplayName();

  String getUsername();

  int getPurchasedStockCount(int stockId);

  void onSaleOut(Stock stock);

  void onCheckout(Stock stock);

  void onWithdraw();

  void onDeposit();

  List<Stock> getPurchasedStockFromPortfolio();

  List<Stock> updateStockList();

  boolean isDerivative();

  void onBuyHouse();

  Customer getCustomer();

  void handleSendRequest();

}
