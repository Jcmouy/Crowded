package com.example.crowded.dto;

import java.io.Serializable;

public class CoordinatesDto implements Serializable {

    private Double longitude;

    private Double latitude;

    public CoordinatesDto() {
    }

    public CoordinatesDto(Double longitude, Double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "CoordinatesDto{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
