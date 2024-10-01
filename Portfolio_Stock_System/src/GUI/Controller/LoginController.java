package GUI.Controller;

import javax.swing.*;

import GUI.Authentication.*;
import GUI.Listener.LoginListener;
import GUI.Utils.PromptSetup;
import Services.AuthenticationService;
import System.Account.LoginResult;
import System.User.Admin;
import System.User.Customer;

public class LoginController extends Controller implements LoginListener {
  private LoginGUI userLogin;

  public LoginController(MainController mainController) {
    super(mainController);
    this.userLogin = new LoginGUI(mainController.getLoginController(), mainController.getSignupController());
  }

  @Override
  public boolean isLogin(String username, String password) {
    return AuthenticationService.login(username, password);
  }

  @Override
  public void onLogin(String username, String password) {
    if (isLogin(username, password)) {
      LoginResult loginResult = getLoginResult(username);
      if (AuthenticationService.isManager(loginResult.getManagerId())) {
        SwingUtilities.invokeLater(() -> {
          ManagerController managerController = mainController.getManagerController();
          Admin admin = new Admin(loginResult.getUserId(), loginResult.getDisplayName(), username,
              loginResult.getManagerId(),
              loginResult.getPasswordId());
          managerController.setUser(admin);
          managerController.openPage();
        });
      } else {
        SwingUtilities.invokeLater(() -> {
          CustomerController userController = mainController.getUserController();
          Customer customer = new Customer(loginResult.getUserId(), loginResult.getDisplayName(), username,
              loginResult.getManagerId(),
              loginResult.getPasswordId());
          userController.setUser(customer);
          userController.openPage();
        });
      }
    } else {
      int[] windowsSize = new int[] { 280, 120 };
      String errorReport = "\nLogin failed!\n";
      SwingUtilities.invokeLater(() -> PromptSetup.setErrorReport(errorReport, windowsSize, mainController));
    }
  }

  public LoginResult getLoginResult(String username) {
    return AuthenticationService.getLoginResult(username);
  }

  @Override
  public void onLoginRequested() {
    if (userLogin == null) {
      userLogin = new LoginGUI(mainController.getLoginController(), mainController.getSignupController());
    }
    openPage();
  }

  @Override
  public void openPage() {
    userLogin.setWindowsSize(280, 170);
    if (userLogin.getFrame() == null)
      userLogin.setFrame();
    userLogin.initializeGUI();
  }

  public void closeWindow() {
    if (userLogin != null && userLogin.getFrame() != null)
      userLogin.closeWindow();
  }

  public LoginGUI getLoginGUI() {
    return userLogin;
  }
}
