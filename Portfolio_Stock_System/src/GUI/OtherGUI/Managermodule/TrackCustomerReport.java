package GUI.OtherGUI.Managermodule;

import DatabaseReader.Stockdb;
import GUI.Authentication.LoadingWindow;
import GUI.Listener.ManagerListener;
import GUI.Utils.ButtonEditor;
import GUI.Utils.ButtonRenderer;
import GUI.Utils.GuiUtils;
import System.User.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class TrackCustomerReport {
    private JButton viewCustomerButton;
    private JPanel responsePanel;

    private ManagerListener managerListener;

    public TrackCustomerReport(ManagerListener managerListener){
        responsePanel = new JPanel();
        this.managerListener = managerListener;
        initializeButton();
    }

    private void initializeButton(){
        viewCustomerButton = new JButton("Track Customer Report");
        viewCustomerButton.setFont(GuiUtils.buttonFont);
        viewCustomerButton.addActionListener(e -> handleAssignAction());
    }

    private void  handleAssignAction(){
        LoadingWindow loadingWindow = new LoadingWindow();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                List<Customer> allCustomerList = managerListener.getCustomerList();
                String[] columnNames = { "User ID", "Username", "Display Name", "View" };
                Object[][] data = new Object[allCustomerList.size()][4];
                for (int i = 0; i < allCustomerList.size(); i++) {
                    Customer customer = allCustomerList.get(i);
                    data[i][0] = customer.getId();
                    data[i][1] = customer.getUsername();
                    data[i][2] = customer.getName();
                    data[i][3] = "View";
                }
                DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return column == 3;
                    }
                };

                JTable registrationRequestTable = new JTable(model);
                JTableHeader header = registrationRequestTable.getTableHeader();

                header.setBackground(Color.GRAY);
                Font currentFont = header.getFont();
                Font boldFont = new Font(currentFont.getFontName(), Font.BOLD, currentFont.getSize());
                header.setFont(boldFont);

                // Set custom cell renderer and editor for the action button column
                registrationRequestTable.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
                registrationRequestTable.getColumnModel().getColumn(3)
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

    public JButton getViewCustomerButton(){
        return viewCustomerButton;
    }

    public JPanel getResponsePanel(){
        initializeButton();
        return responsePanel;
    }

}
