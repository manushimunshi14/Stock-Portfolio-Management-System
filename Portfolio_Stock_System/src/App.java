import javax.swing.SwingUtilities;

import GUI.Controller.HomepageController;
import GUI.Controller.LoginController;
import GUI.Controller.MainController;

public class App {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      MainController mainController = new MainController();
      LoginController controller = new LoginController(mainController);
      new HomepageController();
//      controller.onLoginRequested();
    });
  }
}
