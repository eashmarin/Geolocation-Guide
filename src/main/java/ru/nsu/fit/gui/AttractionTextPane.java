package ru.nsu.fit.gui;

import ru.nsu.fit.entities.Attraction;
import ru.nsu.fit.entities.AttractionDescription;

import javax.swing.BorderFactory;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import java.awt.*;

public class AttractionTextPane extends JTextPane {
    private final SimpleAttributeSet headersAttributes = new SimpleAttributeSet();
    private final SimpleAttributeSet keysAttributes = new SimpleAttributeSet();
    private final SimpleAttributeSet valuesAttributes = new SimpleAttributeSet();

    public AttractionTextPane(int width, int height) {
        initAttributes();
        setEditable(false);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setMaximumSize(new Dimension(width, height));
        setBackground(Color.WHITE);
        setVisible(false);
    }

    private void initAttributes() {
        StyleConstants.setFontSize(headersAttributes, 16);
        StyleConstants.setBold(headersAttributes, true);
        StyleConstants.setForeground(headersAttributes, Color.BLACK);

        StyleConstants.setFontSize(keysAttributes, 14);
        StyleConstants.setItalic(keysAttributes, true);
        StyleConstants.setForeground(keysAttributes, Color.GRAY);

        StyleConstants.setFontSize(valuesAttributes, 14);
        StyleConstants.setForeground(valuesAttributes, Color.BLACK);

    }

    public void insertAttractionText(Attraction attraction) {
        setText("");
        Document document = getDocument();

        try {
            document.insertString(document.getLength(), attraction.getName() + "\n", headersAttributes);
            document.insertString(document.getLength(), "distance: ", keysAttributes);
            document.insertString(document.getLength(), String.valueOf(attraction.getDist()), valuesAttributes);
            if (attraction.getDescription() != null) {
                AttractionDescription desc = attraction.getDescription();
                document.insertString(document.getLength(), "\n", keysAttributes);
                if (!desc.getRoad().equals("") && !desc.getHouseNumber().equals("")) {
                    document.insertString(document.getLength(), "address: ", keysAttributes);
                    document.insertString(document.getLength(), desc.getRoad() + " " + desc.getHouseNumber() + "\n", valuesAttributes);
                }
                if (!attraction.getDescription().getState().equals("")) {
                    document.insertString(document.getLength(), "state: ", keysAttributes);
                    document.insertString(document.getLength(), attraction.getDescription().getState() + "\n", valuesAttributes);
                }
                if (!attraction.getDescription().getSuburb().equals("")) {
                    document.insertString(document.getLength(), "suburb: ", keysAttributes);
                    document.insertString(document.getLength(), attraction.getDescription().getSuburb() + "\n", valuesAttributes);
                }
                document.insertString(document.getLength(), "postcode: ", keysAttributes);
                document.insertString(document.getLength(), String.valueOf(attraction.getDescription().getPostcode()), valuesAttributes);
            }
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }
}
