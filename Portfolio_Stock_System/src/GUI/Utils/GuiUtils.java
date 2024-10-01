package GUI.Utils;

import System.Account.CustomerAccount;
import System.Stock.Stock;
import System.Stock.TransactionDetails;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

public class GuiUtils {
    public static final Font buttonFont = new Font("Arial", Font.PLAIN, 16);

    public static JLabel createLabel(String text) {
        return new JLabel(text);
    }

    public static JTextField createTextField() {
        JTextField textField = new JTextField(20);
        return textField;
    }

    public static JLabel createLabel(String text, int x, int y, int width, int height) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, height);
        return label;
    }

    public static JCheckBox createJCheckBox(String text, int x, int y, int width, int height) {
        JCheckBox showPasswordCheckBox = new JCheckBox(text);
        showPasswordCheckBox.setBounds(x, y, width, height);
        return showPasswordCheckBox;
    }

    public static JCheckBox createJCheckBox(String text) {
        return new JCheckBox(text);
    }

    public static JTextField createTextField(int x, int y, int width, int height) {
        JTextField textField = new JTextField(20);
        textField.setBounds(x, y, width, height);
        return textField;
    }

    public static JPasswordField createPasswordField(int x, int y, int width, int height) {
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setBounds(x, y, width, height);
        return passwordField;
    }

    public static JButton createButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        return button;
    }

    public static JButton createButton(String text) {
        return new JButton(text);
    }

    public static JFormattedTextField createDoubleField(int x, int y, int width, int height) {
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setGroupingUsed(false);
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Double.class);
        formatter.setAllowsInvalid(false);
        formatter.setMinimum(0.0);

        JFormattedTextField doubleField = new JFormattedTextField(formatter);
        doubleField.setBounds(x, y, width, height);
        doubleField.setColumns(20);
        return doubleField;
    }

    public static JFormattedTextField createDoubleField() {
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setGroupingUsed(false);
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Double.class);
        formatter.setAllowsInvalid(false);
        formatter.setMinimum(0.01);

        JFormattedTextField doubleField = new JFormattedTextField(formatter);
        doubleField.setColumns(20);
        return doubleField;
    }

    public static JScrollPane stockDetailTable(List<Map<String, Object>> stockHistory, Stock stock) {
        String[] columnNames = {"Stock ID", "Stock Symbol", "Stock Name", "Last Price", "Current Price", "Volatility", "Price Change"};
        double LastPrice = stockHistory.size() == 1 ? 0 : ((BigDecimal) stockHistory.get(stockHistory.size() - 2).get("last_sale")).doubleValue();
        double volatility = StockCalculator.calculateVolatility(stockHistory);
        double priceChange = StockCalculator.calculateLastPriceChange(stockHistory);

        volatility = Double.parseDouble(String.format("%.2f", volatility));
        priceChange = Double.parseDouble(String.format("%.2f", priceChange));

        Object[][] data = new Object[1][7];
        data[0][0] = stock.getStockId();
        data[0][1] = stock.getSymbol();
        data[0][2] = stock.getStockName();
        data[0][3] = LastPrice;
        data[0][4] = stock.getCurrPrice();
        data[0][5] = volatility;
        data[0][6] = priceChange;
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
        };
        JTable purchasedStockTable = new JTable(model);
        purchasedStockTable.getColumnModel().getColumn(5).setCellRenderer(new VolatilityCellRenderer());
        purchasedStockTable.getColumnModel().getColumn(6).setCellRenderer(new VolatilityCellRenderer());


        JTableHeader header = purchasedStockTable.getTableHeader();
        header.setBackground(Color.GRAY);
        Font currentFont = header.getFont();
        Font boldFont = new Font(currentFont.getFontName(), Font.BOLD, currentFont.getSize());
        header.setFont(boldFont);

        JScrollPane scrollPane = new JScrollPane(purchasedStockTable);
        int tableHeight = purchasedStockTable.getRowHeight() * purchasedStockTable.getRowCount() + header.getHeight();
        scrollPane.setPreferredSize(new Dimension(500, tableHeight + 10));

        return scrollPane;
    }

    public static JScrollPane transactionDetailsTable(List<TransactionDetails> transactionDetailsList) {

        String[] columnNames = {"User ID", "Stock Symbol", "Stock Price", "Stock Quantity", "Total Transaction", "Transaction Time"};
        Object[][] data = new Object[transactionDetailsList.size()][6];
        for (int i = 0; i < transactionDetailsList.size(); i++) {
            TransactionDetails transactionDetails = transactionDetailsList.get(i);

            double totalConsumption = transactionDetails.getTotalConsumption();
            double transactionPrice = transactionDetails.getTransactionPrice();
            totalConsumption = Double.parseDouble(String.format("%.2f", totalConsumption));
            transactionPrice = Double.parseDouble(String.format("%.2f", transactionPrice));

            data[i][0] = transactionDetails.getUserId();
            data[i][1] = transactionDetails.getStockSymbol();
            data[i][2] = transactionPrice;
            data[i][3] = transactionDetails.getCount();
            data[i][4] = totalConsumption;
            data[i][5] = transactionDetails.getTradingDate();
        }
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
        };

        JTable registrationRequestTable = new JTable(model);

        registrationRequestTable.getColumnModel().getColumn(4).setCellRenderer(new VolatilityCellRenderer());

        JTableHeader header = registrationRequestTable.getTableHeader();
        header.setBackground(Color.GRAY);
        Font currentFont = header.getFont();
        Font boldFont = new Font(currentFont.getFontName(), Font.BOLD, currentFont.getSize());
        header.setFont(boldFont);

        return new JScrollPane(registrationRequestTable);
    }

    public static JScrollPane customerAccountTable(CustomerAccount customerAccount) {
        String[] columnNames = {"User ID", "Realized Profit", "Unrealized Profit"};
        Object[][] data = new Object[1][3];
        
        double realizedProfit = customerAccount.getRealizedProfit();
        double unrealizedProfit = customerAccount.getUnrealizedProfit();
        realizedProfit = Double.parseDouble(String.format("%.2f", realizedProfit));
        unrealizedProfit = Double.parseDouble(String.format("%.2f", unrealizedProfit));

        data[0][0] = customerAccount.getUserId();
        data[0][1] = realizedProfit;
        data[0][2] = unrealizedProfit;

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
        };

        JTable customerProfitTable = new JTable(model);
        JTableHeader header = customerProfitTable.getTableHeader();

        header.setBackground(Color.GRAY);
        Font currentFont = header.getFont();
        Font boldFont = new Font(currentFont.getFontName(), Font.BOLD, currentFont.getSize());
        header.setFont(boldFont);

        JScrollPane scrollPane = new JScrollPane(customerProfitTable);
        int tableHeight = customerProfitTable.getRowHeight() * customerProfitTable.getRowCount() + header.getHeight();
        scrollPane.setPreferredSize(new Dimension(500, tableHeight + 20));

        return scrollPane;
    }


}

