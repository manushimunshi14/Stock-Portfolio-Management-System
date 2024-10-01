package GUI.Listener;

import GUI.Authentication.LoginGUI;

public interface LoginListener {

    void onLogin(String username, String password);

    boolean isLogin(String username, String password);

    void onLoginRequested();

    LoginGUI getLoginGUI();
}
