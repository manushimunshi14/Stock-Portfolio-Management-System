package GUI.Controller;

import Database.DatabaseManager;
import DatabaseReader.Profitdb;
import DatabaseReader.Requestdb;
import DatabaseReader.StockTransactdb;
import DatabaseReader.Stockdb;
import DatabaseReader.Userdb;
import GUI.Authentication.LoadingWindow;
import GUI.Authentication.ManagerGUI;
import GUI.Listener.ManagerListener;
import GUI.Utils.PromptSetup;
import System.Account.CustomerAccount;
import System.Account.DerivativeRequest;
import System.Account.SignupRequest;
import System.Stock.Stock;
import System.Stock.TransactionDetails;
import System.User.Admin;
import System.User.Customer;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

public class ManagerController extends Controller implements ManagerListener {
    private Admin admin;
    private DatabaseManager dbManager;
    private ManagerGUI managerGUI;
    private Stockdb stockdb;

    public ManagerController(MainController mainController) {
        super(mainController);
        this.managerGUI = new ManagerGUI(this);

    }

    public void setUser(Admin admin) {
        this.admin = admin;
    }

    @Override
    public void openPage() {
        managerGUI.setWindowsSize(1200, 850);
        if (managerGUI.getFrame() == null) {
            managerGUI.setFrame();
        }
        managerGUI.initializeGUI();
    }

    public String getUserDisplayName() {
        return admin.getName();
    }

    @Override
    public void onLogout() {
        managerGUI.closeWindow();
        LoginController loginController = new LoginController(mainController);
        loginController.onLoginRequested();
    }

    @Override
    public List<Stock> getAllStockList() {
        Stockdb stockdb = new Stockdb();
        return stockdb.getAllStocks();
    }

    @Override
    public void onAddStock() {
        AddStockController addStockController = new AddStockController(mainController);
        addStockController.setManagerGUI(this.managerGUI);
        addStockController.openPage();
    }

    @Override
    public void onViewCustomerProfit(Customer customer) {
        ViewCustomerController viewCustomerController = new ViewCustomerController(mainController);
        viewCustomerController.setManagerListener(this);
        viewCustomerController.setCustomer(customer);
        viewCustomerController.setTitle(customer.getName() + "'s information");
        viewCustomerController.openPage();
//        managerGUI.reloadWindow();
    }

    @Override
    public List<SignupRequest> getSignupRequestList() {
        dbManager = new DatabaseManager();
        Requestdb requestdb = new Requestdb(dbManager);
        return requestdb.getSignupRequests();
    }

    @Override
    public List<Customer> getDerivativeUserList() {
        dbManager = new DatabaseManager();
        Requestdb requestdb = new Requestdb(dbManager);
        return requestdb.getDerivativeRequest();
    }

