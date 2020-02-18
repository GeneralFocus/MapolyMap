package com.gaspercloud.gotozeal.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ShippingLines implements Serializable {

    @SerializedName("id")
    private int id;
    @SerializedName("method_title")
    private String method_title;
    @SerializedName("method_id")
    private String method_id;
    @SerializedName("total")
    private String total;
    @SerializedName("total_tax")
    private String total_tax;
    @SerializedName("taxes")
    private List<TaxLines> taxes;
    @SerializedName("meta_data")
    private List<MetaData> meta_data;

    public ShippingLines() {
    }

    public ShippingLines(int id, String method_title, String method_id, String total, String total_tax, List<TaxLines> taxes, List<MetaData> meta_data) {
        this.id = id;
        this.method_title = method_title;
        this.method_id = method_id;
        this.total = total;
        this.total_tax = total_tax;
        this.taxes = taxes;
        this.meta_data = meta_data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMethod_title() {
        return method_title;
    }

    public void setMethod_title(String method_title) {
        this.method_title = method_title;
    }

    public String getMethod_id() {
        return method_id;
    }

    public void setMethod_id(String method_id) {
        this.method_id = method_id;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String isTotal_tax() {
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

    @Override
    public String toString() {
        return "ShippingLines{" +
                "id=" + id +
                ", method_title='" + method_title + '\'' +
                ", method_id='" + method_id + '\'' +
                ", total='" + total + '\'' +
                ", total_tax=" + total_tax +
                ", taxes='" + taxes + '\'' +
                ", meta_data=" + meta_data +
                '}';
    }
}
