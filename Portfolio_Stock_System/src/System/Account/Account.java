package System.Account;

public interface Account {
    /*
        Set up account with id, balance and dataset
     */
    void deposit(double amount);
    void withdraw(double amount);
    double getBalance();
}
