package ru.nsu.fit;

import ru.nsu.fit.gui.MainFrame;
import ru.nsu.fit.gui.SearchPanel;
import ru.nsu.fit.gui.workers.LocationsRequestHandler;

import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        MainFrame mainFrame = new MainFrame();

        SearchPanel searchPanel = mainFrame.getSearchPanel();

        searchPanel.addButtonListener(e -> {
            LocationsRequestHandler locationsRequestHandler = new LocationsRequestHandler(mainFrame);
            locationsRequestHandler.execute();
        });
    }
}
