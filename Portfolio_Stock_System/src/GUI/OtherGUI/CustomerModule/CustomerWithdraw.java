package GUI.OtherGUI.CustomerModule;

import javax.swing.*;

import GUI.Listener.CustomerListener;
import GUI.Utils.GuiUtils;

public class CustomerWithdraw {
  private CustomerListener customerListener;
  private JButton withdrawButton;

  public CustomerWithdraw(CustomerListener customerListener) {
    this.customerListener = customerListener;
    initializeWithdrawButton();
  }

  private void initializeWithdrawButton() {
    withdrawButton = GuiUtils.createButton("Withdraw");
    withdrawButton.setFont(GuiUtils.buttonFont);
    withdrawButton.addActionListener(e -> customerListener.onWithdraw());
  }

  public JButton getWithdrawButton() {
    return this.withdrawButton;
  }
}
