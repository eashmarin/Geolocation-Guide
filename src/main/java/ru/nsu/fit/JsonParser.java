package ru.nsu.fit;

import ru.nsu.fit.entities.Location;
import ru.nsu.fit.entities.Attraction;
import ru.nsu.fit.entities.AttractionDescription;
import ru.nsu.fit.entities.WeatherData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
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
            location.setLng(pointNode.get("lng").asDouble());
            location.setLat(pointNode.get("lat").asDouble());


            if (node.get("country") != null)
                location.setCountry(node.get("country").asText());

            if (node.get("city") != null) {
                location.setCity(node.get("city").asText());
            }

            location.setName(node.get("name").asText());

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

             String description = weatherNode.elements().next().get("description").asText();
             double temp = mainNode.get("temp").asDouble();
             double feelsLike = mainNode.get("feels_like").asDouble();
             double windSpeed = windNode.get("speed").asDouble();

             weatherData.setDescription(description);
             weatherData.setTemp(temp);
             weatherData.setFeelsLike(feelsLike);
             weatherData.setWindSpeed(windSpeed);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return weatherData;
    }

    public static List<Attraction> parseAttractions(String jsonText) {
        List<Attraction> attractions = new LinkedList<>();

        JsonNode rootNode = null;
        try {
            rootNode = mapper.readTree(jsonText).get("features");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        if (rootNode == null)
            return Collections.emptyList();

        Iterator<JsonNode> iterator = rootNode.elements();

        while (iterator.hasNext()) {
            JsonNode featureNode = iterator.next();

            Attraction attraction = new Attraction();

            if (featureNode.get("error") != null)
                continue;

            JsonNode coordsNode = featureNode.get("geometry").get("coordinates");
            Iterator<JsonNode> coordsIterator = coordsNode.elements();
            attraction.setLng(coordsIterator.next().asDouble());
            attraction.setLat(coordsIterator.next().asDouble());

            JsonNode propertiesNode = featureNode.get("properties");
            attraction.setXid(propertiesNode.get("xid").asText());
            attraction.setName(propertiesNode.get("name").asText());
            attraction.setDist(propertiesNode.get("dist").asDouble());

            attractions.add(attraction);
        }

        return attractions;
    }

    public static List<AttractionDescription> parseDescriptions(List<String> jsonDescriptions) {
        List<AttractionDescription> descriptions = new LinkedList<>();

        try {
            for (String jsonDesc: jsonDescriptions) {
                JsonNode node = mapper.readTree(jsonDesc).get("address");

                if (node == null)
                    continue;

                AttractionDescription description = new AttractionDescription();

                if (node.get("road") != null)
                    description.setRoad(node.get("road").asText());
                if (node.get("state") != null)
                    description.setState(node.get("state").asText());
                if (node.get("suburb") != null)
                    description.setSuburb(node.get("suburb").asText());
                if (node.get("postcode") != null)
                    description.setPostcode(node.get("postcode").asInt());
                if (node.get("house_number") != null)
                    description.setHouseNumber(node.get("house_number").asText());
                if (node.get("city_district") != null)
                    description.setCityDistrict(node.get("city_district").asText());

                descriptions.add(description);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return descriptions;
    }
}
