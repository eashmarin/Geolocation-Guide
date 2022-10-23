import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class JsonParser {
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static List<Location> parseLocations(String jsonText) {
        JsonNode rootNode = null;
        try {
            rootNode = mapper.readTree(jsonText).get("hits");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        List<Location> locations = new LinkedList<>();

        Iterator<JsonNode> iterator = rootNode.elements();

        while (iterator.hasNext()) {
            JsonNode node = iterator.next();

            Location location = new Location();

            JsonNode pointNode = node.get("point");
            location.setLng(Double.parseDouble(pointNode.get("lng").toString()));
            location.setLat(Double.parseDouble(pointNode.get("lat").toString()));

            location.setCountry(node.get("country").toString());
            location.setName(node.get("name").toString());

            locations.add(location);
        }

        return locations;
    }

    public static WeatherData parseWeather(String jsonText) {
        WeatherData weatherData = new WeatherData();

        try {
             JsonNode rootNode = mapper.readTree(jsonText);

             JsonNode weatherNode = rootNode.get("weather");
             JsonNode mainNode = rootNode.get("main");
             JsonNode windNode = rootNode.get("wind");

             String description = weatherNode.elements().next().get("description").toString();
             double temp = Double.parseDouble(mainNode.get("temp").toString());
             double feelsLike = Double.parseDouble(mainNode.get("feels_like").toString());
             double windSpeed = Double.parseDouble(windNode.get("speed").toString());

             weatherData.setDescription(description);
             weatherData.setTemp(temp);
             weatherData.setFeelsLike(feelsLike);
             weatherData.setWindSpeed(windSpeed);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return weatherData;
    }

    public static List<Place> parsePlaces(String jsonText) {
        List<Place> places = new LinkedList<>();

        JsonNode rootNode = null;
        try {
            rootNode = mapper.readTree(jsonText).get("features");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Iterator<JsonNode> iterator = rootNode.elements();

        while (iterator.hasNext()) {
            JsonNode featureNode = iterator.next();

            Place place = new Place();

            JsonNode coordsNode = featureNode.get("geometry").get("coordinates");
            Iterator<JsonNode> coordsIterator = coordsNode.elements();
            place.setLng(coordsIterator.next().asDouble());
            place.setLat(coordsIterator.next().asDouble());

            JsonNode propertiesNode = featureNode.get("properties");
            place.setXid(propertiesNode.get("xid").asText());
            place.setName(propertiesNode.get("name").toString());
            place.setDist(propertiesNode.get("dist").asDouble());

            places.add(place);
        }

        return places;
    }

    public static List<PlaceDescription> parseDescriptions(List<String> jsonDescriptions) {
        List<PlaceDescription> descriptions = new LinkedList<>();

        try {
            for (String jsonDesc: jsonDescriptions) {
                System.out.println(jsonDesc);
                JsonNode node = mapper.readTree(jsonDesc).get("address");

                PlaceDescription description = new PlaceDescription();

                description.setRoad(node.get("road").asText());
                description.setState(node.get("state").asText());
                description.setSuburb(node.get("suburb").asText());
                description.setState(node.get("state").asText());
                description.setPostcode(node.get("postcode").asInt());
                description.setHouseNumber(node.get("house_number").asInt());
                description.setCityDistrict(node.get("city_district").asText());

                descriptions.add(description);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return descriptions;
    }
}
