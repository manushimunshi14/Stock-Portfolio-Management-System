package GUI.Listener;


public interface PromptListener {

  void setMassage(String massage);

  String getMassage();

  void setWindowsSize(int width, int height);

  void setTitle(String title);

  String getTitle();

  void onPromptClosed();

  void closePromptAndTransaction();

  void setOperationStatus(String operationStatus);

  String getOperationStatus();
}
