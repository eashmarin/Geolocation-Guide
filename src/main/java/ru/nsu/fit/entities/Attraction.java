package ru.nsu.fit.entities;

public class Attraction {
    private double lng;
    private double lat;
    private String xid;
    private String name;
    private double dist;

    private AttractionDescription description;

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getXid() {
        return xid;
    }

    public void setXid(String xid) {
        this.xid = xid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDist() {
        return dist;
    }

    public void setDist(double dist) {
        this.dist = dist;
    }

    public AttractionDescription getDescription() {
        return description;
    }

    public void setDescription(AttractionDescription description) {
        this.description = description;
    }
}
