package GUI.OtherGUI.Managermodule;

import GUI.Authentication.BaseGUI;
import GUI.Listener.AddStockListener;
import GUI.Utils.GuiUtils;

import javax.swing.*;
import java.awt.*;

public class AddStockGUI extends BaseGUI {
  private AddStockListener addStockListener;
  private JTextField symbolTextField;
  private JTextField nameTextField;
  private JTextField priceTextField;


  public AddStockGUI(AddStockListener addStockListener)
  {
    this.addStockListener = addStockListener;
  }

  @Override
  public void initializeGUI() {
    frame.setLayout(new GridBagLayout());
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    GridBagConstraints gbc = new GridBagConstraints();

    frame.setTitle("Add New Stock");

    // 设置组件间的间距
    gbc.insets = new Insets(10, 10, 10, 10);

    // Label for stock symbol
    JLabel symbolLabel = GuiUtils.createLabel("Stock Symbol:");
    gbc.gridx = 0;
    gbc.gridy = 0;
    frame.add(symbolLabel, gbc);

    // TextField for stock symbol
    symbolTextField = GuiUtils.createTextField();
    gbc.gridx = 1;
    gbc.gridy = 0;
    frame.add(symbolTextField, gbc);

    // Label for stock name
    JLabel nameLabel = GuiUtils.createLabel("Stock Name:");
    gbc.gridx = 0;
    gbc.gridy = 1;
    frame.add(nameLabel, gbc);

    // TextField for stock name
    nameTextField = GuiUtils.createTextField();
    gbc.gridx = 1;
    gbc.gridy = 1;
    frame.add(nameTextField, gbc);

  // Label for current price
  JLabel priceLabel = GuiUtils.createLabel("Current Price:");
  gbc.gridx = 0;
  gbc.gridy = 2;
  frame.add(priceLabel, gbc);

  // TextField for current price
  priceTextField = GuiUtils.createTextField();
  gbc.gridx = 1;
  gbc.gridy = 2;
  frame.add(priceTextField, gbc);

    // Confirm button
    JButton confirmButton = GuiUtils.createButton("Confirm", 0, 0, 100, 30);
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 1;
    frame.add(confirmButton, gbc);
    confirmButton.addActionListener(e -> onConfirm());

    // Cancel button
    JButton cancelButton = GuiUtils.createButton("Cancel", 0, 0, 100, 30);
    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.gridwidth = 1;
    frame.add(cancelButton, gbc);
    cancelButton.addActionListener(e -> addStockListener.onCancel());


    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

  private void onConfirm() {
        String symbol = symbolTextField.getText();
        String name = nameTextField.getText();
        String price = priceTextField.getText();

        addStockListener.onConfirm(symbol, name , price);
    }
}

