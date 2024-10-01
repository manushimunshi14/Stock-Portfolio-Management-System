package GUI.Controller;

import GUI.Authentication.PromptGUI;
import GUI.Listener.PromptListener;


public class PromptController extends Controller implements PromptListener {
  private PromptGUI promptGUI;
  private String massage;
  private String title;
  private TransactionController transactionController;
  private String operationStatus;

  public static final String SUCCESS = "SUCCESS";
  public static final String FAILURE = "FAILURE";

  public PromptController(MainController mainController) {
    super(mainController);
    promptGUI = new PromptGUI(this);
  }

  public PromptController(MainController mainController, TransactionController transactionController) {
    super(mainController);
    this.transactionController = transactionController;
    promptGUI = new PromptGUI(this);
  }

  @Override
  public void openPage() {
    if (promptGUI.getFrame() == null) {
      promptGUI.setFrame();
    }
    promptGUI.initializeGUI();
  }
    @Override
  public void setOperationStatus(String operationStatus) {
    this.operationStatus = operationStatus;
  }

  @Override
  public String getOperationStatus() {
    return this.operationStatus;
  }


  @Override
  public void setWindowsSize(int width, int height) {
    promptGUI.setWindowsSize(width, height);
  }

  @Override
  public void setMassage(String massage) {
    this.massage = massage;
  }

  @Override
  public String getMassage() {
    return this.massage;
  }

  @Override
  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public String getTitle() {
    return this.title;
  }

  @Override
  public void closePromptAndTransaction() {
    promptGUI.closeWindow();
    onPromptClosed();
  }

  @Override
  public void onPromptClosed() {
    if (transactionController != null) {
      transactionController.closeWindow();
    }
  }
}
