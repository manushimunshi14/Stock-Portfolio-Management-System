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
import System.Account.SignupRequest;


public class SignupRequestHandler {
    private JButton signupButton;
    private JPanel responsePanel;
    private ManagerListener managerListener;

    public SignupRequestHandler(ManagerListener managerListener) {
        initializeButton();
        responsePanel = new JPanel();
        responsePanel.setLayout(new FlowLayout());
        this.managerListener = managerListener;
    }

    private void initializeButton() {
        signupButton = new JButton("Registration Requests");
        signupButton.setFont(GuiUtils.buttonFont);
        signupButton.addActionListener(e -> handleSignupAction());
    }

    public void handleSignupAction() {
        LoadingWindow loadingWindow = new LoadingWindow();

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                 List<SignupRequest> signupRequestList = managerListener.getSignupRequestList();
                String[] columnNames = { "Request ID", "Display Name", "Username", "Password", "Approve",
                        "Disapprove" };
                Object[][] data = new Object[signupRequestList.size()][6];

                for (int i = 0; i < signupRequestList.size(); i++) {
                    SignupRequest signupRequest = signupRequestList.get(i);
                    data[i][0] = signupRequest.getRequestNumber();
                    data[i][1] = signupRequest.getDisplayName();
                    data[i][2] = signupRequest.getUserName();
                    data[i][3] = signupRequest.getPassword();
                    data[i][4] = "Approve";
                    data[i][5] = "Disapprove";
                }
                DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return column == 4 || column == 5;
                    }
                };

                JTable registrationRequestTable = new JTable(model);
                JTableHeader header = registrationRequestTable.getTableHeader();

                header.setBackground(Color.GRAY);
                Font currentFont = header.getFont();
                Font boldFont = new Font(currentFont.getFontName(), Font.BOLD, currentFont.getSize());
                header.setFont(boldFont);

                // Set custom cell renderer and editor for the approve and disapprove button columns
                registrationRequestTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
                registrationRequestTable.getColumnModel().getColumn(4)
                        .setCellEditor(new ButtonEditor(new JCheckBox(), registrationRequestTable, managerListener));
                registrationRequestTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
                registrationRequestTable.getColumnModel().getColumn(5)
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


    public JButton getSignupButton() {
        return signupButton;
    }

    public JPanel getResponsePanel() {
        return responsePanel;
    }
}
