package System.Account;

import Database.DatabaseManager;
import DatabaseReader.DatabaseFetcher;
import DatabaseReader.Profitdb;
import System.Portfolio.Portfolio;
import System.Portfolio.ProfitCalculator;
import System.Observer.ProfitObserver;
import System.Portfolio.ProfitDetails;
import System.Stock.StockPurchaseDetails;
import System.Stock.Tradable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class CustomerAccount implements Account {
    protected TradingStrategy tradingStrategy;
    protected Portfolio portfolio;
    protected DatabaseManager dbManger = new DatabaseManager();
    private Profitdb profitdb;
    protected double realizedProfit;
    protected double unrealizedProfit;
    protected ProfitCalculator calculator;

    protected double balance;
    private int userId;
    private List<ProfitObserver> observers = new ArrayList<>();

    public void attachObserver(ProfitObserver observer){
        observers.add(observer);
    }

    protected void notifyObservers(){
        for(ProfitObserver observer : observers){
            observer.updateRealizedProfit(this.realizedProfit);
            observer.updateUnrealizedProfit(this.unrealizedProfit);
        }
    }

    protected CustomerAccount(int userId, DatabaseFetcher fetcher){
        this.userId = userId;
        this.portfolio = new Portfolio();
        this.calculator = new ProfitCalculator();
        this.balance = fetcher.fetchWalletBalance(userId);
    }

    protected CustomerAccount(int userId){
        DatabaseManager dbManager = new DatabaseManager();
        DatabaseFetcher fetcher = new DatabaseFetcher(dbManager);
        this.userId = userId;
        this.portfolio = new Portfolio();
        this.calculator = new ProfitCalculator();
        this.balance = fetcher.fetchWalletBalance(userId);
    }

    public void setTradingStrategy(TradingStrategy tradingStrategy){
        this.tradingStrategy = tradingStrategy;
    }

    public void performTrade(Tradable trade){
        tradingStrategy.executeTrade(trade,this);
        updateBalanceAfterTrade(trade);
        updateAccountBalance(userId);
    }

    private void updateBalanceAfterTrade(Tradable trade) {
        StockPurchaseDetails details = trade.getPurchaseDetails();
        double amount = details.getPurchasePrice() * details.getQuantity();
        if(trade.isPurchase()){
            this.balance -= amount;
        }else {
            System.out.println("Not enough money");
        }
    }

    public void updateAccountBalance(int userId) {
        String query = "UPDATE wallet SET balance = ? WHERE user_id = ?";
        try (Connection conn = dbManger.getDbConnector();
             PreparedStatement pStmt = conn.prepareStatement(query)) {

            pStmt.setDouble(1, this.getBalance());
            pStmt.setInt(2, userId);

            pStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Portfolio getPortfolio(){
        return this.portfolio;
    }

    @Override
    public void deposit(double amount) {
        if (amount >= 0){
            this.balance += amount;
        }
    }

    @Override
    public void withdraw(double amount) {
        if (amount > 0 && this.balance >= amount){
            this.balance -= amount;
        }
    }

    @Override
    public double getBalance() {
        return this.balance;
    }

    public void loadProfitData() {
        profitdb = new Profitdb(dbManger);
        ProfitDetails profitData = profitdb.getUserProfit(this.userId);
        if (profitData != null) {
            this.realizedProfit = profitData.getRealizedProfit();
            this.unrealizedProfit = profitData.getUnrealizedProfit();
        }
    }

    public void updateRealizedProfit(double value){
        profitdb = new Profitdb(dbManger);
        this.realizedProfit = value;
        boolean success = profitdb.updateRealizedProfit(this.userId, this.realizedProfit);
        if (!success) {
            System.out.println("Failed to update realized profit in the database");
        }
    }

    public void updateUnrealizedProfit(double value){
        profitdb = new Profitdb(dbManger);
        this.unrealizedProfit = value;
        boolean success = profitdb.updateUnrealizedProfit(this.userId,this.unrealizedProfit);
        if (!success) {
            System.out.println("Failed to update unrealized profit in the database");
        }
        notifyObservers();
    }

    public double getRealizedProfit(){
        profitdb = new Profitdb(dbManger);
        return profitdb.getRealizedProfit(this.userId);
    }

    public double getUnrealizedProfit(){
        profitdb = new Profitdb(dbManger);
        return profitdb.getUnrealizedProfit(this.userId);
    }


    public  int getUserId(){
        return userId;
    }


}

