package com.gaspercloud.gotozeal.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Attributes implements Serializable {

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("position")
    private int position;
    @SerializedName("visible")
    private boolean visible;
    @SerializedName("variation")
    private boolean variation;
    @SerializedName("options")
    private List<MetaData> options;

    public Attributes() {
    }

    public Attributes(int id, String name, int position, boolean visible, boolean variation, List<MetaData> options) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.visible = visible;
        this.variation = variation;
        this.options = options;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVariation() {
        return variation;
    }

    public void setVariation(boolean variation) {
        this.variation = variation;
    }

    public List<MetaData> getOptions() {
        return options;
    }

    public void setOptions(List<MetaData> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "Attributes{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", position=" + position +
                ", visible=" + visible +
                ", variation=" + variation +
                ", options=" + options +
                '}';
    }
}
