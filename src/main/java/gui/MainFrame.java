package gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    SearchPanel searchPanel = new SearchPanel();

    public MainFrame() {

        setPreferredSize(new Dimension(600, 800));

        Container container = getContentPane();

        container.add(searchPanel, BorderLayout.NORTH);

        pack();
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
