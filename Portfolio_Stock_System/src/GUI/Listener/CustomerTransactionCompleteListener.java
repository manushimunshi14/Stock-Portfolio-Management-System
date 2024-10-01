package GUI.Listener;

import System.User.Customer;

public interface CustomerTransactionCompleteListener {
   void onTransactionComplete(Customer currentCustomer, double amount);
}
