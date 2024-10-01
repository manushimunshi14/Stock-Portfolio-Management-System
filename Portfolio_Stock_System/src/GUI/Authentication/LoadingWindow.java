package GUI.Authentication;
import javax.swing.*;
import java.awt.*;

public class LoadingWindow extends BaseGUI {
    private JLabel label;
    private Timer timer;
    private int dotCount = 0;

    public LoadingWindow() {
        initializeGUI();
    }

    @Override
    public void initializeGUI() {
        setFrame("Loading");
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new BorderLayout());

        label = new JLabel("Loading, please wait", SwingConstants.CENTER);
        frame.add(label, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        timer = new Timer(500, e -> updateLabelText());
        timer.start();
    }

    private void updateLabelText() {
        StringBuilder text = new StringBuilder("Loading, please wait");
        dotCount = (dotCount + 1) % 4; 

        for (int i = 0; i < dotCount; i++) {
            text.append(".");
        }

        label.setText(text.toString());
    }

    @Override
    public void closeWindow() {
        if (timer != null) {
            timer.stop();
        }
        frame.dispose();
    }
}
