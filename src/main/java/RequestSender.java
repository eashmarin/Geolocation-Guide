import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final List<Place> places = new LinkedList<>();

    public RequestSender() {
        try {
            String jsonLocations = requestLocationsVariants("Washington").body();
            List<Location> locations = JsonParser.parseLocations(jsonLocations);

            String jsonWeather = requestWeatherLocation(locations.get(0)).body();
            WeatherData weatherData = JsonParser.parseWeather(jsonWeather);

            List<PlaceDescription> descriptions = requestPlaces(locations.get(0), 1000)
                    .thenApply(HttpResponse::body)
                    .thenApply(JsonParser::parsePlaces)
                    .thenApply(this::savePlaces)
                    .thenApply(this::requestDescriptions)
                    .thenApply(responses -> responses.stream().map(HttpResponse::body).toList())
                    .thenApply(JsonParser::parseDescriptions)
                    .get();

            setPlacesDescriptions(descriptions);

            System.out.println(places);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public HttpResponse<String> requestLocationsVariants(String locationName) throws ExecutionException, InterruptedException {
        String requestURIString = String.format("%s?q=%s&key=%s",
                URLs.GRAPHHOPPER,
                locationName,
                Credentials.getKey("graphhopperKey")
                );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestURIString))
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).get();
    }

    public HttpResponse<String> requestWeatherLocation(Location location) throws ExecutionException, InterruptedException {
        String requestURIString = String.format("%s?lat=%f&lon=%f&units=metric&appid=%s",
                URLs.OPENWEATHER,
                location.getLat(),
                location.getLat(),
                Credentials.getKey("openweatherKey")
                );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestURIString))
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).get();
    }

    public CompletableFuture<HttpResponse<String>> requestPlaces(Location location, int radius) {
        String requestURIString = String.format("%s?limit=10&radius=%d&lon=%f&lat=%f&apikey=%s",
                URLs.OPENTRIPMAP_PLACES,
                radius,
                location.getLng(),
                location.getLat(),
                Credentials.getKey("opentripmapKey")
                );

         HttpRequest request = HttpRequest.newBuilder()
                 .uri(URI.create(requestURIString))
                 .build();

         return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    private List<Place> savePlaces(List<Place> places) {
        this.places.addAll(places);

        return this.places;
    }

    private List<HttpResponse<String>> requestDescriptions(List<Place> places) {
        List<HttpResponse<String>> responseList = new LinkedList<>();
        for (Place place: places) {
            String requestURIString = String.format("%s%s?limit=10&apikey=%s",
                    URLs.OPENTRIPMAP_DESCRIPTION,
                    place.getXid(),
                    Credentials.getKey("opentripmapKey")
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


    private void setPlacesDescriptions(List<PlaceDescription> descriptions) {
        Iterator<Place> placeIterator = places.listIterator();
        Iterator<PlaceDescription> descriptionIterator = descriptions.listIterator();

        while (placeIterator.hasNext()) {
            placeIterator.next().setDescription(descriptionIterator.next());
        }
    }

}
