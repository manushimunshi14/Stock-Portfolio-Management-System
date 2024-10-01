package GUI.Controller;

import GUI.Authentication.TransactionGUI;
import GUI.Listener.CustomerTransactionCompleteListener;
import GUI.Listener.StockPriceChangeListener;
import GUI.Listener.StockTransactionCompleteListener;
import GUI.Listener.TransactionListener;
import GUI.Utils.PromptSetup;
import System.Stock.Stock;
import System.User.Customer;

public class TransactionController extends PromptController implements TransactionListener {
  private TransactionGUI transactionGUI;
  private int maxCount;
  private String tradingOptions;
  private double maxBalance;
  private double currentPrice;
  private Stock stock;
  private Customer customer;
  private CustomerTransactionCompleteListener customerTransactionListener;
  private StockTransactionCompleteListener StockTransactionListener;
  private StockPriceChangeListener stockPriceChangeListener;

  public static final String SOLD = "SOLD";
  public static final String PURCHASE = "PURCHASE";
  public static final String WITHDRAW = "WITHDRAW";
  public static final String DEPOSIT = "DEPOSIT";
  public static final String CHANGE = "CHANGE";

  public TransactionGUI getTransactionGUI() {
    return this.transactionGUI;
  }

  public void setCustomerTransactionCompleteListener(CustomerTransactionCompleteListener customerTransactionListener) {
    this.customerTransactionListener = customerTransactionListener;
  }

  public void setStockTransactionCompleteListener(StockTransactionCompleteListener StockTransactionListener) {
    this.StockTransactionListener = StockTransactionListener;
  }

  public void setStockPriceChangeListener(StockPriceChangeListener stockPriceChangeListener) {
    this.stockPriceChangeListener = stockPriceChangeListener;
  }

  public void setTransactionGUI(TransactionGUI transactionGUI) {
    this.transactionGUI = transactionGUI;
  }

  public String getTradingOptions() {
    return this.tradingOptions;
  }

  public Stock getStock() {
    return this.stock;
  }

  public Customer getCustomer() {
    return this.customer;
  }

  TransactionController(MainController mainController) {
    super(mainController);
    transactionGUI = new TransactionGUI(this);
  }

  @Override
  public void openPage() {
    if (transactionGUI.getFrame() == null) {
      transactionGUI.setFrame();
    }
    transactionGUI.initializeGUI();
  }

  @Override
  public void setWindowsSize(int width, int height) {
    transactionGUI.setWindowsSize(width, height);
  }

  @Override
  public void getConform(String amountString) {

    switch (tradingOptions) {
      case PURCHASE:
      case SOLD:
        checkCountValidate(amountString);
        break;
      case WITHDRAW:
      case DEPOSIT:
        checkBalanceValidate(amountString);
        break;
      case CHANGE:
        checkPriceValidate(amountString);
        break;
      default:
        break;
    }
  }

  private void checkCountValidate(String countString) {
    String validationError = validateCount(countString);
    if (!validationError.isEmpty()) {
      int[] windDowSize = new int[]{400, 200};
      PromptSetup.setErrorReport(validationError, mainController, this, windDowSize);
      return;
    }

    int count = Integer.parseInt(countString);
    if (count < 0 || count > maxCount) {
      String error = "\n\nThe total count must be between 0 and " + maxCount + "!\n";
      int[] windDowSize = new int[]{350, 200};
      PromptSetup.setErrorReport(error, mainController, this, windDowSize);
    } else {
      if (StockTransactionListener != null) {
        StockTransactionListener.onTransactionComplete(stock, count);
      }
      transactionGUI.closeWindow();
    }
  }

