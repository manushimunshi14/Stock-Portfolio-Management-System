package GUI.OtherGUI.CustomerModule;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import GUI.Authentication.LoadingWindow;
import GUI.Listener.CustomerListener;
import GUI.Utils.ButtonEditor;
import GUI.Utils.ButtonRenderer;
import GUI.Utils.GuiUtils;
import System.Stock.Stock;

public class ViewPurchasedStock {
  private CustomerListener customerListener;
  private JButton viewPurchasedStocksButton;
  private JPanel viewPurchasedStocksPanel;

  public ViewPurchasedStock(CustomerListener customerListener) {
    this.customerListener = customerListener;
    viewPurchasedStocksPanel = new JPanel();
    viewPurchasedStocksPanel.setLayout(new FlowLayout());
    initializeBaseJPanel();
  }

  private void initializeBaseJPanel() {
    viewPurchasedStocksButton = GuiUtils.createButton("View Purchased Stocks");
    viewPurchasedStocksButton.setFont(GuiUtils.buttonFont);
    viewPurchasedStocksButton.addActionListener(e -> viewPurchasedStocks());
  }

  public void viewPurchasedStocks() {
    LoadingWindow loadingWindow = new LoadingWindow();

    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() throws Exception {
        viewPurchasedStocksPanel.removeAll();
        viewPurchasedStocksPanel.setLayout(new BorderLayout(15, 15));
        viewPurchasedStocksPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        List<Stock> purchasedStocksList = customerListener.getPurchasedStockFromPortfolio();

        String[] columnNames = { "Stock ID", "Stock Symbol", "Stock Name", "total count", "Sell" };
        Object[][] data = new Object[purchasedStocksList.size()][5];
        for (int i = 0; i < purchasedStocksList.size(); i++) {
          Stock purchasedStock = purchasedStocksList.get(i);
          data[i][0] = purchasedStock.getStockId();
          data[i][1] = purchasedStock.getSymbol();
          data[i][2] = purchasedStock.getStockName();
          data[i][3] = customerListener.getPurchasedStockCount(purchasedStock.getStockId());
          data[i][4] = "Sell";
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
          @Override
          public boolean isCellEditable(int row, int column) {
            return column == 4;
          }
        };

        JTable registrationRequestTable = new JTable(model);
        JTableHeader header = registrationRequestTable.getTableHeader();

        header.setBackground(Color.GRAY);
        Font currentFont = header.getFont();
        Font boldFont = new Font(currentFont.getFontName(), Font.BOLD, currentFont.getSize());
        header.setFont(boldFont);

        // Set custom cell renderer and editor for the approve and disapprove button columns
        registrationRequestTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        registrationRequestTable.getColumnModel().getColumn(4)
            .setCellEditor(new ButtonEditor(new JCheckBox(), registrationRequestTable, customerListener));
        JScrollPane scrollPane = new JScrollPane(registrationRequestTable);
        viewPurchasedStocksPanel.add(scrollPane, BorderLayout.CENTER);
        return null;
      }

      @Override
      protected void done() {
        loadingWindow.closeWindow();

        viewPurchasedStocksPanel.revalidate();
        viewPurchasedStocksPanel.repaint();
      }
    };
    worker.execute();
  }

  public JButton getViewPurchasedStocksButton() {
    return viewPurchasedStocksButton;
  }

  public JPanel getViewPurchasedStocksPanel() {
    return viewPurchasedStocksPanel;
  }
}
