package GUI.Utils;

import GUI.Listener.CustomerListener;
import GUI.Listener.ManagerListener;
import System.Account.DerivativeRequest;
import System.Account.SignupRequest;
import System.Stock.Stock;
import System.User.Customer;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


public class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;

    public ButtonEditor(JCheckBox checkBox, JTable table, ManagerListener managerListener) {
        super(checkBox);

        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> {
            int row = table.convertRowIndexToModel(table.getEditingRow());
            // Get row data
            int requestId = (int) table.getModel().getValueAt(row, 0); // Assuming Request ID is in the first column

            // Perform actions based on the button label
            if (label.equals("Approve")) {
                String displayName = (String) table.getModel().getValueAt(row, 1);
                String userName = (String) table.getModel().getValueAt(row, 2);
                String password = (String) table.getModel().getValueAt(row, 3);
                SignupRequest ApproveSignupRequest = new SignupRequest(requestId, displayName, userName, password);

                managerListener.onApprove(ApproveSignupRequest);
                // Perform approval operation

            } else if (label.equals("Disapprove")) {
                String displayName = (String) table.getModel().getValueAt(row, 1);
                String userName = (String) table.getModel().getValueAt(row, 2);
                String password = (String) table.getModel().getValueAt(row, 3);
                SignupRequest DisapproveSignupRequest = new SignupRequest(requestId, displayName, userName, password);

                managerListener.onDisapprove(DisapproveSignupRequest);
                // Perform disapproval operation
            } else if (label.equals("Promote")) {
                String displayName = (String) table.getModel().getValueAt(row, 1);
                String userName = (String) table.getModel().getValueAt(row, 2);
                double balance = (double) table.getModel().getValueAt(row, 3);
                DerivativeRequest derivativeRequest = new DerivativeRequest(requestId, displayName, userName, balance);

                managerListener.onPromote(derivativeRequest);
                // Perform disapproval operation

            } else if (label.equals("Delete")) {
                String stockSymbol = (String) table.getModel().getValueAt(row, 1);
                String stockName = (String) table.getModel().getValueAt(row, 2);
                Stock currentStock = new Stock(requestId, stockSymbol, stockName);

                managerListener.onDeleteStock(currentStock);
                // Perform disapproval operation
            } else if (label.equals("View")) {
                String userName = (String) table.getModel().getValueAt(row, 1);
                String displayName = (String) table.getModel().getValueAt(row, 2);
                Customer customer = new Customer(requestId, userName, displayName);

                managerListener.onViewCustomerProfit(customer);

                System.out.println("View - requestId:" + requestId + ", userName:" + userName);

            }

            isPushed = false;
            fireEditingStopped();
        });
    }

    public ButtonEditor(JCheckBox checkBox, JTable table, CustomerListener customerListener) {
        super(checkBox);
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> {
            int row = table.convertRowIndexToModel(table.getEditingRow());
            // Get row data
            if (label.equals("Sell")) {
                int stockId = (int) table.getModel().getValueAt(row, 0);
                String stockSymbol = (String) table.getModel().getValueAt(row, 1);
                String stockName = (String) table.getModel().getValueAt(row, 2);
                Stock currentStock = new Stock(stockId, stockSymbol, stockName);

                List<Map<String, Object>> stockHistory = currentStock.getStockSales();
                double LastPrice = ((BigDecimal) stockHistory.get(stockHistory.size() - 1).get("last_sale")).doubleValue();
                currentStock.setCurrPrice(LastPrice);

                // Perform disapproval operation
                System.out.println("Sale Stock Id: " + stockId + "\nStock Symbol: " + stockSymbol + "\nStock Stock Name: " + stockName);

                customerListener.onSaleOut(currentStock);

            }
        });
    }


    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        if (isSelected) {
            button.setForeground(table.getSelectionForeground());
            button.setBackground(table.getSelectionBackground());
        } else {
            button.setForeground(table.getForeground());
            button.setBackground(table.getBackground());
        }
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            // Button has been clicked
        }
        isPushed = false;
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    @Override
    protected void fireEditingStopped() {
        // Notify the listeners that editing has stopped
        super.fireEditingStopped();
    }
}
