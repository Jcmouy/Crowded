package com.example.crowded.database.entity;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Coordinates")
public class Coordinates implements Serializable {

    @ColumnInfo(name = "longitude")
    @NonNull
    public Double longitude;

    @ColumnInfo(name = "latitude")
    @NonNull
    public Double latitude;

    @NonNull
    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @NonNull
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
