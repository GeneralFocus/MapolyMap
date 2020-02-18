package com.gaspercloud.gotozeal.model;

import com.google.firebase.firestore.GeoPoint;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class State implements Serializable {

    @SerializedName("advert")
    private List<String> advert;
    @SerializedName("code")
    private String code;
    @SerializedName("countryID")
    private String countryID;
    @SerializedName("location_geopoint")
    private GeoPoint location_geopoint;
    @SerializedName("metadata")
    private List<MetaData> metadata;
    @SerializedName("name")
    private String name;
    @SerializedName("status")
    private boolean status;

    public State() {
    }

    public State(List<String> advert, String code, String countryID, GeoPoint location_geopoint, List<MetaData> metadata, String name, boolean status) {
        this.advert = advert;
        this.code = code;
        this.countryID = countryID;
        this.location_geopoint = location_geopoint;
        this.metadata = metadata;
        this.name = name;
        this.status = status;
    }

    public List<String> getAdvert() {
        return advert;
    }

    public void setAdvert(List<String> advert) {
        this.advert = advert;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCountryID() {
        return countryID;
    }

    public void setCountryID(String countryID) {
        this.countryID = countryID;
    }


    public List<MetaData> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<MetaData> metadata) {
        this.metadata = metadata;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public GeoPoint getLocation_geopoint() {
        return location_geopoint;
    }

    public void setLocation_geopoint(GeoPoint location_geopoint) {
        this.location_geopoint = location_geopoint;
    }

    @Override
    public String toString() {
        return name;
    }
}
