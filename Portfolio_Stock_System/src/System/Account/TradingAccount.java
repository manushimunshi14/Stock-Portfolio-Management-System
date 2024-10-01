package System.Account;

import DatabaseReader.DatabaseFetcher;
import System.Stock.Tradable;

public class TradingAccount extends CustomerAccount{
    public TradingAccount(int userId, DatabaseFetcher fetcher) {
        super(userId);
    }
    /*
        Manage trading actions for a customer, include realized and unrealizedProfit,
        list of stocks own by customer, etc
     */

    @Override
    public void performTrade(Tradable trade) {
        super.performTrade(trade);
//        updateRealizedProfit();
    }

}
