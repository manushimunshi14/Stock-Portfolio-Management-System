package GUI.Controller;

import javax.swing.*;

import DatabaseReader.Stockdb;
import GUI.Authentication.LoadingWindow;
import GUI.Authentication.ManagerGUI;
import GUI.Listener.AddStockListener;
import GUI.OtherGUI.Managermodule.AddStockGUI;
import GUI.Utils.PromptSetup;
import System.Stock.Stock;
import System.Stock.StockManager;

public class AddStockController extends Controller implements AddStockListener {
  private AddStockGUI addStockGUI;
  private ManagerGUI managerGUI;
  private Stockdb stockdb;

  public AddStockController(MainController mainController) {
    super(mainController);
    addStockGUI = new AddStockGUI(this);
  }

  public void setManagerGUI(ManagerGUI managerGUI){
    this.managerGUI = managerGUI;
  }


  @Override
  protected void openPage() {
    if (addStockGUI.getFrame() == null) {
      addStockGUI.setFrame();
    }
    addStockGUI.initializeGUI();
  }

  @Override
  public void onConfirm(String symbol, String name, String price) {
    if (symbol != null && name != null && price != null) {
      if (isValidate(symbol, name, price)) {
        double priceValue = Double.parseDouble(price);
        Stock newStock = new Stock(symbol, name, priceValue);
        handleAddStock(newStock);
        addStockGUI.closeWindow();
      } else {
        SwingUtilities.invokeLater(() -> {
          String errorMassage = validateStockInput(symbol, name, price);
          int[] windowsSize = new int[] { 380, 170 };
          PromptSetup.setErrorReport(errorMassage, windowsSize, mainController);
        });
      }
    } else {
      SwingUtilities.invokeLater(() -> {
        String errorMassage = "\nSymbol, name, or price cannot be empty!\n";
        int[] windowsSize = new int[] { 350, 130 };
        PromptSetup.setErrorReport(errorMassage, windowsSize, mainController);
      });
    }
  }

  @Override
  public void onCancel() {
    addStockGUI.closeWindow();
  }

  private void handleAddStock(Stock stock) {
    stockdb = new Stockdb();
    StockManager stockManager = new StockManager(stockdb);
    LoadingWindow loadingWindow = new LoadingWindow();
      SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
        @Override
        protected Void doInBackground() throws Exception {


          int stockId = stock.getStockId();
          String stockSymbol = stock.getSymbol();
          String stockName = stock.getStockName();
          double stockCurrentPrice = stock.getCurrPrice();
          int[] windowsSize = new int[]{360,260};

          // Add new stock to database
          stockManager.addNewStock(stockSymbol,stockName,stockCurrentPrice);

          String report = String.format(
                  "\nAdding new stock:\nStock ID: %d\nStock Symbol: %s\nStock Name: %s\nCurrent Price: $%.2f\n",
                  stockId, stockSymbol, stockName, stockCurrentPrice
          );
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
  }


  private String validateStockInput(String symbol, String name, String price) {
    StringBuilder errors = new StringBuilder();

    //  Check symbol
    if (!symbol.matches("[A-Z]{1,5}")) {
      errors.append("\nSymbol must consist of 1 to 5 uppercase letters.\n");
    }

    // Check name
    if (!name.endsWith("Common Stock")) {
      errors.append("\nName must end with 'Common Stock'.\n");
    }

    // check price
    try {
      double priceValue = Double.parseDouble(price);
      if (priceValue <= 0 || !price.matches("\\d+\\.\\d{2}")) {
        errors.append("\nPrice must be a positive number with two decimal places.\n");
      }
    } catch (NumberFormatException e) {
      errors.append("\nPrice must be a valid number.\n");
    }
    return errors.toString();
  }

private boolean isValidate(String symbol, String name, String price) {

    //  Check symbol
    if (!symbol.matches("[A-Z]{1,5}"))
      return false;

    // Check name
    if (!name.endsWith("Common Stock"))
      return false;

    // check price
    try {
        double priceValue = Double.parseDouble(price);
        if (priceValue <= 0 || !price.matches("\\d+\\.\\d{2}"))
          return false;
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
}


  public static void main(String[] agr) {
    MainController mainController = new MainController();
    AddStockController addStockController = new AddStockController(mainController);
    addStockController.openPage();
  }



}
