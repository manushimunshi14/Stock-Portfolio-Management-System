package GUI.Authentication;

import GUI.Controller.PromptController;
import GUI.Listener.PromptListener;
import GUI.Utils.GUIFunction;
import GUI.Utils.GuiUtils;

import javax.swing.*;
import java.awt.*;

public class PromptGUI extends BaseGUI implements GUIFunction {
  protected String massage;
  protected PromptListener promptListener;

  public PromptGUI(PromptListener promptListener) {
    this.promptListener = promptListener;
  }

  public void setMassage() {
    this.massage = promptListener.getMassage();
  }

  @Override
  public void initializeGUI() {
    setMassage();
    // Create a JFrame instance as the main window
    setTitle(promptListener.getTitle());
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

    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.gridwidth = GridBagConstraints.REMAINDER;
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.anchor = GridBagConstraints.CENTER;

    String htmlErrorMassage = "<html><div style='text-align: center;'>" + massage.replace("\n", "<br>")
        + "</div></html>";
    JLabel errorLabel = GuiUtils.createLabel(htmlErrorMassage, 0, 0, 260, 100);
    panel.add(errorLabel, constraints);

    constraints.gridy = 1;
    constraints.weighty = 1.0;

    JButton backButton = GuiUtils.createButton("Back", 0, 0, 80, 25);
    panel.add(backButton, constraints);
    addActionListener(backButton);
  }

  public void addActionListener(JButton backButton) {
    if (promptListener.getOperationStatus().equals(PromptController.SUCCESS)) {
      backButton.addActionListener(e -> promptListener.closePromptAndTransaction());
    } else {
      backButton.addActionListener(e -> closeWindow());
    }
  }
}
