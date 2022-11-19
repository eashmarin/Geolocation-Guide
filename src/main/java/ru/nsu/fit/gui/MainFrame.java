package ru.nsu.fit.gui;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

public class MainFrame extends JFrame {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;

    private static final int SEARCH_PANEL_HEIGHT = 60;
    private static final int CONTENT_PANEL_HEIGHT = 520;

    private final SearchPanel searchPanel = new SearchPanel(WIDTH, SEARCH_PANEL_HEIGHT);
    private final ContentPanel contentPanel = new ContentPanel(WIDTH, CONTENT_PANEL_HEIGHT);

    public MainFrame() {
        Container container = getContentPane();

        contentPanel.getLocationsPanel().addRadioButtonsListener(new RadioButtonListener(searchPanel.getSearchButton(), contentPanel));

        container.add(searchPanel, BorderLayout.NORTH);
        container.add(contentPanel, BorderLayout.CENTER);

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLocationRelativeTo(null);
        pack();

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setResizable(false);
        setTitle("Geolocation-Guide");
        setVisible(true);
    }

    public ContentPanel getContentPanel() {
        return contentPanel;
    }

    public SearchPanel getSearchPanel() {
        return searchPanel;
    }
}
