package GUI.Authentication;
import java.awt.*;
import javax.swing.*;

import GUI.Listener.PromptListener;
import GUI.Listener.TransactionListener;
import GUI.Utils.GuiUtils;

public class TransactionGUI extends PromptGUI {
  private TransactionListener transactionListener;
  private JTextField totalAmountText;

  public TransactionGUI(PromptListener transactionListener) {
    super(transactionListener);
    this.transactionListener = (TransactionListener) transactionListener;
  }

  @Override
  public void initializeGUI() {
    setMassage();
    setTitle(transactionListener.getTitle());
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
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
    panel.setLayout(new GridBagLayout());
    GridBagConstraints constraints = new GridBagConstraints();

    // Error Label
    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.gridwidth = GridBagConstraints.REMAINDER;
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.anchor = GridBagConstraints.CENTER;
    String htmlErrorMessage = "<html><div style='text-align: center;'>" + massage.replace("\n", "<br>")
        + "</div></html>";
    JLabel errorLabel = GuiUtils.createLabel(htmlErrorMessage);
    panel.add(errorLabel, constraints);

    constraints.insets = new Insets(10, 0, 10, 0);

    // Total Amount Label
    constraints.gridx = 0;
    constraints.gridy = 1;
    constraints.gridwidth = 1;
    JLabel totalAmountLabel = GuiUtils.createLabel("Total amount:");
    panel.add(totalAmountLabel, constraints);

    // Total Amount Text
    constraints.gridx = 1;
    totalAmountText = GuiUtils.createTextField();
    panel.add(totalAmountText, constraints);

    // Conform Button
    constraints.gridx = 0;
    constraints.gridy = 2;
    constraints.gridwidth = 1;
    JButton backButton = GuiUtils.createButton("Back", 0, 0, 40, 25);
    panel.add(backButton, constraints);
    backButton.addActionListener(e -> closeWindow());

    // Back Button
    constraints.gridx = 1;
    constraints.gridwidth = 1;
    JButton conformButton = GuiUtils.createButton("Conform", 0, 0, 40, 25); // Same size as conformButton
    panel.add(conformButton, constraints);
    conformButton.addActionListener(e -> {
      String amountString = totalAmountText.getText();
      transactionListener.getConform(amountString);
    });
  }
}
