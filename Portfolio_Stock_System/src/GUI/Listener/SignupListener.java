package GUI.Listener;

import GUI.Controller.SignupController;

public interface SignupListener {
    void onSignup(String username, String password, String displayName, String rePassword);

    void onSignupRequested();

    void resetSignupController();

    SignupController setSignupController();
}
