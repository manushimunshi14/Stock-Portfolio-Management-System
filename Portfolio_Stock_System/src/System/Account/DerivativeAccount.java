package System.Account;

import DatabaseReader.DatabaseFetcher;

public class DerivativeAccount extends CustomerAccount{
    public DerivativeAccount(int userId, DatabaseFetcher fetcher) {
        super(userId);
    }
    /*
        Derivative account for customer, include customer owned derivated investment, unrealized and
        realized profit
     */

}
