package ru.nsu.fit.gui.workers;

import ru.nsu.fit.gui.LocationPanel;

import javax.swing.JButton;
import javax.swing.SwingWorker;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.List;

public class HandlerCompletionWaiter implements PropertyChangeListener {
    private final List<LocationPanel> locationPanels;
    private final JButton searchButton;

    public HandlerCompletionWaiter(List<LocationPanel> locationPanels, JButton searchButton) {
        this.locationPanels = locationPanels;
        this.searchButton = searchButton;
    }

    public void propertyChange(PropertyChangeEvent event) {
        if ("state".equals(event.getPropertyName()) && SwingWorker.StateValue.DONE == event.getNewValue()) {
            searchButton.setEnabled(true);
            locationPanels.forEach(x -> x.getRadioButton().setEnabled(true));
        }
    }
}
