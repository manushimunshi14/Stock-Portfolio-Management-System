package GUI.OtherGUI.Managermodule;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.List;

import GUI.Listener.ManagerListener;
import GUI.Utils.ButtonEditor;
import GUI.Utils.ButtonRenderer;
import GUI.Utils.GuiUtils;
import System.Stock.Stock;

public class StockQuantityChanger {
  private JButton manageStocksButton;
  private JButton increaseButton;
  private JButton decreaseButton;
  private JPanel responsePanel;
  private ManagerListener managerListener;

  public StockQuantityChanger(ManagerListener managerListener) {
    initializeButton();
    responsePanel = new JPanel();
    responsePanel.setLayout(new FlowLayout());
    this.managerListener = managerListener;
  }

  private void initializeButton() {
    manageStocksButton = new JButton("Manage Stocks");
    manageStocksButton.setFont(GuiUtils.buttonFont);
    manageStocksButton.addActionListener(e -> handleManageStocksAction());
  }

  private void handleManageStocksAction() {
    if (increaseButton == null && decreaseButton == null) {
      increaseButton = new JButton("Add New Stock");
      decreaseButton = new JButton("Delete A Stock");

      increaseButton.addActionListener(e -> increaseStock());
      decreaseButton.addActionListener(e -> deleteStock());
    }
    responsePanel.removeAll();
    responsePanel.add(increaseButton);
    responsePanel.add(decreaseButton);
    responsePanel.revalidate();
    responsePanel.repaint();

  }

  private void increaseStock() {
    responsePanel.removeAll();
    responsePanel.revalidate();
    responsePanel.repaint();
    managerListener.onAddStock();
  }

  private void deleteStock() {
    List<Stock> allStocks = managerListener.getAllStockList();
    String[] columnNames = { "Stock ID", "Stock Symbol", "Stock Name", "Delete"};
    Object[][] data = new Object[allStocks.size()][4];

    for (int i = 0; i < allStocks.size(); i++) {
      Stock stock = allStocks.get(i);
      data[i][0] = stock.getStockId();
      data[i][1] = stock.getSymbol();
      data[i][2] = stock.getStockName();
      data[i][3] = "Delete";
    }
    DefaultTableModel model = new DefaultTableModel(data, columnNames) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return column == 3;
      }
    };

    JTable allStockInMarketTable = new JTable(model);
    JTableHeader header = allStockInMarketTable.getTableHeader();

    header.setBackground(Color.GRAY);
    Font currentFont = header.getFont();
    Font boldFont = new Font(currentFont.getFontName(), Font.BOLD, currentFont.getSize());
    header.setFont(boldFont);

    // Set custom cell renderer and editor for the approve and disapprove button columns
    allStockInMarketTable.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
    allStockInMarketTable.getColumnModel().getColumn(3)
        .setCellEditor(new ButtonEditor(new JCheckBox(), allStockInMarketTable, managerListener));

    JScrollPane scrollPane = new JScrollPane(allStockInMarketTable);

    responsePanel.removeAll();
    responsePanel.add(scrollPane, BorderLayout.CENTER);
    responsePanel.revalidate();
    responsePanel.repaint();
  }

  public JButton getMainButton() {
    return manageStocksButton;
  }

  public JButton getIncreaseButton() {
    return increaseButton;
  }

  public JButton getDecreaseButton() {
    return decreaseButton;
  }

  public JPanel getResponsePanel() {
    return responsePanel;
  }
}
