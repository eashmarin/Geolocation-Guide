package ru.nsu.fit.gui;

import ru.nsu.fit.OptionsParser;
import ru.nsu.fit.entities.Location;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import java.awt.Dimension;
import java.awt.event.ItemListener;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class LocationsPanel extends JScrollPane {
    private final static int MAX_LOCATIONS_NUM = OptionsParser.get("locations_limit");

    private final JPanel viewPanel = new JPanel();
    private final List<LocationPanel> locationPanels = new LinkedList<>();

    private final ButtonGroup buttonGroup = new ButtonGroup();

    public LocationsPanel(int width, int height) {
        super(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        initComponents((int) (width * 0.95), 65);

        setViewportView(viewPanel);

        setPreferredSize(new Dimension(width, height));
    }

    private void initComponents(int locationPanelWidth, int locationPanelHeight) {
        viewPanel.setLayout(new BoxLayout(viewPanel, BoxLayout.Y_AXIS));

        for (int i = 0; i < MAX_LOCATIONS_NUM; i++) {
            LocationPanel locationPanel = new LocationPanel(locationPanelWidth, locationPanelHeight);
            locationPanels.add(locationPanel);

            viewPanel.add(locationPanel);

            buttonGroup.add(locationPanel.getRadioButton());
        }
    }

    public void setLocations(List<Location> locations) {
        Iterator<Location> locationIterator = locations.listIterator();
        Iterator<LocationPanel> recordIterator = locationPanels.listIterator();
        while (locationIterator.hasNext()) {
            LocationPanel panel = recordIterator.next();
            panel.setLocation(locationIterator.next());
            panel.setVisible(true);
        }

        while (recordIterator.hasNext()) {
            recordIterator.next().setVisible(false);
        }
    }

    public void addRadioButtonsListener(ItemListener listener) {
        for (LocationPanel panel: locationPanels) {
            panel.getRadioButton().addItemListener(listener);
        }
    }

    public List<LocationPanel> getLocationPanels() {
        return locationPanels;
    }

    public void removeButtonsSelection() {
        buttonGroup.clearSelection();
    }

    public void disableRadioButtons() {
        getLocationPanels().forEach(x -> x.getRadioButton().setEnabled(false));
    }
}
