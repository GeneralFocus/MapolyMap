package com.gaspercloud.gotozeal.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class CartModel implements Serializable {

    @SerializedName("no_of_items")
    private int no_of_items;

    @SerializedName("product")
    private Product product;

    public CartModel() {
    }

    public CartModel(int no_of_items, Product product) {
        this.no_of_items = no_of_items;
        this.product = product;
    }

    public int getNo_of_items() {
        return no_of_items;
    }

    public void setNo_of_items(int no_of_items) {
        this.no_of_items = no_of_items;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
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
