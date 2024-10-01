package GUI.Authentication;


import Database.DatabaseManager;
import DatabaseReader.DatabaseFetcher;
import DatabaseReader.StockTransactdb;
import DatabaseReader.Stockdb;
import GUI.Controller.CustomerController;
import GUI.Controller.LoginController;
import GUI.Controller.MainController;
import GUI.Listener.CustomerListener;
import GUI.OtherGUI.CustomerModule.*;
import GUI.Utils.GuiUtils;
import GUI.Utils.RandomGenerator;
import GUI.Utils.TimerManager;
import System.Account.LoginResult;
import System.Portfolio.PortfolioManager;
import System.Portfolio.ProfitCalculator;
import System.Stock.StockPriceChanger;
import System.User.Customer;
import System.User.CustomerManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CustomerGUI extends UserGUI {
    private CustomerListener customerListener;
    private BalanceView balanceView;
    private CheckOutStock checkOutStock;
    private CustomerDeposit customerDeposit;
    private CustomerWithdraw customerWithdraw;
    private ViewPurchasedStock viewPurchasedStock;
    private  CustomerReport customerReport;
    private JPanel centralPanel;
    private JPanel windowJPanel;
    private static CustomerManager customerManager = new CustomerManager();
    private static ProfitCalculator profitCalculator = new ProfitCalculator();
    private static TimerManager timerManager = new TimerManager();
    private static RandomGenerator randomGenerator = new RandomGenerator();
    private static DatabaseManager dbManager = new DatabaseManager();
    private static Stockdb stockdb = new Stockdb(dbManager);
    private static PortfolioManager portfolioManager = new PortfolioManager(stockdb);
    private StockPriceChanger priceChanger = new StockPriceChanger(customerManager,profitCalculator,timerManager,randomGenerator,portfolioManager);


    public CustomerGUI(CustomerListener customerListener) {
        this.customerListener = customerListener;
        checkOutStock = new CheckOutStock(customerListener);
        checkOutStock.addStockDetailPanelListener(this::setRespondPanel);

        customerDeposit = new CustomerDeposit(customerListener);
        customerWithdraw = new CustomerWithdraw(customerListener);
        viewPurchasedStock = new ViewPurchasedStock(customerListener);
        customerReport = new CustomerReport(customerListener);

        this.centralPanel = new JPanel(new BorderLayout());
        this.windowJPanel = new JPanel();
    }

    @Override
    public void initializeGUI() {
        balanceView = new BalanceView(customerListener);
        String username = customerListener.getUserDisplayName();
        setTitle("Customer - " + username);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.setSize(this.width, this.height);
        frame.setLayout(new BorderLayout());

        frame.add(windowJPanel, BorderLayout.CENTER);
        placeComponents(windowJPanel);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                customerListener.onLogout();
                priceChanger.stopStockPriceChange();
            }
        });

        // TODO
        //  *************************************************************************************
        //  *** COMMENT THIS LINE IF YOU DONT WANT PRICE CHANGE OR THE FILE GET TOO BIG *********
        //  *************************************************************************************
        priceChanger.stockPriceChange();

        // Set the window in the center of the screen
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        customerListener.handleSendRequest();
    }

    @Override
    public void placeComponents(JPanel panel) {
        panel.setLayout(new BorderLayout(15, 15)); // Set horizontal and vertical gaps
        panel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        // basicInformation panel
        JPanel basicInformation = new JPanel();
        basicInformation.setLayout(new BoxLayout(basicInformation, BoxLayout.Y_AXIS));
        basicInformation.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); // Reduce bottom border spacing

        // Add components to basicInformation with controlled width
        JPanel userDisplayNamePanel = balanceView.getUserDisplayNamePanel();
        userDisplayNamePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, userDisplayNamePanel.getPreferredSize().height));
        JPanel balancePanel = balanceView.getBalancePanel();
        balancePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, balancePanel.getPreferredSize().height));
        JPanel showBalanceCheckBox = balanceView.getShowBalanceCheckBox();
        showBalanceCheckBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, showBalanceCheckBox.getPreferredSize().height));

        basicInformation.add(userDisplayNamePanel);
        basicInformation.add(Box.createVerticalStrut(5)); // Add vertical spacing
        basicInformation.add(balancePanel);
        basicInformation.add(Box.createVerticalStrut(5)); // Add vertical spacing
        basicInformation.add(showBalanceCheckBox);

        // Add basicInformation to buttonPanel
        buttonPanel.add(basicInformation);

        // Other buttons
        JButton viewPurchasedStocksButton = viewPurchasedStock.getViewPurchasedStocksButton();
        JButton depositButton = customerDeposit.getDepositButton();
        JButton withDrawButton = customerWithdraw.getWithdrawButton();
        JButton customerReportButton = customerReport.getViewCustomerButton();

        JButton logoutButton = GuiUtils.createButton("Log Out", 20, 1000, 35, 30);
        logoutButton.setFont(GuiUtils.buttonFont);
        logoutButton.addActionListener(e -> customerListener.onLogout());

        JButton reloadButton = GuiUtils.createButton("Flash Window", 20, 1000, 35, 30);
        reloadButton.setFont(GuiUtils.buttonFont);
        reloadButton.addActionListener(e -> reloadWindow());

        // Add buttons to buttonPanel
        buttonPanel.add(viewPurchasedStocksButton);
        buttonPanel.add(depositButton);
        buttonPanel.add(withDrawButton);
        buttonPanel.add(customerReportButton);

        if (customerListener.isDerivative()) {
            JButton buyHouseButton = GuiUtils.createButton("Buy House");
            buttonPanel.add(buyHouseButton);
            buyHouseButton.addActionListener(e -> customerListener.onBuyHouse());
        }
        buttonPanel.add(logoutButton);
        buttonPanel.add(reloadButton);


        // Add buttonPanel to the east side of the panel
        panel.add(buttonPanel, BorderLayout.EAST);

        // Stock list panel
        JPanel allStockButtonList = checkOutStock.getStockListPanel();
        panel.add(allStockButtonList, BorderLayout.WEST);

        // Central panel
        viewPurchasedStocksButton.addActionListener(e -> setRespondPanel(viewPurchasedStock.getViewPurchasedStocksPanel()));
        customerReportButton.addActionListener(e ->{
                    LoadingWindow loadingWindow = new LoadingWindow();
                    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() throws Exception {
                            setRespondPanel(customerReport.getResponsePanel());
                            return null;
                        }
                        @Override
                        protected void done() {
                            loadingWindow.closeWindow();
                        }
                    };
                    worker.execute();
                }
                );
        depositButton.addActionListener(e -> reloadWindow());
        withDrawButton.addActionListener(e -> reloadWindow());
        panel.add(centralPanel, BorderLayout.CENTER);
        panel.add(new JPanel(), BorderLayout.SOUTH);
    }

    private void setRespondPanel(JPanel newPanel) {
        centralPanel.removeAll();
        centralPanel.add(newPanel, BorderLayout.CENTER);
        centralPanel.revalidate();
        centralPanel.repaint();
        customerListener.handleSendRequest();
    }

    public void viewPurchasedStocks() {
        viewPurchasedStock.viewPurchasedStocks();
        JPanel newPanel = viewPurchasedStock.getViewPurchasedStocksPanel();
        centralPanel.add(newPanel, BorderLayout.CENTER);
        centralPanel.revalidate();
        centralPanel.repaint();
        customerListener.handleSendRequest();
    }

    public void reloadWindow() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                customerListener.handleSendRequest();
                return null;
            }

            @Override
            protected void done() {
                windowJPanel.removeAll();
                centralPanel.removeAll();
                placeComponents(windowJPanel);
                windowJPanel.revalidate();
                windowJPanel.repaint();
                centralPanel.revalidate();
                centralPanel.repaint();
                checkOutStock.initializeStockListPanel();
            }
        };
        worker.execute();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String username = "JohnJay1";
            MainController mainController = new MainController();
            CustomerController userController = mainController.getUserController();
            LoginController loginController = new LoginController(mainController);
            LoginResult loginResult = loginController.getLoginResult(username);
            Customer customer = new Customer(loginResult.getUserId(), loginResult.getDisplayName(), username,
                    loginResult.getManagerId(),
                    loginResult.getPasswordId());
            userController.setUser(customer);
            userController.openPage();
        });
    }
}
