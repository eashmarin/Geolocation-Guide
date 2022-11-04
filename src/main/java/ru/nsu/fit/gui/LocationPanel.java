package ru.nsu.fit.gui;

import ru.nsu.fit.entities.Location;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

public class LocationPanel extends JPanel {
    private final JLabel nameLabel = new JLabel();
    private final JLabel coordsLabel = new JLabel();
    private final JLabel countryLabel = new JLabel();
    private final JLabel cityLabel = new JLabel();

    private Location location;

    private final JRadioButton radioButton = new JRadioButton();

    public LocationPanel(int width, int height) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        Box labelBox = new Box(BoxLayout.Y_AXIS);
        initLabels();
        initLabelBox(labelBox);

        radioButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        radioButton.setAlignmentY(Component.CENTER_ALIGNMENT);

        add(radioButton);
        add(Box.createHorizontalGlue());
        add(labelBox);
        add(Box.createHorizontalGlue());

        setPreferredSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));

        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setVisible(false);
    }

    private void initLabels() {
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        coordsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        countryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        String fontName = nameLabel.getFont().getName();

        nameLabel.setFont(new Font(fontName, Font.BOLD, 20));
        coordsLabel.setFont(new Font(fontName, Font.PLAIN, 12));
        coordsLabel.setForeground(Color.GRAY);
    }

    private void initLabelBox(Box labelBox) {
        labelBox.add(nameLabel);
        labelBox.add(Box.createVerticalGlue());
        labelBox.add(countryLabel);
        labelBox.add(Box.createVerticalGlue());
        labelBox.add(cityLabel);

        labelBox.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public void setLocation(Location location) {
        this.location = location;

        nameLabel.setText(location.getName());
        coordsLabel.setText(location.getLat() + "," + location.getLng());
        countryLabel.setText(location.getCountry());
        cityLabel.setText(location.getCity());
    }

    public JRadioButton getRadioButton() {
        return radioButton;
    }

    public Location getlocation() {
        return location;
    }
}