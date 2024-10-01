package GUI.Listener;

import System.Account.CustomerAccount;
import System.Stock.TransactionDetails;
import System.User.Customer;

import java.util.List;

public interface CustomerProfitListener {
    CustomerAccount getCustomerAccount(Customer customer);

    List<TransactionDetails> getCustomerTransactionHistory(Customer customer);
}
