package com.example.austinchang.testproject;

/**
 * Created by N on 11/21/2016.
 */
public class Place {

    private String name;
    private String cloudTag;
    private Double longitude;
    private Double latitude;
    //In meters
    static final int radius = 15;


    public Place(String in_name, Double in_longitude, Double in_latitude, String in_cloudTag){
        setName(in_name);
        setLongitude(in_longitude);
        setLatitude(in_latitude);
        setCloudTag(in_cloudTag);
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getCloudTag() {
        return cloudTag;
    }

    public void setCloudTag(String cloudTag) {
        this.cloudTag = cloudTag;
    }
}
