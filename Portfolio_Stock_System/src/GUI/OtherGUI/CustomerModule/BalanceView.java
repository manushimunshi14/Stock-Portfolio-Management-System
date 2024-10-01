package GUI.OtherGUI.CustomerModule;

import javax.swing.*;
import java.awt.Color;
import java.awt.Font;

import GUI.Listener.CustomerListener;
import GUI.Utils.GuiUtils;

public class BalanceView {

  private CustomerListener customerListener;
  private JPanel userDisplayNamePanel;
  private JPanel balancePanel;
  private JPanel showBalanceCheckBoxPanel;
  private JLabel balanceLabel;
  private JCheckBox showBalanceCheckBox;

  public BalanceView(CustomerListener customerListener) {
    this.customerListener = customerListener;
    initializeBaseJPanel();
  }

  public void initializeBaseJPanel() {
    userDisplayNamePanel = new JPanel();
    String displayName = customerListener.getUserDisplayName();

    JLabel greetingLabel = GuiUtils.createLabel("Hi!");

    JLabel displayNameLabel = GuiUtils.createLabel(displayName);

    if (customerListener.isDerivative()) {
      displayNameLabel.setForeground(Color.ORANGE);
      displayNameLabel.setFont(displayNameLabel.getFont().deriveFont(Font.BOLD));
    }

    userDisplayNamePanel.add(greetingLabel);
    userDisplayNamePanel.add(displayNameLabel);

    balancePanel = new JPanel();
    balanceLabel = GuiUtils.createLabel("Your Balance: $*****");
    balancePanel.add(balanceLabel);

    showBalanceCheckBoxPanel = new JPanel();
    showBalanceCheckBox = new JCheckBox("Show Balance");
    showBalanceCheckBox.setSelected(false);
    showBalanceCheckBox.setToolTipText("Check to show balance");
    showBalanceCheckBoxPanel.add(showBalanceCheckBox);

    showBalanceCheckBox.addActionListener(e -> {
        double balance = customerListener.getUserBalance();
        if (showBalanceCheckBox.isSelected()) {
          balanceLabel.setText("Your Balance: $" + balance);
        } else {
          balanceLabel.setText("Your Balance: $*****");
        }
    });
  }
  private void resetBalanceDisplay() {
    balanceLabel.setText("Your Balance: $*****");
    showBalanceCheckBox.setSelected(false);
  }
  public JPanel getUserDisplayNamePanel() {
    return userDisplayNamePanel;
  }

  public JPanel getBalancePanel() {
    resetBalanceDisplay();
    return balancePanel;
  }

  public JPanel getShowBalanceCheckBox() {
    return showBalanceCheckBoxPanel;
  }
}

