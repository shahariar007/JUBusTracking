package com.hossain.ju.bus.model;

public class DistanceDisplayModel {
    String placeName;
    int id;

    public DistanceDisplayModel(String placeName, int id) {
        this.placeName = placeName;
        this.id = id;
    }

    public String getPlaceName() {
        return placeName;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return placeName + "   " + id;
    }
}
