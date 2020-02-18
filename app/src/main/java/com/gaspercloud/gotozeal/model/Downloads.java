package com.gaspercloud.gotozeal.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Downloads implements Serializable {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("file")
    private String file;

    public Downloads() {
    }

    public Downloads(String id, String name, String file) {
        this.id = id;
        this.name = name;
        this.file = file;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "Downloads{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", file='" + file + '\'' +
                '}';
    }
}
