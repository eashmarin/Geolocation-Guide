package ru.nsu.fit.gui;

import ru.nsu.fit.OptionsParser;
import ru.nsu.fit.entities.Attraction;

import javax.swing.*;

import java.awt.Color;
import java.awt.Dimension;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class AttractionsPanel extends JScrollPane {
    private final int MAX_ATTRACTIONS_NUM = OptionsParser.get("attractions_limit");

    private final JPanel viewPanel = new JPanel();
    private final List<AttractionTextPane> attractionsList = new LinkedList<>();

    public AttractionsPanel(int width, int height) {
        super(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        viewPanel.setLayout(new BoxLayout(viewPanel, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(width, height));
        setSize(new Dimension(width, height));

        for (int i = 0; i < MAX_ATTRACTIONS_NUM; i++) {
            AttractionTextPane panel = new AttractionTextPane(Integer.MAX_VALUE, 100);
            attractionsList.add(panel);
            viewPanel.add(panel);
            //viewPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        viewPanel.setBackground(Color.WHITE);
        viewPanel.setVisible(true);

        setViewportView(viewPanel);

        setBackground(Color.WHITE);
    }

    public void setAttractions(List<Attraction> attractions) {
        Iterator<Attraction> attractionIterator = attractions.listIterator();
        Iterator<AttractionTextPane> recordsIterator = attractionsList.listIterator();

        while (attractionIterator.hasNext() && recordsIterator.hasNext()) {
            AttractionTextPane panel = recordsIterator.next();
            panel.insertAttractionText(attractionIterator.next());
            panel.setVisible(true);
        }

        while (recordsIterator.hasNext()) {
            recordsIterator.next().setVisible(false);
        }
        repaint();
    }
}
