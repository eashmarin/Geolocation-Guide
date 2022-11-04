package ru.nsu.fit.gui;

import ru.nsu.fit.entities.Attraction;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import java.awt.Color;
import java.awt.Dimension;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class AttractionsPanel extends JScrollPane {

    private JPanel viewPanel = new JPanel();
    private List<AttractionTextPane> placeRecords = new LinkedList<>();

    public AttractionsPanel(int width, int height, int recordsLimit) {
        super(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        viewPanel.setLayout(new BoxLayout(viewPanel, BoxLayout.Y_AXIS));

        for (int i = 0; i < recordsLimit; i++) {
            AttractionTextPane panel = new AttractionTextPane();
            placeRecords.add(panel);
            viewPanel.add(panel);
        }

        viewPanel.setBackground(Color.WHITE);
        viewPanel.setVisible(false);

        setViewportView(viewPanel);
        setPreferredSize(new Dimension(width, height));
    }

    public void setPlaces(List<Attraction> places) {
        Iterator<Attraction> placeIterator = places.listIterator();
        Iterator<AttractionTextPane> recordsIterator = placeRecords.listIterator();

        while (placeIterator.hasNext() && recordsIterator.hasNext()) {
            AttractionTextPane panel = recordsIterator.next();
            panel.setPlace(placeIterator.next());
            panel.setVisible(true);
        }

        while (recordsIterator.hasNext()) {
            recordsIterator.next().setVisible(false);
        }
    }
}
