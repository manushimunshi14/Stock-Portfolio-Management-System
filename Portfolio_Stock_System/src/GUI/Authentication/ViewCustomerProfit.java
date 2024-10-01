package GUI.Authentication;

import Database.DatabaseManager;
import DatabaseReader.Stockdb;
import GUI.Listener.CustomerListener;
import GUI.Listener.ManagerListener;
import GUI.Listener.ViewCustomerListener;
import GUI.OtherGUI.CustomerModule.*;
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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class ViewCustomerProfit extends BaseGUI{
    private  ManagerListener managerListener;
    private ViewCustomerListener viewCustomerListener;

    private static CustomerManager customerManager = new CustomerManager();
    private static ProfitCalculator profitCalculator = new ProfitCalculator();
    private static TimerManager timerManager = new TimerManager();
    private static RandomGenerator randomGenerator = new RandomGenerator();
    private static DatabaseManager dbManager = new DatabaseManager();
    private static Stockdb stockdb = new Stockdb(dbManager);
    private static PortfolioManager portfolioManager = new PortfolioManager(stockdb);

    public void setViewCustomerListener(ViewCustomerListener viewCustomerListener) {
        this.viewCustomerListener = viewCustomerListener;
    }

    public void setManagerListener(ManagerListener managerListener) {
        this.managerListener = managerListener;
    }

    @Override
    public void initializeGUI() {
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        frame.setSize(this.width, this.height);

        JPanel customerInformPanel = new JPanel();
        customerInformPanel.setLayout(new BorderLayout());
        frame.add(customerInformPanel);

        LoadingWindow loadingWindow = new LoadingWindow();

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                JScrollPane customerProfitTable = getCustomerProfitTable();
                JScrollPane customerTransactionDetailsTable = getCustomerTransactionDetailsTable();

                JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, customerProfitTable, customerTransactionDetailsTable);
                splitPane.setResizeWeight(0.025);
                customerInformPanel.add(splitPane, BorderLayout.CENTER);
                return null;
            }

            @Override
            protected void done() {
                loadingWindow.closeWindow();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        };
        worker.execute();
    }

    private JScrollPane getCustomerProfitTable(){
        Customer customer = viewCustomerListener.getCustomer();
        StockPriceChanger priceChanger = new StockPriceChanger(customerManager,profitCalculator,timerManager,randomGenerator,portfolioManager);

        System.out.println(customer.getName());
        priceChanger.updateCustomerUnrealizedProfit(customer.getId());

        CustomerAccount customerAccount = managerListener.getCustomerAccount(customer);
        return  GuiUtils.customerAccountTable(customerAccount);
    }

    private JScrollPane getCustomerTransactionDetailsTable(){
        Customer customer = viewCustomerListener.getCustomer();
        List<TransactionDetails> customerTransactionDetailsList = managerListener.getCustomerTransactionHistory(customer);
        return GuiUtils.transactionDetailsTable(customerTransactionDetailsList);
    }
}

