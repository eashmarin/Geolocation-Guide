package ru.nsu.fit;

import org.apache.logging.log4j.LogManager;
import ru.nsu.fit.entities.Location;
import ru.nsu.fit.entities.Attraction;
import ru.nsu.fit.entities.AttractionDescription;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

enum URLs {
    GRAPHHOPPER("https://graphhopper.com/api/1/geocode"),
    OPENWEATHER("https://api.openweathermap.org/data/2.5/weather"),
    OPENTRIPMAP_PLACES("https://api.opentripmap.com/0.1/en/places/radius"),
    OPENTRIPMAP_DESCRIPTION("https://api.opentripmap.com/0.1/en/places/xid/");

    private final String url;

    URLs(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return url;
    }
}

public class RequestSender {
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final List<Attraction> places = new LinkedList<>();

    public static List<Attraction> requestPlacesAndDescriptions(Location location) throws ExecutionException, InterruptedException {
        List<AttractionDescription> descriptions = requestPlaces(location, 1000)
                .thenApplyAsync(HttpResponse::body)
                .thenApplyAsync(JsonParser::parsePlaces)
                .thenApplyAsync(RequestSender::savePlaces)
                .thenApplyAsync(RequestSender::requestDescriptions)
                .thenApplyAsync(responses -> responses.stream().map(HttpResponse::body).toList())
                .thenApplyAsync(JsonParser::parseDescriptions)
                .get();

        setPlacesDescriptions(descriptions);

        return places;
    }

    /*public static WeatherData requestAndParseWeather(Location location) throws ExecutionException, InterruptedException, IOException {
        return ru.nsu.fit.JsonParser.parseWeather(requestWeatherLocation(location).body());
    }*/

    public static CompletableFuture<HttpResponse<String>> requestLocationsVariants(String locationName) throws ExecutionException, InterruptedException {
        locationName = locationName.replaceAll(" ", "+");

        String requestURIString = String.format("%s?q=%s&limit=10&key=%s",
                URLs.GRAPHHOPPER,
                locationName,
                Credentials.getKey("graphhopperKey")
        );

        LogManager.getLogger().debug(requestURIString);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestURIString))
                .timeout(Duration.ofSeconds(10))
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    public static CompletableFuture<HttpResponse<String>> requestWeatherLocation(Location location) throws InterruptedException, IOException {
        String requestURIString = String.format("%s?lat=%f&lon=%f&units=metric&appid=%s",
                URLs.OPENWEATHER,
                location.getLat(),
                location.getLat(),
                Credentials.getKey("openweatherKey")
        );

        System.out.println(requestURIString);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestURIString))
                .timeout(Duration.ofSeconds(10))
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    public static CompletableFuture<HttpResponse<String>> requestPlaces(Location location, int radius) {
        String requestURIString = String.format("%s?limit=12&radius=%d&lon=%f&lat=%f&apikey=%s",
                URLs.OPENTRIPMAP_PLACES,
                radius,
                location.getLng(),
                location.getLat(),
                Credentials.getKey("opentripmapKey")
        );

        LogManager.getLogger().debug(requestURIString);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestURIString))
                .timeout(Duration.ofSeconds(10))
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    private static List<Attraction> savePlaces(List<Attraction> places) {
        RequestSender.places.clear();

        RequestSender.places.addAll(places);

        return RequestSender.places;
    }

    private static List<HttpResponse<String>> requestDescriptions(List<Attraction> places) {
        List<HttpResponse<String>> responseList = new LinkedList<>();
        for (Attraction place : places) {
            String requestURIString = String.format("%s%s?limit=12&apikey=%s",
                    URLs.OPENTRIPMAP_DESCRIPTION,
                    place.getXid(),
                    Credentials.getKey("opentripmapKey")
            );

            System.out.println(requestURIString);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(requestURIString))
                    .build();

            try {
                responseList.add(httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).get());
                Thread.sleep(100);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return responseList;
    }

    private static void setPlacesDescriptions(List<AttractionDescription> descriptions) {
        Iterator<Attraction> placeIterator = places.listIterator();
        Iterator<AttractionDescription> descriptionIterator = descriptions.listIterator();

        while (placeIterator.hasNext() && descriptionIterator.hasNext()) {
            placeIterator.next().setDescription(descriptionIterator.next());
        }
    }
}
