package GUI.Authentication;

import java.awt.*;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import GUI.Controller.LoginController;
import GUI.Controller.MainController;
import GUI.Controller.ManagerController;
import GUI.Listener.ManagerListener;
import GUI.OtherGUI.Managermodule.*;
import GUI.Utils.GuiUtils;
import System.Account.LoginResult;
import System.User.Admin;

public class ManagerGUI extends UserGUI {
  private ManagerListener managerListener;
  private DerivativeUserAssigner derivativeUserAssigner;
  private PriceModifier priceModifier;
  private SignupRequestHandler signupRequestHandler;
  private StockQuantityChanger stockQuantityChanger;
  private TrackCustomerReport trackCustomerReport;
  private JPanel centralPanel;
  private JPanel windowJPanel;

  public ManagerGUI(ManagerListener managerListener) {
    this.managerListener = managerListener;

    this.derivativeUserAssigner = new DerivativeUserAssigner(managerListener);

    this.priceModifier = new PriceModifier(managerListener);
    priceModifier.addStockDetailPanelListener(this::handleRespondPanel);

    this.signupRequestHandler = new SignupRequestHandler(managerListener);
    this.stockQuantityChanger = new StockQuantityChanger(managerListener);
    this.trackCustomerReport = new TrackCustomerReport(managerListener);

    this.centralPanel = new JPanel(new BorderLayout());
    this.windowJPanel = new JPanel();
    // Create a panel to hold the login form elements
  }

  @Override
  public void initializeGUI() {
    // Create a JFrame instance as the main window
    String managerName = managerListener.getUserDisplayName();
    setTitle("Welcome! Manager - " + managerName);
    frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    frame.setSize(this.width, this.height);
    frame.setLayout(new BorderLayout());

    frame.add(windowJPanel, BorderLayout.CENTER);
    placeComponents(windowJPanel);

    frame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        managerListener.onLogout();
      }
    });



    // Set the window in the center of the screen
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

  @Override
  public void placeComponents(JPanel panel) {
    panel.setLayout(new BorderLayout(15, 15)); // Horizontal and vertical gaps
    panel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

    // place these buttons at east
    JButton derivativeUserButton = derivativeUserAssigner.getAssignButton();
    JButton signupRequestButton = signupRequestHandler.getSignupButton();
    JButton stockQuantityChangerButton = stockQuantityChanger.getMainButton();
    JButton trackCustomerReportButton = trackCustomerReport.getViewCustomerButton();

    JButton logoutButton = GuiUtils.createButton("Log Out", 20, 1000, 35, 30);
    logoutButton.setFont(GuiUtils.buttonFont);
    logoutButton.addActionListener(e -> managerListener.onLogout());

    JButton reloadButton = GuiUtils.createButton("Flash Window", 20, 1000, 35, 30);
    reloadButton.setFont(GuiUtils.buttonFont);
    reloadButton.addActionListener(e -> reloadWindow());

    buttonPanel.add(derivativeUserButton);
    buttonPanel.add(signupRequestButton);
    buttonPanel.add(stockQuantityChangerButton);
    buttonPanel.add(trackCustomerReportButton);
    buttonPanel.add(logoutButton);
    buttonPanel.add(reloadButton);

    panel.add(buttonPanel, BorderLayout.EAST);

    // place the allStockButtonList  at west
    JPanel allStockButtonList = priceModifier.getStockListPanel();
    panel.add(allStockButtonList, BorderLayout.WEST);

    // place the responsePanel at the center
    derivativeUserButton.addActionListener(e -> handleRespondPanel(derivativeUserAssigner.getResponsePanel()));
    signupRequestButton.addActionListener(e -> handleRespondPanel(signupRequestHandler.getResponsePanel()));
    stockQuantityChangerButton.addActionListener(e -> handleRespondPanel(stockQuantityChanger.getResponsePanel()));
    trackCustomerReportButton.addActionListener(e -> handleRespondPanel(trackCustomerReport.getResponsePanel()));

    panel.add(centralPanel, BorderLayout.CENTER);
    panel.add(new JPanel(), BorderLayout.SOUTH);
  }

  public void reloadWindow() {
    priceModifier.initializeStockListPanel();
    windowJPanel.removeAll();
    centralPanel.removeAll();
    placeComponents(windowJPanel);
    windowJPanel.revalidate();
    windowJPanel.repaint();
    centralPanel.revalidate();
    centralPanel.repaint();
  }

  private void handleRespondPanel(JPanel newPanel) {
    centralPanel.removeAll();
    centralPanel.add(newPanel, BorderLayout.CENTER);
    centralPanel.revalidate();
    centralPanel.repaint();
  }

  public void assignDerivativeUser() {
    derivativeUserAssigner.handleAssignAction();
    JPanel responsePanel = derivativeUserAssigner.getResponsePanel();
    centralPanel.removeAll();
    centralPanel.add(responsePanel, BorderLayout.CENTER);
    centralPanel.revalidate();
    centralPanel.repaint();
  }

  public void handleSignup() {
    signupRequestHandler.handleSignupAction();
    JPanel responsePanel = signupRequestHandler.getResponsePanel();
    centralPanel.removeAll();
    centralPanel.add(responsePanel, BorderLayout.CENTER);
    centralPanel.revalidate();
    centralPanel.repaint();
  }

  public void changeStockQuantity() {
    JPanel responsePanel = stockQuantityChanger.getResponsePanel();
    centralPanel.removeAll();
    centralPanel.add(responsePanel, BorderLayout.CENTER);
    centralPanel.revalidate();
    centralPanel.repaint();
  }


public static void main(String[] args) {
      SwingUtilities.invokeLater(() -> {
        String username = "JohnJay1";
        MainController mainController = new MainController();
        ManagerController managerController = mainController.getManagerController();
        LoginController loginController = new LoginController(mainController);
        LoginResult loginResult = loginController.getLoginResult(username);
        Admin admin = new Admin(loginResult.getUserId(), loginResult.getDisplayName(), username,
            loginResult.getManagerId(),
            loginResult.getPasswordId());
        managerController.setUser(admin);
        managerController.openPage();
      });
    }
}
