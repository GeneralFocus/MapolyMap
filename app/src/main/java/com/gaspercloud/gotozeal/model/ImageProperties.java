package com.gaspercloud.gotozeal.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class ImageProperties implements Serializable {

    @SerializedName("id")
    private int id;
    @SerializedName("date_created")
    private String date_created;
    @SerializedName("date_created_gmt")
    private String date_created_gmt;
    @SerializedName("date_modified")
    private String date_modified;
    @SerializedName("date_modified_gmt")
    private String date_modified_gmt;
    @SerializedName("src")
    private String src;
    @SerializedName("name")
    private String name;
    @SerializedName("alt")
    private String alt;

    public ImageProperties() {
    }

    public ImageProperties(int id, String date_created, String date_created_gmt, String date_modified, String date_modified_gmt, String src, String name, String alt) {
        this.id = id;
        this.date_created = date_created;
        this.date_created_gmt = date_created_gmt;
        this.date_modified = date_modified;
        this.date_modified_gmt = date_modified_gmt;
        this.src = src;
        this.name = name;
        this.alt = alt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getdate_created() {
        return date_created;
    }

    public void setdate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getdate_created_gmt() {
        return date_created_gmt;
    }

    public void setdate_created_gmt(String date_created_gmt) {
        this.date_created_gmt = date_created_gmt;
    }

    public String getdate_modified() {
        return date_modified;
    }

    public void setdate_modified(String date_modified) {
        this.date_modified = date_modified;
    }

    public String getdate_modified_gmt() {
        return date_modified_gmt;
    }

    public void setdate_modified_gmt(String date_modified_gmt) {
        this.date_modified_gmt = date_modified_gmt;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    @Override
    public String toString() {
        return "ImageProperties{" +
                "id=" + id +
                ", date_created=" + date_created +
                ", date_created_gmt=" + date_created_gmt +
                ", date_modified=" + date_modified +
                ", date_modified_gmt=" + date_modified_gmt +
                ", src=" + src +
                ", name=" + name +
                ", alt=" + alt +
                '}';
    }
}
