package GUI.Authentication;


import GUI.Controller.LoginController;
import GUI.Controller.MainController;
import GUI.Controller.SignupController;
import GUI.Listener.LoginListener;
import GUI.Listener.SignupListener;
import GUI.Utils.GUIFunction;
import GUI.Utils.GuiUtils;

import javax.swing.*;
import java.awt.*;

public class LoginGUI extends BaseGUI implements GUIFunction {
    protected LoginListener loginListener;
    protected SignupListener signupListener;


    public LoginGUI(LoginListener loginListener, SignupListener signupListener) {
        this.loginListener = loginListener;
        this.signupListener = signupListener;
    }

    @Override
    public void initializeGUI() {
        // Create a JFrame instance as the main window
        setTitle("Stock Market Login");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(this.width, this.height);
        frame.setLayout(new BorderLayout());

        // Create a panel to hold the login form elements
        JPanel panel = new JPanel();
        frame.add(panel, BorderLayout.CENTER);
        placeComponents(panel);

        // Set the window in the center of the screen
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel userLabel = GuiUtils.createLabel("User:", 10, 10, 80, 25);
        JTextField userText = GuiUtils.createTextField(100, 10, 160, 25);
        JLabel passwordLabel = GuiUtils.createLabel("Password:", 10, 40, 80, 25);
        JPasswordField passwordText = GuiUtils.createPasswordField(100, 40, 160, 25);
        JCheckBox showPasswordCheckBox = GuiUtils.createJCheckBox("Show Password", 100, 70, 160, 25);
        JButton loginButton = GuiUtils.createButton("Login", 10, 100, 80, 25);
        JButton signupButton = GuiUtils.createButton("Signup", 180, 100, 80, 25);

        panel.add(userLabel);
        panel.add(userText);
        panel.add(passwordLabel);
        panel.add(passwordText);
        panel.add(loginButton);
        panel.add(signupButton);
        panel.add(showPasswordCheckBox);
        addActionListener(userText, passwordText, loginButton, signupButton, showPasswordCheckBox);
    }

    protected void addActionListener(JTextField userText, JPasswordField passwordText, JButton loginButton,
            JButton signupButton, JCheckBox showPasswordCheckBox) {
        // Add an event listener for the button
        loginButton.addActionListener(e -> {
            String username = userText.getText();
            String password = new String(passwordText.getPassword());
            if (signupListener != null) {
                loginListener.onLogin(username, password);
                if (loginListener.isLogin(username, password)) {
                    closeWindow();
                }
            } else {
                MainController mainController = new MainController();
                LoginController newLoginController = new LoginController(mainController);
                newLoginController.onLogin(username, password);
            }
        });

        signupButton.addActionListener(e -> {
            closeWindow(); // Close the login window

            if (signupListener != null) {
                signupListener.onSignupRequested();
            } else {
                MainController mainController = new MainController();
                SignupController newSignupController = new SignupController(mainController);
                newSignupController.onSignupRequested();
            }
        });

        showPasswordCheckBox.addActionListener(e -> {
            if (showPasswordCheckBox.isSelected()) {
                passwordText.setEchoChar((char) 0);
            } else {
                passwordText.setEchoChar('*');
            }
        });
    }
}
