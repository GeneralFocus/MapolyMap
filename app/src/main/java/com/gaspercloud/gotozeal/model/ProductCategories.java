package com.gaspercloud.gotozeal.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ProductCategories implements Serializable {

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("slug")
    private String slug;
    @SerializedName("parent")
    private int parent;
    @SerializedName("description")
    private String description;
    @SerializedName("display")
    private String display;
    @SerializedName("image")
    private ImageProperties imageProperties;
    @SerializedName("menu_order")
    private int menu_order;
    @SerializedName("count")
    private int count;

    public ProductCategories() {
    }

    public ProductCategories(int id, String name, String slug, int parent, String description, String display, ImageProperties imageProperties, int menu_order, int count) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.parent = parent;
        this.description = description;
        this.display = display;
        this.imageProperties = imageProperties;
        this.menu_order = menu_order;
        this.count = count;
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public ImageProperties getImageProperties() {
        return imageProperties;
    }

    public void setImageProperties(ImageProperties imageProperties) {
        this.imageProperties = imageProperties;
    }

    public int getMenu_order() {
        return menu_order;
    }

    public void setMenu_order(int menu_order) {
        this.menu_order = menu_order;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "ProductCategories{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", slug='" + slug + '\'' +
                ", parent=" + parent +
                ", description='" + description + '\'' +
                ", display='" + display + '\'' +
                ", imageProperties=" + imageProperties +
                ", menu_order=" + menu_order +
                ", count=" + count +
                '}';
    }
}
