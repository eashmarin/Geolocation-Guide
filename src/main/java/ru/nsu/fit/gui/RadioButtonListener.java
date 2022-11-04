package ru.nsu.fit.gui;

import ru.nsu.fit.gui.workers.HandlerCompletionWaiter;
import ru.nsu.fit.gui.workers.AttractionsRequestHandler;
import ru.nsu.fit.gui.workers.WeatherRequestHandler;

import javax.swing.JButton;
import javax.swing.JRadioButton;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class RadioButtonListener implements ItemListener {
    private final LocationsPanel locationsPanel;
    private final WeatherPanel weatherPanel;
    private final AttractionsPanel attractionsPanel;
    private final JButton searchButton;

    public RadioButtonListener(JButton searchButton, ContentPanel contentPanel) {
        this.searchButton = searchButton;
        this.locationsPanel = contentPanel.getLocationsPanel();
        this.weatherPanel = contentPanel.getWeatherPanel();
        this.attractionsPanel = contentPanel.getAttractionsPanel();
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            JRadioButton radioButton = (JRadioButton) e.getSource();
            for (LocationPanel resultPanel : locationsPanel.getLocationPanels()) {
                if (resultPanel.getRadioButton().equals(radioButton)) {
                    searchButton.setEnabled(false);
                    locationsPanel.getLocationPanels().forEach(x -> x.getRadioButton().setEnabled(false));

                    WeatherRequestHandler weatherRequestHandler = new WeatherRequestHandler(resultPanel.getlocation(), weatherPanel);
                    AttractionsRequestHandler placesRequestHandler = new AttractionsRequestHandler(resultPanel.getlocation(), attractionsPanel);

                    placesRequestHandler.addPropertyChangeListener(new HandlerCompletionWaiter(locationsPanel.getLocationPanels(), searchButton));

                    weatherRequestHandler.execute();
                    placesRequestHandler.execute();
                }
            }
        }
    }
}
