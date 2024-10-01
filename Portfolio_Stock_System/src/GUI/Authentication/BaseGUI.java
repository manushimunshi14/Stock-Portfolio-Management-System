package GUI.Authentication;

import javax.swing.*;

public abstract class BaseGUI {
    protected JFrame frame;
    protected int width;
    protected int height;

    public void setWidth(int width) {
        this.width = width;
        frame = new JFrame();
    }

    public void setHeight(int height) {
        this.height = height;

    }

    public void setWindowsSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public abstract void initializeGUI();

    public void setTitle(String Title) {
        frame.setTitle(Title);
    }

    public void setFrame() {
        frame = new JFrame();
    }

    public void setFrame(String FrameName) {
        frame = new JFrame(FrameName);
    }

    public JFrame getFrame() {
        return this.frame;
    }

    public int getWidth() {
        return this.width;
    }
    public int getHeight() {
        return this.height;
    }

    public void closeWindow() {
        if (frame != null) {
            SwingUtilities.invokeLater(() -> frame.dispose());
        }
    }
}
