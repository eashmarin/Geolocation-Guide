package ru.nsu.fit.gui;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

public class ContentPanel extends JPanel {
    private final LocationsPanel locationsPanel;

    private final JLabel weatherLabel = new JLabel("Weather");
    private final JLabel attractionsLabel = new JLabel("Attractions");

    private final WeatherPanel weatherPanel;
    private final AttractionsPanel attractionsPanel;

    private final Box sidePanelsBox = new Box(BoxLayout.Y_AXIS);

    public ContentPanel(int width, int height, int limit) {
        locationsPanel = new LocationsPanel((int) (width * 0.65), 500, limit);
        weatherPanel = new WeatherPanel(width - (int) (width * 0.65), 90);
        attractionsPanel = new AttractionsPanel(width - (int) (width * 0.65), 405, limit);

        initLabels();
        addSidePanelsToBox();

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        add(locationsPanel, BorderLayout.CENTER);
        add(sidePanelsBox);

        setBackground(Color.WHITE);
        setVisible(true);
    }

    private void initLabels() {
        weatherLabel.setHorizontalTextPosition(JLabel.CENTER);
        attractionsLabel.setHorizontalTextPosition(JLabel.CENTER);

        weatherLabel.setFont(new Font(getFont().getFontName(), Font.ITALIC, 19));
        attractionsLabel.setFont(new Font(getFont().getFontName(), Font.ITALIC, 19));

        weatherLabel.setAlignmentX(CENTER_ALIGNMENT);
        attractionsLabel.setAlignmentX(CENTER_ALIGNMENT);

        weatherLabel.setVisible(false);
        attractionsLabel.setVisible(false);
    }

    private void addSidePanelsToBox() {
        sidePanelsBox.add(weatherLabel);
        sidePanelsBox.add(weatherPanel);
        sidePanelsBox.add(attractionsLabel);
        sidePanelsBox.add(attractionsPanel);
    }

    public WeatherPanel getWeatherPanel() {
        return weatherPanel;
    }

    public AttractionsPanel getAttractionsPanel() {
        return attractionsPanel;
    }

    public LocationsPanel getLocationsPanel() {
        return locationsPanel;
    }

    public void showInfoPanels() {
        weatherLabel.setVisible(true);
        attractionsLabel.setVisible(true);
        weatherPanel.setVisible(true);
        attractionsPanel.setVisible(true);
    }
}
