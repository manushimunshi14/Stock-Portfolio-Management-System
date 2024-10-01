package GUI.OtherGUI.CustomerModule;


import Database.DatabaseManager;
import DatabaseReader.Stockdb;
import GUI.Authentication.LoadingWindow;
import GUI.Authentication.TimeSeriesChartPanel;
import GUI.Listener.CustomerListener;
import GUI.Utils.GuiUtils;
import GUI.Utils.RandomGenerator;
import GUI.Utils.TimerManager;
import System.Account.CustomerAccount;
import System.Portfolio.PortfolioManager;
import System.Portfolio.ProfitCalculator;
import System.Stock.StockPriceChanger;
import System.Stock.TransactionDetails;
import System.User.Customer;
import System.User.CustomerManager;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class CustomerReport {
    private JButton viewCustomerButton;
    private JPanel responsePanel;
    private CustomerListener customerListener;

    private static CustomerManager customerManager = new CustomerManager();
    private static ProfitCalculator profitCalculator = new ProfitCalculator();
    private static TimerManager timerManager = new TimerManager();
    private static RandomGenerator randomGenerator = new RandomGenerator();
    private static DatabaseManager dbManager = new DatabaseManager();
    private static Stockdb stockdb = new Stockdb(dbManager);
    private static PortfolioManager portfolioManager = new PortfolioManager(stockdb);

    public CustomerReport (CustomerListener customerListener){
        this.customerListener = customerListener;
        initializeWithdrawButton();
        responsePanel = new JPanel();
    }

    private void initializeWithdrawButton() {
        viewCustomerButton = GuiUtils.createButton("Trading Report");
        viewCustomerButton.setFont(GuiUtils.buttonFont);
        viewCustomerButton.addActionListener(e -> handleAssignAction());
    }

    private void  handleAssignAction(){


        JScrollPane customerProfitTable = getCustomerProfitTable();
        JScrollPane customerTransactionDetailsTable = getCustomerTransactionDetailsTable();

        responsePanel.removeAll();
        responsePanel.setLayout(new BorderLayout());
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, customerProfitTable, customerTransactionDetailsTable);
        splitPane.setResizeWeight(0.025);
        responsePanel.add(splitPane, BorderLayout.CENTER);
        responsePanel.revalidate();
        responsePanel.repaint();
    }

    private JScrollPane getCustomerProfitTable(){
        Customer customer = customerListener.getCustomer();

        StockPriceChanger priceChanger = new StockPriceChanger(customerManager,profitCalculator,timerManager,randomGenerator,portfolioManager);
//        priceChanger.updateCustomerUnrealizedProfit(customer.getId());
        CustomerAccount customerAccount = customerListener.getCustomerAccount(customer);

        return  GuiUtils.customerAccountTable(customerAccount);
    }


    private JScrollPane getCustomerTransactionDetailsTable(){
        Customer customer = customerListener.getCustomer();
        List<TransactionDetails> customerTransactionDetailsList = customerListener.getCustomerTransactionHistory(customer);

        if (customerTransactionDetailsList == null || customerTransactionDetailsList.isEmpty()) {
            JPanel panel = new JPanel(new BorderLayout());
            JLabel label = new JLabel("No Transaction", SwingConstants.CENTER);
            panel.add(label);

            return new JScrollPane(panel);
        } else {
            return GuiUtils.transactionDetailsTable(customerTransactionDetailsList);
        }
    }


    public JButton getViewCustomerButton(){
        return viewCustomerButton;
    }

    public JPanel getResponsePanel(){
        handleAssignAction();
        return responsePanel;
    }

}
