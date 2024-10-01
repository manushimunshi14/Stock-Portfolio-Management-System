package GUI.Authentication;

import javax.swing.*;
import java.awt.*;

import GUI.Controller.MainController;
import GUI.Controller.SignupController;
import GUI.Listener.LoginListener;
import GUI.Listener.SignupListener;
import GUI.Utils.GuiUtils;

public class SignupGUI extends LoginGUI {

    public SignupGUI(LoginListener loginListener, SignupListener signupListener) {
        super(loginListener, signupListener);
    }

    @Override
    public void initializeGUI() {
        setTitle("Stock Market Signup");
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

    @Override
    public void placeComponents(JPanel panel) {
        panel.setLayout(null);

        int labelWidth = 140;
        int textFieldWidth = 160;
        int componentHeight = 25;
        int verticalSpacing = 30;
        int labelX = (this.width - labelWidth - textFieldWidth) / 2;
        int textFieldX = labelX + labelWidth;

        int totalComponents = 6;
        int totalHeightNeeded = totalComponents * componentHeight + (totalComponents - 1) * verticalSpacing;
        int startY = (this.height - totalHeightNeeded) / 2;

        JLabel userLabel = GuiUtils.createLabel("User Name:", labelX, startY, labelWidth, componentHeight);
        JTextField userText = GuiUtils.createTextField(textFieldX, startY, textFieldWidth, componentHeight);

        startY += componentHeight + verticalSpacing;

        JLabel displayNameLabel = GuiUtils.createLabel("Display Name:", labelX, startY, labelWidth, componentHeight);
        JTextField displayNameText = GuiUtils.createTextField(textFieldX, startY, textFieldWidth, componentHeight);

        startY += componentHeight + verticalSpacing;

        JLabel passwordLabel = GuiUtils.createLabel("Password:", labelX, startY, labelWidth, componentHeight);
        JPasswordField passwordText = GuiUtils.createPasswordField(textFieldX, startY, textFieldWidth, componentHeight);

        startY += componentHeight + verticalSpacing;

        JLabel reEnterPasswordLabel = GuiUtils.createLabel("Re-Enter Password:", labelX, startY, labelWidth,
                componentHeight);
        JPasswordField reEnterpasswordText = GuiUtils.createPasswordField(textFieldX, startY, textFieldWidth,
                componentHeight);
        startY += componentHeight + verticalSpacing - 5;

        JCheckBox showPasswordCheckBox = GuiUtils.createJCheckBox("Show Password", textFieldX, startY, textFieldWidth,
                componentHeight);

        showPasswordCheckBox.addActionListener(e -> {
            char echoChar = showPasswordCheckBox.isSelected() ? (char) 0 : '*';
            passwordText.setEchoChar(echoChar);
            reEnterpasswordText.setEchoChar(echoChar);
        });

        startY += componentHeight + verticalSpacing - 5;

        JButton signupButton = GuiUtils.createButton("Signup", labelX, startY, 80, componentHeight);
        JButton backButton = GuiUtils.createButton("Back", textFieldX + (textFieldWidth - 80), startY, 80,
                componentHeight);

        panel.add(userLabel);
        panel.add(userText);
        panel.add(passwordLabel);
        panel.add(passwordText);
        panel.add(displayNameLabel);
        panel.add(displayNameText);
        panel.add(reEnterPasswordLabel);
        panel.add(reEnterpasswordText);
        panel.add(showPasswordCheckBox);
        panel.add(signupButton);
        panel.add(backButton);

        addActionListener(userText, displayNameText, passwordText, reEnterpasswordText, backButton, signupButton);
    }

    private void addActionListener(JTextField userText, JTextField displayNameText, JPasswordField passwordText, JPasswordField reEnterpasswordText , JButton backButton,
            JButton signupButton) {
        // Add an event listener for the button
        backButton.addActionListener(e -> {
            closeWindow(); // Close the signup window
            loginListener.onLoginRequested();
        });

        signupButton.addActionListener(e -> {
            String username = userText.getText();
            String password = new String(passwordText.getPassword());
            String rePassword = new String(reEnterpasswordText.getPassword());
            String displayName = displayNameText.getText();
            if (signupListener != null) {
                signupListener.onSignup(username, password, displayName, rePassword);
            } else {
                MainController mainController = new MainController();
                SignupController newSignupController = new SignupController(mainController);
                newSignupController.onSignup(username, password, displayName, rePassword);
            }
        });
    }
}
