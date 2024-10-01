package GUI.Authentication;

import GUI.Listener.DerivativeUserPromptListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DerivativeUserPrompt extends BaseGUI {
    private DerivativeUserPromptListener derivativeUserPromptListener;

    public DerivativeUserPrompt(DerivativeUserPromptListener derivativeUserPromptListener){
        this.derivativeUserPromptListener = derivativeUserPromptListener;
    }

    @Override
    public void initializeGUI() {
        // Set basic properties of the frame
        frame.setTitle("Derivative User Confirmation");
        frame.setSize(this.width, this.height);
        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // Add a label
        JLabel label = new JLabel("You have met the requirements to become a Derivative User.Would you like to proceed?");
        frame.add(label);

        // Add "Yes" button and its action listener
        JButton yesButton = new JButton("Yes");
        yesButton.addActionListener(e-> derivativeUserPromptListener.confirm());
        frame.add(yesButton);

        // Add "No" button and its action listener
        JButton noButton = new JButton("No");
        noButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeWindow();
            }
        });
        frame.add(noButton);

        // Set the size of the frame and center it on the screen
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
