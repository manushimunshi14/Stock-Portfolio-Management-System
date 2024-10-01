package System.Account;

import System.Stock.Tradable;

public interface TradingStrategy {
    void executeTrade(Tradable trade, CustomerAccount account);
}
