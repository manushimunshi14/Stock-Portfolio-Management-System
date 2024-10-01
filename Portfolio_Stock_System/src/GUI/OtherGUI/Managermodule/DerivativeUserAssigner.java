package GUI.OtherGUI.Managermodule;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.List;
import java.util.ArrayList;

import GUI.Authentication.LoadingWindow;
import GUI.Listener.ManagerListener;
import GUI.Utils.ButtonEditor;
import GUI.Utils.ButtonRenderer;
import GUI.Utils.GuiUtils;
import System.User.Customer;


public class DerivativeUserAssigner {
  private JButton assignButton;
  private JPanel responsePanel;
  private ManagerListener managerListener;

  public DerivativeUserAssigner(ManagerListener managerListener) {
    initializeButton();
    responsePanel = new JPanel();
    responsePanel.setLayout(new BorderLayout());
    this.managerListener = managerListener;
  }

  private void initializeButton() {
    assignButton = new JButton("Assign Derivative User");
    assignButton.setFont(GuiUtils.buttonFont);
    assignButton.addActionListener(e -> handleAssignAction());
  }

  public void handleAssignAction() {
    LoadingWindow loadingWindow = new LoadingWindow();

    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() throws Exception {
         List<Customer> derivativeUserList = managerListener.getDerivativeUserList();

        String[] columnNames = { "User ID", "Username", "Display Name", "Balance", "Action" };
        Object[][] data = new Object[derivativeUserList.size()][5];
        for (int i = 0; i < derivativeUserList.size(); i++) {
          Customer customer = derivativeUserList.get(i);
          data[i][0] = customer.getId();
          data[i][1] = customer.getUsername();
          data[i][2] = customer.getName();
          data[i][3] = customer.getBalance();
          data[i][4] = "Promote";
        }
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
          @Override
          public boolean isCellEditable(int row, int column) {
            return column == 4;
          }
        };

        JTable registrationRequestTable = new JTable(model);
        JTableHeader header = registrationRequestTable.getTableHeader();

        header.setBackground(Color.GRAY);
        Font currentFont = header.getFont();
        Font boldFont = new Font(currentFont.getFontName(), Font.BOLD, currentFont.getSize());
        header.setFont(boldFont);

        // Set custom cell renderer and editor for the action button column
        registrationRequestTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        registrationRequestTable.getColumnModel().getColumn(4)
            .setCellEditor(new ButtonEditor(new JCheckBox(), registrationRequestTable, managerListener));

        JScrollPane scrollPane = new JScrollPane(registrationRequestTable);

        responsePanel.removeAll();
        responsePanel.setLayout(new BorderLayout());
        responsePanel.add(scrollPane, BorderLayout.CENTER);

        return null;
      }

      @Override
      protected void done() {
        loadingWindow.closeWindow();

        responsePanel.revalidate();
        responsePanel.repaint();
      }
    };

    worker.execute();
  }

  public JButton getAssignButton() {
    return assignButton;
  }

  public JPanel getResponsePanel() {
    return responsePanel;
  }
}
