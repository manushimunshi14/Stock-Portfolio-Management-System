package GUI.Controller;

import Database.DatabaseManager;
import DatabaseReader.Requestdb;
import DatabaseReader.StockTransactdb;
import DatabaseReader.Stockdb;
import GUI.Authentication.CustomerGUI;
import GUI.Authentication.LoadingWindow;
import GUI.Listener.CustomerListener;
import GUI.Utils.PromptSetup;
import System.Account.CustomerAccount;
import System.Stock.Stock;
import System.Stock.TransactionDetails;
import System.Trading.StockTrading;
import System.User.Customer;

import javax.swing.*;
import java.util.List;



public class CustomerController extends Controller implements CustomerListener {
    private CustomerGUI userGUI;
    private Customer customer;
    private DatabaseManager dbManager;

    public static final int DERIVATIVE_USER_THRESHOLD = 1000000;

    public CustomerController(MainController mainController) {
        super(mainController);
        this.userGUI = new CustomerGUI(this);
    }

    @Override
    public Customer getCustomer(){
        return customer;
    }

    public void setUser(Customer customer) {
        this.customer = customer;
    }

    @Override
    public int getPurchasedStockCount(int stockId) {
        return customer.getPortfolio().getPurchasedStockCount(stockId);
    }

    @Override
    public List<Stock> getPurchasedStockFromPortfolio() {
        return customer.getPortfolio().getPurchasedStock(customer.getId());
    }

    @Override
    public List<Stock> updateStockList() {
        Stockdb stockdb = new Stockdb();
        return stockdb.getAllStocks();
    }

