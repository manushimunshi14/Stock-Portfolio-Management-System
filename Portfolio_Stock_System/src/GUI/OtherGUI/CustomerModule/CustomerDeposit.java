package GUI.OtherGUI.CustomerModule;

import java.awt.*;
import javax.swing.*;

import GUI.Controller.CustomerController;
import GUI.Controller.MainController;
import GUI.Listener.CustomerListener;
import GUI.Utils.GuiUtils;

public class CustomerDeposit {
  private CustomerListener customerListener;
  private JButton depositButton;

  public CustomerDeposit(CustomerListener customerListener) {
    this.customerListener = customerListener;
    initializeWithdrawButton();
  }

  private void initializeWithdrawButton() {
    depositButton = GuiUtils.createButton("Deposit");
    depositButton.setFont(GuiUtils.buttonFont);
    depositButton.addActionListener(e -> customerListener.onDeposit());
  }

  public JButton getDepositButton() {
    return this.depositButton;
  }

  public static void main(String[] args) {
        // 创建 CustomerController 实例
        CustomerController customerController = new CustomerController(new MainController());

        // 使用 CustomerController 实例创建 ViewPurchasedStock 实例
        CustomerDeposit customerDeposit = new CustomerDeposit(customerController);

        // 创建 JFrame 来显示 ViewPurchasedStock
        JFrame frame = new JFrame("Deposit Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // 使用 getter 方法添加组件
        frame.add(customerDeposit.getDepositButton(), BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
    }

}
