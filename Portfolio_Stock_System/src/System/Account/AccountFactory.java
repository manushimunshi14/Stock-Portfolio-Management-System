package System.Account;

import DatabaseReader.DatabaseFetcher;

public class AccountFactory {

    public Account createAccount(String s, int userId, DatabaseFetcher fetcher){
        switch (s){
            case "Trading":
                return new TradingAccount(userId,fetcher);
            case "Derivative":
                return new DerivativeAccount(userId,fetcher);
            default:
                throw new IllegalArgumentException("Invalid account type");
        }
    }
}
