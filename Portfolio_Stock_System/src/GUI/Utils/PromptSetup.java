package GUI.Utils;

import GUI.Controller.MainController;
import GUI.Controller.PromptController;
import GUI.Controller.TransactionController;

public class PromptSetup {

  public static void setErrorReport(String report, int[] windowsSize, MainController mainController) {
    PromptController promptController = new PromptController(mainController);
    promptController.setOperationStatus(PromptController.FAILURE);
    promptController.setTitle("Error");
    promptController.setWindowsSize(windowsSize[0], windowsSize[1]);
    promptController.setMassage(report);
    promptController.openPage();
  }

  public static void setSuccessReport(String report, int[] windowsSize, MainController mainController) {
    PromptController promptController = new PromptController(mainController);
    promptController.setOperationStatus(PromptController.SUCCESS);
    promptController.setTitle("Success");
    promptController.setWindowsSize(windowsSize[0], windowsSize[1]);
    promptController.setMassage(report);
    promptController.openPage();
  }


  public static void setErrorReport(String report, MainController mainController,
      TransactionController transactionController, int[] windowSize) {
    PromptController promptController = new PromptController(mainController, transactionController);
    promptController.setOperationStatus(PromptController.FAILURE);
    promptController.setTitle("Error");
    promptController.setWindowsSize(windowSize[0], windowSize[1]);
    promptController.setMassage(report);
    promptController.openPage();
  }

}
