package GUI.OtherGUI.Managermodule;

import GUI.Authentication.TimeSeriesChartPanel;
import GUI.Listener.ManagerListener;
import GUI.Utils.GuiUtils;
import System.Stock.Stock;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class PriceModifier {
    private ManagerListener managerListener;
    private JPanel stockListPanel;
    private JPanel stockDetailPanel;
    private List<Consumer<JPanel>> stockDetailPanelListeners = new ArrayList<>();

    public PriceModifier(ManagerListener managerListener) {
        this.managerListener = managerListener;
        stockDetailPanel = new JPanel();
        stockDetailPanel.setLayout(new FlowLayout());
        initializeStockListPanel();
    }

    public void initializeStockListPanel() {
        stockListPanel = new JPanel(new BorderLayout());

        JLabel availableStocksLabel = new JLabel("Available Stocks:");
        stockListPanel.add(availableStocksLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        List<Stock> allStocks = managerListener.getAllStockList();
        for (Stock stock : allStocks) {
            JButton stockButton = new JButton(stock.getSymbol());
            stockButton.addActionListener(e -> showStockDetails(stock));
            buttonPanel.add(stockButton);
        }

        // add scrollPane to stockListPanel
        JScrollPane scrollPane = new JScrollPane(buttonPanel);
        stockListPanel.add(scrollPane, BorderLayout.CENTER);
    }

    public void addStockDetailPanelListener(Consumer<JPanel> listener) {
        stockDetailPanelListeners.add(listener);
    }

    private void showStockDetails(Stock stock) {
        List<Map<String, Object>> stockHistory = stock.getStockSales();

        double LastPrice = ((BigDecimal) stockHistory.get(stockHistory.size() - 1).get("last_sale")).doubleValue();
        stock.setCurrPrice(LastPrice);

        JScrollPane stockDetailTable = GuiUtils.stockDetailTable(stockHistory, stock);
        TimeSeriesChartPanel chartPanel = new TimeSeriesChartPanel(stockHistory);

        JButton changePriceButton = new JButton("Change Price");
        changePriceButton.addActionListener(e -> managerListener.handlePriceChange(stock));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(changePriceButton);

        for (Consumer<JPanel> listener : stockDetailPanelListeners) {
            listener.accept(stockDetailPanel);
        }

        stockDetailPanel.removeAll();
        stockDetailPanel.setLayout(new BorderLayout());
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, stockDetailTable, chartPanel);
        splitPane.setResizeWeight(0.025);
        stockDetailPanel.add(splitPane, BorderLayout.CENTER);
        stockDetailPanel.add(buttonPanel, BorderLayout.SOUTH);
        stockDetailPanel.revalidate();
        stockDetailPanel.repaint();
    }

    public JPanel getStockListPanel() {
        return stockListPanel;
    }

    public JPanel getStockDetailPanel() {
        return stockDetailPanel;
    }
}
