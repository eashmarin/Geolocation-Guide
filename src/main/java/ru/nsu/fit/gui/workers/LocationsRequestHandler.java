package ru.nsu.fit.gui.workers;

import ru.nsu.fit.JsonParser;
import ru.nsu.fit.RequestSender;
import ru.nsu.fit.entities.Location;
import ru.nsu.fit.gui.ContentPanel;
import ru.nsu.fit.gui.MainFrame;

import javax.swing.SwingWorker;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LocationsRequestHandler extends SwingWorker<List<Location>, Void> {
    private final MainFrame mainFrame;

    public LocationsRequestHandler(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    protected List<Location> doInBackground() {
        try {
            String input = mainFrame.getSearchPanel().getInputText();
            String jsonLocations = RequestSender.requestLocationsVariants(input)
                    .thenApply(HttpResponse::body)
                    .get();
            return JsonParser.parseLocations(jsonLocations);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void done() {
        try {
            ContentPanel contentPanel = mainFrame.getContentPanel();
            contentPanel.getLocationsPanel().setLocations(get());
            contentPanel.getLocationsPanel().removeButtonsSelection();
            contentPanel.showInfoPanels();
        } catch (InterruptedException | ExecutionException ex) {
            throw new RuntimeException(ex);
        }
    }
}
