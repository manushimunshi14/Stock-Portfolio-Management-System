package GUI.Controller;


public class MainController {
  private LoginController loginController;
  private SignupController signupController;
  private CustomerController userController;
  private ManagerController managerController;
  private PromptController errorController;

  public MainController() {
    loginController = new LoginController(this);
    signupController = new SignupController(this);
    userController = new CustomerController(this);
    managerController = new ManagerController(this);
    errorController = new PromptController(this);
  }


  public LoginController getLoginController() {
    return loginController;
  }

  public SignupController getSignupController() {
    return signupController;
  }

  public CustomerController getUserController() {
    return userController;
  }

  public ManagerController getManagerController() {
    return managerController;
  }

  public PromptController getPromptControllerController() {
    return errorController;
  }

}
