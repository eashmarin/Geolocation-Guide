package ru.nsu.fit.gui.workers;

import ru.nsu.fit.gui.LocationPanel;

import javax.swing.JButton;
import javax.swing.SwingWorker;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.List;

public class HandlerCompletionWaiter implements PropertyChangeListener {
    private final List<LocationPanel> panelList;
    private final JButton searchButton;

    public HandlerCompletionWaiter(List<LocationPanel> panelList, JButton searchButton) {
        this.panelList = panelList;
        this.searchButton = searchButton;
    }

    public void propertyChange(PropertyChangeEvent event) {
        if ("state".equals(event.getPropertyName())
                && SwingWorker.StateValue.DONE == event.getNewValue()) {

            searchButton.setEnabled(true);
            panelList.forEach(x -> x.getRadioButton().setEnabled(true));
        }
    }
}
