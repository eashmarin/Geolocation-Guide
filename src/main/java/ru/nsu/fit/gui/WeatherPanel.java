package ru.nsu.fit.gui;

import ru.nsu.fit.entities.WeatherData;

import javax.swing.BorderFactory;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

public class WeatherPanel extends JTextPane {
    private final SimpleAttributeSet keysAttributes = new SimpleAttributeSet();
    private final SimpleAttributeSet valuesAttributes = new SimpleAttributeSet();

    public WeatherPanel(int width, int height) {
        initAttributes();

        setEditable(false);

        setFont(new Font(getFont().getName(), Font.PLAIN, 18));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        setPreferredSize(new Dimension(width, height));
        setVisible(false);
    }

    private void initAttributes() {
        StyleConstants.setFontSize(keysAttributes, 14);
        StyleConstants.setItalic(keysAttributes, true);
        StyleConstants.setForeground(keysAttributes, Color.GRAY);

        StyleConstants.setFontSize(valuesAttributes, 16);
        StyleConstants.setForeground(valuesAttributes, Color.BLACK);
    }

    public void setWeatherData(WeatherData weatherData) {
        setText("");
        StyledDocument document = (StyledDocument) getDocument();
        try {
            document.insertString(document.getLength(), "Status: ", keysAttributes);
            document.insertString(document.getLength(), weatherData.getDescription() + "\n", valuesAttributes);
            document.insertString(document.getLength(), "Temperature: ", keysAttributes);
            document.insertString(document.getLength(), weatherData.getTemp() + " °C\n", valuesAttributes);
            document.insertString(document.getLength(), "Feels like: ", keysAttributes);
            document.insertString(document.getLength(), weatherData.getFeelsLike() + " °C\n", valuesAttributes);
            document.insertString(document.getLength(), "Wind speed: ", keysAttributes);
            document.insertString(document.getLength(), weatherData.getWindSpeed() + " m/s", valuesAttributes);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }
}
