package GUI.Controller;

import Database.DatabaseManager;
import GUI.Authentication.SignupGUI;
import GUI.Listener.SignupListener;
import GUI.Utils.PromptSetup;
import PasswordAuthentication.PasswordAuthentication;
import Services.AuthenticationService;

import java.sql.SQLException;

public class SignupController extends Controller implements SignupListener {
    private SignupGUI userSignup;

    public SignupController(MainController mainController) {
        super(mainController);
        userSignup = new SignupGUI(mainController.getLoginController(), mainController.getSignupController());
    }

    @Override
    public void resetSignupController() {
        userSignup = new SignupGUI(mainController.getLoginController(), mainController.getSignupController());
    }

    @Override
    public SignupController setSignupController() {
        return mainController.getSignupController();
    }

    @Override
    public void onSignupRequested() {
        if (userSignup == null) {
            userSignup = new SignupGUI(mainController.getLoginController(), mainController.getSignupController());
        }
        openPage();
    }

    @Override
    protected void openPage() {
        userSignup.setWindowsSize(450, 350);
        if (userSignup.getFrame() == null) {
            userSignup.setFrame();
        }
        userSignup.initializeGUI();
    }

    protected void closeWindow() {
        if (userSignup != null) {
            userSignup.closeWindow();
        }
    }

    @Override
    public void onSignup(String username, String password, String displayName, String rePassword) {
        if (password.equals(rePassword)) {
            if (AuthenticationService.signup(username, password, displayName)) {
                int requestId = signupUserResult(username, password, displayName);
                if (requestId != -1) {
                    String successMassage = "\nRegistration request sent.\nWaiting for administrator review!\nYour registration request code: " + requestId;
                    int[] windowsSize = new int[]{300, 200};
                    PromptSetup.setSuccessReport(successMassage, windowsSize, mainController);
                } else {
                    // Handle the case where signupResult is null
                    String errorMassage = "An error occurred during the signup process. Please try again.";
                    int[] windowsSize = new int[]{380, 130};
                    PromptSetup.setErrorReport(errorMassage, windowsSize, mainController);
                }
            } else {
                String signupErrorMassage = errorReport(username, password, displayName);
                int[] windowsSize = new int[]{420, 270};
                PromptSetup.setErrorReport(signupErrorMassage, windowsSize, mainController);
            }
        } else {
            String errorMassage = "\nInconsistency between password and re-entry password!\nTry again!\n";
            int[] windowsSize = new int[]{380, 130};
            PromptSetup.setErrorReport(errorMassage, windowsSize, mainController);
        }
    }

    private String errorReport(String username, String password, String displayName) {
        String signupErrorMassage = "";
        if (!AuthenticationService.isUsernameUnique(username)) {
            signupErrorMassage += "\nUsername already used!\n";
        }
        if (!AuthenticationService.isUsernameValid(username)) {
            signupErrorMassage += "\nThe length of the username cannot exceed 15 characters, \nand is only a-z, A-Z, and 1-9.\n";
        }
        if (!AuthenticationService.isDisplayNameValid(displayName)) {
            signupErrorMassage += "\nThe length of the display name cannot exceed 25 characters, \nand is only a-z, A-Z, and _.\n";
        }
        if (!AuthenticationService.isPasswordValid(password)) {
            signupErrorMassage += "\nThe length of the password must be more than 6 characters,\nand have at least one uppercase letter,\none lowercase letter, \none number \nand one special symbol. \n";
        }
        return signupErrorMassage;
    }

    private int signupUserResult(String username, String password, String displayName){
        DatabaseManager dbManager = new DatabaseManager();
        int requestId = -1;
        try {

            // Encrypting passwords
            String hashedPassword = AuthenticationService.hashPassword(password);

            String query = "INSERT INTO request (user_name, display_name, password, derivative) VALUES (?, ?, ?, 0)";
            dbManager.executeUpdate(query, true, username, displayName, hashedPassword);
            requestId = dbManager.getLastInsertedId();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
            return requestId;
        } finally {
            dbManager.closeConnection();
        }
        return requestId;
    }

}