  private void checkBalanceValidate(String amountString) {
    String validationError = validateAmount(amountString);
    if (!validationError.isEmpty()) {
      int[] windDowSize = new int[]{400, 200};
      PromptSetup.setErrorReport(validationError, mainController, this, windDowSize);
      return;
    }

    double amount = Double.parseDouble(amountString);
    if (amount < 0.01 || amount > maxBalance) {
      String error = "\n\nThe total amount must between $0.01 and " + String.format("%.2f", maxBalance)
          + "!\n";
      int[] windDowSize = new int[]{360, 200};
        PromptSetup.setErrorReport(error, mainController, this, windDowSize);
    } else {
      if (customerTransactionListener != null) {
        customerTransactionListener.onTransactionComplete(customer, amount);
      }
      transactionGUI.closeWindow();
    }
  }

  private void checkPriceValidate(String priceString) {
    String validationError = validateAmount(priceString);
    if (!validationError.isEmpty()) {
      int[] windDowSize = new int[]{400, 200};
      PromptSetup.setErrorReport(validationError, mainController, this, windDowSize);
      return;
    }

    double price = Double.parseDouble(priceString);
    if (price < 0.01) {
      String error = "\n\nThe new price must large than $0.01!\n";
      int[] windDowSize = new int[]{350, 200};
      PromptSetup.setErrorReport(error, mainController, this, windDowSize);
    } else {
      if (stockPriceChangeListener != null) {

        stockPriceChangeListener.onStockPriceChangeComplete(stock, price);
      }
      transactionGUI.closeWindow();
    }
  }

  public void closeWindow() {
    transactionGUI.closeWindow();
  }

  public void setMaxCount(int maxCount) {
    this.maxCount = maxCount;
  }

  public int getMaxCount() {
    return maxCount;
  }

  public double getMaxBalance() {
    return maxBalance;
  }

  public void setMaxBalance(double maxBalance) {
    this.maxBalance = maxBalance;
  }

  public void setCurrentPrice(double currentPrice) {
    this.currentPrice = currentPrice;
  }

  public void setTradingOptions(String tradingOptions) {
    this.tradingOptions = tradingOptions;
  }

  public void setStock(Stock stock) {
    this.stock = stock;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public String validateCount(String countString) {
    StringBuilder errors = new StringBuilder();

    if (countString == null || countString.trim().isEmpty()) {
      errors.append("Count cannot be empty.\n");
    } else {
      try {
        int countValue = Integer.parseInt(countString);
        if (countValue <= 0) {
          errors.append("Count must be a positive integer.\n");
        }
      } catch (NumberFormatException e) {
        errors.append("Count must be a valid integer.\n");
      }
    }
    return errors.toString();
  }

  public String validateAmount(String amountString) {
    StringBuilder errors = new StringBuilder();
    if (amountString == null || amountString.trim().isEmpty()) {
      errors.append("Count cannot be empty.\n");
    } else {
      try {
        double priceValue = Double.parseDouble(amountString);
        if (priceValue <= 0) {
          errors.append("\nPrice must be a positive number.\n");
        } else if (!amountString.matches("\\d+\\.\\d{2}") && !amountString.matches("\\d+")) {
          errors.append("\nPrice must be a positive number with two decimal places or a whole number.\n");
        }
      } catch (NumberFormatException e) {
        errors.append("\nPrice must be a valid number.\n");
      }
    }
    return errors.toString();
  }

  public boolean isValidateInteger(String countString) {
    if (countString == null || countString.trim().isEmpty()) {
      return false;
    } else {
      try {
        int countValue = Integer.parseInt(countString);
        if (countValue <= 0)
          return false;
      } catch (NumberFormatException e) {
        return false;
      }
    }
    return true;
  }

  public boolean isValidateDouble(String amountString) {
    if (amountString == null || amountString.trim().isEmpty()) {
      return false;
    } else {
      try {
        double priceValue = Double.parseDouble(amountString);
        if (priceValue < 0) {
          return false;
        }
        // 检查是否符合 "数字.数字数字" 或 "数字" 的格式
        if (!amountString.matches("\\d+\\.\\d{2}") && !amountString.matches("\\d+")) {
          return false;
        }
      } catch (NumberFormatException e) {
        return false;
      }
    }
    return true;
  }

}
