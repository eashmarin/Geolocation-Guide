package gui;

import javax.swing.*;
import java.awt.*;

public class SearchPanel extends JPanel {
    private final JTextField inputArea = new JTextField();

    public SearchPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER));

        setPreferredSize(new Dimension(400, 100));

        inputArea.setPreferredSize(new Dimension(200, 40));
        inputArea.setHorizontalAlignment(JTextField.CENTER);

        add(Box.createVerticalStrut(70));
        add(inputArea, BorderLayout.CENTER);

        setBackground(Color.DARK_GRAY);


        setVisible(true);
    }
}
