package GUI.Authentication;

import GUI.Listener.HomepageListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WelcomeGUI extends JFrame {
    private HomepageListener homepageListener;
    private JLabel companyLabel;

    public WelcomeGUI(HomepageListener homepageListener) {
        this.homepageListener = homepageListener;
        initializeGUI();
    }

    private void initializeGUI() {
        // Basic frame setup
        setTitle("Trade Pro");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLayout(new BorderLayout());

        // Create company name label
        companyLabel = new JLabel("Welcome to Trade Pro", SwingConstants.CENTER);
        companyLabel.setFont(new Font("Serif", Font.BOLD, 24));
        companyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Icon for App
        ImageIcon logo = new ImageIcon("Portfolio_Stock_System/Icon/CompanyIcon.png");
        Image originalImage = logo.getImage();
        Image resizedImage = originalImage.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        JLabel logoLabel = new JLabel(resizedIcon);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Make button clickable
        logoLabel = new JLabel(resizedIcon);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                homepageListener.onLoginButtonPressed();
            }
        });

        // Layout setup
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createVerticalGlue());
        panel.add(companyLabel);
        panel.add(logoLabel);
        panel.add(Box.createVerticalGlue());

        add(panel, BorderLayout.CENTER);

        // Set the window in the center of the screen and display it
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
