package ru.nsu.fit.gui;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;

public class SearchPanel extends JPanel {
    private final JTextField inputField = new JTextField();
    private final JButton searchButton = new JButton("Search");

    public SearchPanel(int width, int height) {
        setLayout(new FlowLayout(FlowLayout.LEFT));

        setPreferredSize(new Dimension(width, height));

        inputField.setPreferredSize(new Dimension(width / 2, (int) (height / 1.5)));
        inputField.setHorizontalAlignment(JTextField.CENTER);
        inputField.setFont(new Font(getFont().getFontName(), Font.PLAIN, 16));

        searchButton.setPreferredSize(new Dimension(80, (int) (height / 1.5)));

        add(inputField, BorderLayout.CENTER);
        add(searchButton);

        setBackground(new Color(220, 242, 176));
        setVisible(true);
    }

    public String getInputText() {
        return inputField.getText();
    }

    public void addButtonListener(ActionListener listener) {
        searchButton.addActionListener(listener);
    }

    public JButton getSearchButton() {
        return searchButton;
    }
}