    @Override
    public void onApprove(SignupRequest signupRequest) {
        dbManager = new DatabaseManager();
        Requestdb requestdb = new Requestdb(dbManager);
        Profitdb profitdb = new Profitdb(dbManager);
        LoadingWindow loadingWindow = new LoadingWindow();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {

                // Remove request ticket from table
                boolean approved = requestdb.deleteRequest(signupRequest.getUserName(),"user_name");
                if(approved) {
                    signupUser(signupRequest.getUserName(), signupRequest.getPassword(), signupRequest.getDisplayName());

                    int[] windowsSize = new int[]{400,200};
                    String report = "\n\n" + signupRequest.getUserName() + "'s application for registration approved!\n";
                    PromptSetup.setSuccessReport(report,windowsSize,mainController);
                }
                return null;
            }

            @Override
            protected void done() {
                loadingWindow.closeWindow();
                managerGUI.handleSignup();
            }
        };
        worker.execute();
    }

    @Override
    public void onDisapprove(SignupRequest signupRequest) {
        dbManager = new DatabaseManager();
        Requestdb requestdb = new Requestdb(dbManager);
        LoadingWindow loadingWindow = new LoadingWindow();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {

                // Remove request ticket from table
                boolean deny = requestdb.deleteRequest(signupRequest.getUserName(),"user_name");
                if (deny){
                    int[] windowsSize = new int[]{400,200};
                    String report = "\n\n" + signupRequest.getUserName() + "'s application canceled!\n";
                    PromptSetup.setErrorReport(report,windowsSize,mainController);
                }
                return null;
            }

            @Override
            protected void done() {
                loadingWindow.closeWindow();
                managerGUI.handleSignup();
            }
        };
        worker.execute();
    }

    @Override
    public void onPromote(DerivativeRequest derivativeRequest) {
        dbManager = new DatabaseManager();
        Requestdb requestdb = new Requestdb(dbManager);
        LoadingWindow loadingWindow = new LoadingWindow();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                System.out.println("Derivative User: " + derivativeRequest.getUserName());
                boolean promote = requestdb.deleteRequest(derivativeRequest.getUserName(),"display_name");
                if (promote){
                    // Add user to derivative table
                    boolean added = requestdb.addToDerivative(derivativeRequest.getUserName());
                    if (added) {
                        int[] windowsSize = new int[]{200, 260};
                        String report = "\n\n" + derivativeRequest.getUserName() + "'s application approved!\n";
                        PromptSetup.setSuccessReport(report, windowsSize, mainController);
                    }
                }
                System.out.println(derivativeRequest.getRequestNumber() + " remove from request table");
                return null;
            }

            @Override
            protected void done() {
                loadingWindow.closeWindow();
                managerGUI.assignDerivativeUser();
            }
        };
        worker.execute();
    }

    @Override
    public void handlePriceChange(Stock stock) {
        SwingUtilities.invokeLater(() -> {
            double currentPrice = stock.getCurrPrice();
            TransactionController transactionController = new TransactionController(mainController);
            transactionController.setStock(stock);
            transactionController.setTradingOptions(TransactionController.CHANGE);
            transactionController.setMassage("This stock current price is $" + String.format("%.2f", currentPrice) + "!\n");
            transactionController.setCurrentPrice(currentPrice);
            transactionController.setTitle("Change Price");
            transactionController.setWindowsSize(420, 270);
            transactionController.setStockPriceChangeListener((currentStock, newPrice) -> {
                LoadingWindow loadingWindow = new LoadingWindow();
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        stockdb = new Stockdb();
                        int stockId = currentStock.getStockId();
                        String stockSymbol = currentStock.getSymbol();
                        String stockName = currentStock.getStockName();
                        double stockBeforePrice = currentStock.getCurrPrice();

                        // Update stock price
                        stockdb.updateStockPrice(stockId,newPrice);

                        int[] windowsSize = new int[]{360,260};
                        String report = String.format(
                                "\nUpdate the price of the stock:\nStock ID: %d\nStock Symbol: %s\nStock Name: %s\nPrevious Price: $%.2f\n\nCurrent Price: $%.2f\n",
                                stockId, stockSymbol, stockName, stockBeforePrice, newPrice);
                        PromptSetup.setSuccessReport(report,windowsSize,mainController);
                        return null;
                    }

                    @Override
                    protected void done() {
                        loadingWindow.closeWindow();
                        managerGUI.reloadWindow();
                    }
                };
                worker.execute();
            });
            transactionController.openPage();
        });
    }

    @Override
    public void onDeleteStock(Stock stock) {
        LoadingWindow loadingWindow = new LoadingWindow();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                stockdb = new Stockdb();

                int stockId = stock.getStockId();
                String stockSymbol = stock.getSymbol();
                String stockName = stock.getStockName();
                int[] windowsSize = new int[]{360,260};

                // Method to deleteStock from market
                boolean deleted = stockdb.deleteStock(stockId);

                if (deleted) {
                    String report = String.format(
                            "\nSuccessfully deleted the stock:\nStock ID: %d\nStock Symbol: %s\nStock Name: %s\n",
                            stockId, stockSymbol, stockName);
                    PromptSetup.setSuccessReport(report, windowsSize, mainController);
                }else{
                    String report = String.format(
                            "\nUnable to delete the stock:\nStock Symbol: %s\nSome user are still holding this stock",
                            stockSymbol);
                    PromptSetup.setErrorReport(report, windowsSize, mainController);
                }
                return null;
            }

            @Override
            protected void done() {
                loadingWindow.closeWindow();
                managerGUI.changeStockQuantity();
            }
        };
        worker.execute();
    }

    private void signupUser(String username, String password, String displayName) {
        DatabaseManager dbManager = new DatabaseManager();
        try {

            // Insert hashed Password into Password table
            String query = "INSERT INTO password (password) VALUES (?)";
            dbManager.executeUpdate(query, true, password);

            // Get the current user's password id
            int passwordId = dbManager.getLastInsertedId();

            // Insert manager = false into the manager table
            query = "INSERT INTO manager (manager) VALUES (false)";
            dbManager.executeUpdate(query, true);

            // Get the current user's manager's id
            int managerId = dbManager.getLastInsertedId();

            // Inserting user information into the user table
            query = "INSERT INTO user (user_name, display_name, manager_id, password_id) VALUES (?, ?, ?, ?)";
            dbManager.executeUpdate(query, true, username, displayName, managerId, passwordId);


            // Get the current user's user id
            int userId = dbManager.getLastInsertedId();

            // Insert into wallet table
            query = "INSERT INTO wallet (user_id) VALUES (?)";
            dbManager.executeUpdate(query, true, userId);

            query = "INSERT INTO customer_profit (user_id) VALUES (?)";
            dbManager.executeUpdate(query, true, userId);

        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        } finally {
            dbManager.closeConnection();
        }
    }

   @Override
    public List<Customer> getCustomerList(){
       dbManager = new DatabaseManager();
       Userdb userdb = new Userdb(dbManager);
       return userdb.getCustomerList();
    }


    @Override
    public  CustomerAccount getCustomerAccount(Customer customer){
        return customer.getAccount();
    }

    @Override
    public List<TransactionDetails> getCustomerTransactionHistory(Customer customer) {
        dbManager = new DatabaseManager();
        StockTransactdb transactdb = new StockTransactdb(dbManager);
        int userId = customer.getId();
        return transactdb.getStocksTransact(userId);
    }
}
