package System.Observer;

public interface ProfitObserver {
    void updateRealizedProfit(double realizedProfit);
    void updateUnrealizedProfit(double unrealizedProfit);
}
