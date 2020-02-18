package com.gaspercloud.gotozeal.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class LineItems implements Serializable {

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("product_id")
    private int product_id;
    @SerializedName("variation_id")
    private int variation_id;
    @SerializedName("quantity")
    private int quantity;
    @SerializedName("tax_class")
    private String tax_class;
    @SerializedName("subtotal")
    private String subtotal;
    @SerializedName("subtotal_tax")
    private String subtotal_tax;
    @SerializedName("total")
    private String total;
    @SerializedName("total_tax")
    private String total_tax;
    @SerializedName("taxes")
    private List<TaxLines> taxes;
    @SerializedName("meta_data")
    private List<MetaData> meta_data;
    @SerializedName("sku")
    private String sku;
    @SerializedName("price")
    private String price;

    public LineItems() {
    }

    public LineItems(int product_id, int quantity) {
        this.product_id = product_id;
        this.quantity = quantity;
    }

    public LineItems(int product_id, int variation_id, int quantity) {
        this.product_id = product_id;
        this.variation_id = variation_id;
        this.quantity = quantity;
    }

    public LineItems(int id, String name, int product_id, int variation_id, int quantity, String tax_class, String subtotal, String subtotal_tax, String total, String total_tax, List<TaxLines> taxes, List<MetaData> meta_data, String sku, String price) {
        this.id = id;
        this.name = name;
        this.product_id = product_id;
        this.variation_id = variation_id;
        this.quantity = quantity;
        this.tax_class = tax_class;
        this.subtotal = subtotal;
        this.subtotal_tax = subtotal_tax;
        this.total = total;
        this.total_tax = total_tax;
        this.taxes = taxes;
        this.meta_data = meta_data;
        this.sku = sku;
        this.price = price;
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

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getVariation_id() {
        return variation_id;
    }

    public void setVariation_id(int variation_id) {
        this.variation_id = variation_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getTax_class() {
        return tax_class;
    }

    public void setTax_class(String tax_class) {
        this.tax_class = tax_class;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getSubtotal_tax() {
        return subtotal_tax;
    }

    public void setSubtotal_tax(String subtotal_tax) {
        this.subtotal_tax = subtotal_tax;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotal_tax() {
        return total_tax;
    }

    public void setTotal_tax(String total_tax) {
        this.total_tax = total_tax;
    }

    public List<TaxLines> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<TaxLines> taxes) {
        this.taxes = taxes;
    }

    public List<MetaData> getMeta_data() {
        return meta_data;
    }

    public void setMeta_data(List<MetaData> meta_data) {
        this.meta_data = meta_data;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "LineItems{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", product_id=" + product_id +
                ", variation_id=" + variation_id +
                ", quantity=" + quantity +
                ", tax_class='" + tax_class + '\'' +
                ", subtotal='" + subtotal + '\'' +
                ", subtotal_tax='" + subtotal_tax + '\'' +
                ", total='" + total + '\'' +
                ", total_tax=" + total_tax +
                ", taxes=" + taxes +
                ", meta_data=" + meta_data +
                ", sku='" + sku + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
