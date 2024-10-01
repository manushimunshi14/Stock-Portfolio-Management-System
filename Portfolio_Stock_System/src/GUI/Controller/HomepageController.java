package GUI.Controller;

import GUI.Authentication.WelcomeGUI;
import GUI.Listener.HomepageListener;


public class HomepageController implements HomepageListener {
    private MainController mainController;
    private WelcomeGUI welcomeGUI;

    public HomepageController() {
        // Create and display the WelcomeGUI
        mainController = new MainController();
        welcomeGUI = new WelcomeGUI(this);
    }

    @Override
    public void onLoginButtonPressed() {
        // Dispose the welcome screen
        welcomeGUI.dispose();
        LoginController controller = new LoginController(mainController);
        controller.onLoginRequested();
    }


    public static void main(String[] args) {
        new HomepageController();
    }
}
