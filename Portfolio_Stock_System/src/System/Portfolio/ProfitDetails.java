package System.Portfolio;

public class ProfitDetails {
    private int userId;
    private double realizedProfit;
    private double unrealizedProfit;

    public ProfitDetails(int userId, double realizedProfit, double unrealizedProfit) {
        this.userId = userId;
        this.realizedProfit = realizedProfit;
        this.unrealizedProfit = unrealizedProfit;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getRealizedProfit() {
        return realizedProfit;
    }

    public void setRealizedProfit(double realizedProfit) {
        this.realizedProfit = realizedProfit;
    }

    public double getUnrealizedProfit() {
        return unrealizedProfit;
    }

    public void setUnrealizedProfit(double unrealizedProfit) {
        this.unrealizedProfit = unrealizedProfit;
    }
}