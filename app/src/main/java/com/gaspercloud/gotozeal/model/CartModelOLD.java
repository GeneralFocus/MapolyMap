package com.gaspercloud.gotozeal.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CartModelOLD implements Serializable {

    @SerializedName("no_of_items")
    private int no_of_items;

    @SerializedName("product")
    private ProductOLD product;

    public CartModelOLD() {
    }

    public CartModelOLD(int no_of_items, ProductOLD product) {
        this.no_of_items = no_of_items;
        this.product = product;
    }

    public int getNo_of_items() {
        return no_of_items;
    }

    public void setNo_of_items(int no_of_items) {
        this.no_of_items = no_of_items;
    }

    public ProductOLD getProduct() {
        return product;
    }

    public void setProduct(ProductOLD product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "CartModel{" +
                "no_of_items=" + no_of_items +
                ", product=" + product +
                '}';
    }
}
