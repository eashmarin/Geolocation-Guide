package ru.nsu.fit;

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
    OPENTRIPMAP_ATTRACTIONS("https://api.opentripmap.com/0.1/en/places/radius"),
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
    private static final int LOCATIONS_LIMIT = OptionsParser.get("locations_limit");
    private static final int ATTRACTIONS_LIMIT = OptionsParser.get("attractions_limit");
    private static final int RADIUS = OptionsParser.get("radius");

    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final List<Attraction> attractionsList = new LinkedList<>();

    public static List<Attraction> requestAttractionsAndDescriptions(Location location) throws ExecutionException, InterruptedException {
        List<AttractionDescription> descriptions = requestAttractions(location)
                .thenApply(HttpResponse::body)
                .thenApply(JsonParser::parseAttractions)
                .thenApply(RequestSender::saveAttractions)
                .thenApply(RequestSender::requestDescriptions)
                .thenApply(responses -> responses.stream().map(HttpResponse::body).toList())
                .thenApply(JsonParser::parseDescriptions)
                .get();

        linkDescriptionsToAttractions(descriptions);

        return attractionsList;
    }

    public static CompletableFuture<HttpResponse<String>> requestLocationsVariants(String locationName) throws ExecutionException, InterruptedException {
        locationName = locationName.replaceAll(" ", "+");

        String requestURIString = String.format("%s?q=%s&limit=%d&key=%s",
                URLs.GRAPHHOPPER,
                locationName,
                LOCATIONS_LIMIT,
                CredentialsParser.getKey("graphhopperKey")
        );

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
                CredentialsParser.getKey("openweatherKey")
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestURIString))
                .timeout(Duration.ofSeconds(10))
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    private static CompletableFuture<HttpResponse<String>> requestAttractions(Location location) {
        String requestURIString = String.format("%s?limit=%d&radius=%d&lon=%f&lat=%f&apikey=%s",
                URLs.OPENTRIPMAP_ATTRACTIONS,
                ATTRACTIONS_LIMIT,
                RADIUS,
                location.getLng(),
                location.getLat(),
                CredentialsParser.getKey("opentripmapKey")
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestURIString))
                .timeout(Duration.ofSeconds(10))
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    private static List<Attraction> saveAttractions(List<Attraction> attractions) {
        RequestSender.attractionsList.clear();

        RequestSender.attractionsList.addAll(attractions);

        return RequestSender.attractionsList;
    }

    private static List<HttpResponse<String>> requestDescriptions(List<Attraction> attractions) {
        List<HttpResponse<String>> responseList = new LinkedList<>();
        for (Attraction attraction : attractions) {
            String requestURIString = String.format("%s%s?limit=%d&apikey=%s",
                    URLs.OPENTRIPMAP_DESCRIPTION,
                    attraction.getXid(),
                    ATTRACTIONS_LIMIT,
                    CredentialsParser.getKey("opentripmapKey")
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(requestURIString))
                    .build();

            try {
                responseList.add(httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return responseList;
    }

    private static void linkDescriptionsToAttractions(List<AttractionDescription> descriptions) {
        Iterator<Attraction> attractionIterator = attractionsList.listIterator();
        Iterator<AttractionDescription> descriptionIterator = descriptions.listIterator();

        while (attractionIterator.hasNext() && descriptionIterator.hasNext()) {
            attractionIterator.next().setDescription(descriptionIterator.next());
        }
    }
}
