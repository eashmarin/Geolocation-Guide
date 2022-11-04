package ru.nsu.fit.gui.workers;

import ru.nsu.fit.JsonParser;
import ru.nsu.fit.RequestSender;
import ru.nsu.fit.entities.Location;
import ru.nsu.fit.entities.WeatherData;
import ru.nsu.fit.gui.WeatherPanel;

import javax.swing.*;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

public class WeatherRequestHandler extends SwingWorker<WeatherData, Void> {
    private final Location location;
    private final WeatherPanel infoPanel;

    public WeatherRequestHandler(Location location, WeatherPanel infoPanel) {
        this.location = location;
        this.infoPanel = infoPanel;
    }

    @Override
    protected WeatherData doInBackground() {
        try {
            String jsonWeather = RequestSender.requestWeatherLocation(location).thenApply(HttpResponse::body).get();
            return JsonParser.parseWeather(jsonWeather);
        } catch (ExecutionException | IOException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    };

    @Override
    protected void done() {
        try {
            infoPanel.setWeatherData(get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
