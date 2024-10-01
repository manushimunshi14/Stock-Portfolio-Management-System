package System.User;

import Database.DatabaseManager;
import DatabaseReader.DatabaseFetcher;
import DatabaseReader.Userdb;
import System.Account.AccountFactory;
import System.Account.CustomerAccount;
import System.Observer.ProfitObserver;
import System.Portfolio.Portfolio;

public class Customer extends User implements ProfitObserver {

    private Portfolio portfolio;
    private CustomerAccount account;
    private AccountFactory accountFactory;
    private DatabaseManager dbManager;
    private Userdb userdb;
    private DatabaseFetcher fetcher;
    private boolean isDerivative;

    // Updated constructor
    public Customer(int id, String name, String username, int managerId, int passwordId) {
        super(id, name, username, managerId, passwordId);
        dbManager = new DatabaseManager();
        userdb = new Userdb(dbManager);
        fetcher = new DatabaseFetcher(dbManager);
    }

    public Customer(int id, String name, String username) {
        super(id, name, username);
        dbManager = new DatabaseManager();
        userdb = new Userdb(dbManager);
        fetcher = new DatabaseFetcher(dbManager);
    }


    private void createAccount(String accountType, int userId, DatabaseFetcher fetcher) {
        this.account = (CustomerAccount) accountFactory.createAccount(accountType, userId, fetcher);
    }

    @Override
    public double getBalance() {
        this.accountFactory = new AccountFactory();
        createAccount("Trading", id, fetcher);
        return this.account.getBalance();
    }

    public CustomerAccount getAccount() {
        this.accountFactory = new AccountFactory();
        createAccount("Trading", id, fetcher);
        return account;
    }

    public Portfolio getPortfolio() {
        if (this.portfolio == null) {
            this.portfolio = new Portfolio(id);
        }
        return this.portfolio;

    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    @Override
    public void updateRealizedProfit(double realizedProfit) {
        this.getAccount().updateRealizedProfit(realizedProfit);
    }

    @Override
    public void updateUnrealizedProfit(double unrealizedProfit) {
        this.getAccount().updateUnrealizedProfit(unrealizedProfit);
    }

    public boolean isDerivative() {
        setDerivative();
        return isDerivative;
    }

    private void setDerivative() {
        this.isDerivative = userdb.getDerivativeValue(this.getId());
    }
}