    @Override
    public void onSaleOut(Stock stock) {
        LoadingWindow outLoadingWindow = new LoadingWindow();
        SwingUtilities.invokeLater(() -> {
            SwingWorker<Void, Void> outWorker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    int maxCount = getPurchasedStockCount(stock.getStockId());
                    TransactionController transactionController = new TransactionController(mainController);
                    transactionController.setStock(stock);
                    transactionController.setCustomer(customer);
                    String report = "Stock Current Price: $" + stock.getCurrPrice() + "\nYour total amount of " + stock.getSymbol() + " is " + maxCount + ".\n";
                    transactionController.setMassage(report);
                    transactionController.setMaxCount(maxCount);
                    transactionController.setTradingOptions(TransactionController.SOLD);
                    transactionController.setTitle("Sale Stock");
                    transactionController.setWindowsSize(420, 285);
                    transactionController.setStockTransactionCompleteListener((currentStock, count) -> {
                        LoadingWindow loadingWindow = new LoadingWindow();
                        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                            @Override
                            protected Void doInBackground() throws Exception {

                                double beforeBalance = customer.getBalance();
                                int stockId = currentStock.getStockId();
                                String stockSymbol = currentStock.getSymbol();
                                String stockName = currentStock.getStockName();

                                StockTrading.sellStock(customer, customer.getPortfolio(), currentStock, count);

                                double stockRecordedPrice = currentStock.getCurrPrice();
                                double totalRevenue = stockRecordedPrice * count;
                                double updatedBalance = customer.getBalance();
                                int[] windowsSize = new int[]{360, 260};

                                String report = String.format(
                                        "\nYou have sold the following stock:\nStock ID: %d\nStock Symbol: %s\nStock Name: %s\nSelling Price: $%.2f\nQuantity Sold: %d\nTotal Revenue: $%.2f\n\nPrevious Balance: $%.2f\nBalance after Sale: $%.2f",
                                        stockId, stockSymbol, stockName, stockRecordedPrice, count, totalRevenue, beforeBalance, updatedBalance
                                );
                                PromptSetup.setSuccessReport(report, windowsSize, mainController);

                                return null;
                            }

                            @Override
                            protected void done() {
                                loadingWindow.closeWindow();
                                userGUI.reloadWindow();
                            }
                        };
                        worker.execute();
                    });
                    transactionController.openPage();

                    return null;
                }

                @Override
                protected void done() {
                    outLoadingWindow.closeWindow();
                }
            };
            outWorker.execute();
        });
    }

    @Override
    public void onCheckout(Stock stock) {
        SwingUtilities.invokeLater(() -> {
            int maxCount = 30;
            TransactionController transactionController = new TransactionController(mainController);
            transactionController.setTradingOptions(TransactionController.PURCHASE);
            transactionController.setStock(stock);
            transactionController.setCustomer(customer);
            String report = "Current Stock Price: $" + stock.getCurrPrice() + "\n";
            if (isDerivative()) {
                maxCount = 100;
                report += "You are a wealthy person!\nThe maximum number of this stock  can be purchased is" + maxCount + "!\n";
                transactionController.setMassage(report);
                transactionController.setMaxCount(maxCount);
            } else {
                report += "The maximum number of this stock  can be purchased is" + maxCount + "!\n";
                transactionController.setMassage(report);
                transactionController.setMaxCount(maxCount);
            }
            transactionController.setTitle("Check out");
            transactionController.setWindowsSize(440, 270);
            transactionController.setStockTransactionCompleteListener((currentStock, count) -> {
                LoadingWindow loadingWindow = new LoadingWindow();
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        double beforeBalance = customer.getBalance();
                        int stockId = currentStock.getStockId();
                        String stockSymbol = currentStock.getSymbol();
                        String stockName = currentStock.getStockName();
                        StockTrading.buyStock(customer, currentStock, count);

                        double stockRecordedPrice = currentStock.getCurrPrice();
                        double totalRevenue = stockRecordedPrice * count;
                        double updatedBalance = customer.getBalance();
                        int[] windowsSize = new int[]{360, 260};
                        String report = String.format(
                                "\nYou have purchased the following stock:\nStock ID: %d\nStock Symbol: %s\nStock Name: %s\nSelling Price: $%.2f\nQuantity Sold: %d\nTotal Consumption: $%.2f\n\nPrevious Balance: $%.2f\nBalance after Sale: $%.2f",
                                stockId, stockSymbol, stockName, stockRecordedPrice, count, totalRevenue, beforeBalance, updatedBalance
                        );
                        PromptSetup.setSuccessReport(report, windowsSize, mainController);

                        return null;
                    }

                    @Override
                    protected void done() {
                        loadingWindow.closeWindow();
                        userGUI.reloadWindow();
                    }
                };
                worker.execute();
            });
            transactionController.openPage();
        });
    }

    @Override
    public void onWithdraw() {
        SwingUtilities.invokeLater(() -> {
            double maxBalance = this.customer.getBalance();
            TransactionController transactionController = new TransactionController(mainController);
            transactionController.setCustomer(this.customer);
            transactionController.setTradingOptions(TransactionController.WITHDRAW);
            transactionController.setMassage("You can withdraw up to $" + String.format("%.2f", maxBalance) + "!\n");
            transactionController.setMaxBalance(maxBalance);
            transactionController.setTitle("Withdraw");
            transactionController.setWindowsSize(420, 270);
            transactionController.setCustomerTransactionCompleteListener((currentCustomer, amount) -> {
                System.out.println("Customer hash: " + System.identityHashCode(customer));
                System.out.println("CurrentCustomer hash: " + System.identityHashCode(currentCustomer));
                LoadingWindow loadingWindow = new LoadingWindow();
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        double beforeBalance = currentCustomer.getAccount().getBalance();

                        CustomerAccount customerAccount = currentCustomer.getAccount();
                        customerAccount.withdraw(amount);
                        customerAccount.updateAccountBalance(customer.getId());

                        double updatedBalance = currentCustomer.getAccount().getBalance();
                        int[] windowsSize = new int[]{320, 200};
                        String report = String.format(
                                "\nWithdraw successful!\nPrevious Balance: %.2f\nWithdraw: %.2f\nCurrent Balance: %.2f\n",
                                beforeBalance, amount, updatedBalance
                        );
                        PromptSetup.setSuccessReport(report, windowsSize, mainController);
                        return null;
                    }

                    @Override
                    protected void done() {
                        loadingWindow.closeWindow();
                        userGUI.reloadWindow();
                    }
                };
                worker.execute();
            });
            transactionController.openPage();
        });
    }


    @Override
    public void onDeposit() {
        SwingUtilities.invokeLater(() -> {
            double maxBalance = 1000000.00;
            TransactionController transactionController = new TransactionController(mainController);
            transactionController.setCustomer(customer);
            transactionController.setTradingOptions(TransactionController.DEPOSIT);
            transactionController.setMassage("You can deposit up to $" + String.format("%.2f", maxBalance) + "!\n");
            transactionController.setMaxBalance(maxBalance);
            transactionController.setTitle("Deposit");
            transactionController.setWindowsSize(420, 270);
            transactionController.setCustomerTransactionCompleteListener((currentCustomer, amount) -> {
                System.out.println("Customer hash: " + System.identityHashCode(customer));
                System.out.println("CurrentCustomer hash: " + System.identityHashCode(currentCustomer));
                LoadingWindow loadingWindow = new LoadingWindow();
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        double beforeBalance = currentCustomer.getAccount().getBalance();

                        CustomerAccount customerAccount = currentCustomer.getAccount();
                        customerAccount.deposit(amount);
                        customerAccount.updateAccountBalance(customer.getId());

                        double updatedBalance = currentCustomer.getAccount().getBalance();
                        int[] windowsSize = new int[]{320, 200};
                        String report = String.format(
                                "\nDeposit successful!\nPrevious Balance: %.2f\nDeposited: %.2f\nCurrent Balance: %.2f\n",
                                beforeBalance, amount, updatedBalance
                        );
                        PromptSetup.setSuccessReport(report, windowsSize, mainController);
                        return null;
                    }

                    @Override
                    protected void done() {
                        loadingWindow.closeWindow();
                        userGUI.reloadWindow();
                    }
                };
                worker.execute();
            });
            transactionController.openPage();
        });
    }

    @Override
    public void onLogout() {
        userGUI.closeWindow();
        SwingUtilities.invokeLater(() -> {
            LoginController loginController = new LoginController(mainController);
            loginController.onLoginRequested();
        });
    }


    @Override
    public double getUserBalance() {
        return this.customer.getBalance();
    }


    @Override
    public String getUserDisplayName() {
        return customer.getName();
    }

    @Override
    public String getUsername() {
        return customer.getUsername();
    }

    @Override
    public void openPage() {
        userGUI.setWindowsSize(1200, 850);
        if (userGUI.getFrame() == null) {
            userGUI.setFrame();
        }
        userGUI.initializeGUI();
    }

    @Override
    public boolean isDerivative() {
        return customer.isDerivative();
    }

    @Override
    public void onBuyHouse() {
        int[] windowsSize = new int[]{320, 150};
        String report = "\nIt's being built!\nStay tuned!\n";
        PromptSetup.setSuccessReport(report, windowsSize, mainController);
    }

    @Override
    public CustomerAccount getCustomerAccount(Customer customer) {

        return customer.getAccount();
    }

    @Override
    public List<TransactionDetails> getCustomerTransactionHistory(Customer customer) {
        dbManager = new DatabaseManager();
        StockTransactdb transactdb = new StockTransactdb(dbManager);
        int userId = customer.getId();
        return transactdb.getStocksTransact(userId);
    }

    @Override
    public void handleSendRequest() {
        dbManager = new DatabaseManager();
        Requestdb requestdb = new Requestdb(dbManager);
        List<Customer> customers = requestdb.getDerivativeRequest();
        int userId = customer.getId();
        boolean sendRequest = true;

        for (Customer c : customers){
            if (c.getId() == userId){
                sendRequest = false;
            }
        }

        if (customer.getAccount().getRealizedProfit() > DERIVATIVE_USER_THRESHOLD && !isDerivative() && sendRequest){
            DerivativeUserPromptController derivativeUserPromptController = new DerivativeUserPromptController(mainController);
            derivativeUserPromptController.setCustomer(customer);
            derivativeUserPromptController.setWindowSize(500,250);
            derivativeUserPromptController.openPage();
        }
    }
}
