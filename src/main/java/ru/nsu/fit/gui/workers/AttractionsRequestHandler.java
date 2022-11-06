package ru.nsu.fit.gui.workers;

import ru.nsu.fit.RequestSender;
import ru.nsu.fit.entities.Location;
import ru.nsu.fit.entities.Attraction;
import ru.nsu.fit.gui.AttractionsPanel;

import javax.swing.SwingWorker;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AttractionsRequestHandler extends SwingWorker<List<Attraction>, Void> {
    private final Location location;
    private final AttractionsPanel infoPanel;

    public AttractionsRequestHandler(Location location, AttractionsPanel attractionsPanel) {
        this.location = location;
        this.infoPanel = attractionsPanel;
    }

    @Override
    protected List<Attraction> doInBackground() {
        try {
            return RequestSender.requestAttractionsAndDescriptions(location);
        } catch (ExecutionException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected void done() {
        try {
            infoPanel.setAttractions(get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
