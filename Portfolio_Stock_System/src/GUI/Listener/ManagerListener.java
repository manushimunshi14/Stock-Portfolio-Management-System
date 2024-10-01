package GUI.Listener;

import java.util.List;

import GUI.Controller.CustomerController;
import System.Account.CustomerAccount;
import System.Account.DerivativeRequest;
import System.Account.SignupRequest;
import System.Stock.Stock;
import System.Stock.TransactionDetails;
import System.User.Customer;

public interface ManagerListener extends LogoutListener, CustomerProfitListener {
  String getUserDisplayName();

  List<SignupRequest> getSignupRequestList();

  void onApprove(SignupRequest signupRequest);

  void onDisapprove(SignupRequest signupRequest);

  void onPromote(DerivativeRequest derivativeRequest);

  List<Customer> getDerivativeUserList();

  List<Stock> getAllStockList();

  void handlePriceChange(Stock stock);

  void onDeleteStock(Stock stock);

  void onAddStock();

  void onViewCustomerProfit(Customer customer);

  List<Customer> getCustomerList();
}
