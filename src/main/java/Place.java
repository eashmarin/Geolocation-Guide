public class Place {
    private double lng;
    private double lat;
    private String xid;
    private String name;
    private double dist;

    private PlaceDescription description;

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

    @Override
    public String toString() {
        return String.format("%f %f %s %s %f", lng, lat, xid, name, dist);
    }

    public PlaceDescription getDescription() {
        return description;
    }

    public void setDescription(PlaceDescription description) {
        this.description = description;
    }
}
