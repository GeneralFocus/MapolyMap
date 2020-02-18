package com.gaspercloud.gotozeal.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class FeeLines implements Serializable {

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("tax_class")
    private String tax_class;
    @SerializedName("tax_status")
    private String tax_status;
    @SerializedName("total")
    private boolean total;
    @SerializedName("total_tax")
    private String total_tax;
    @SerializedName("taxes")
    private List<TaxLines> taxes;
    @SerializedName("meta_data")
    private List<MetaData> meta_data;

    public FeeLines() {
    }

    public FeeLines(int id, String name, String tax_class, String tax_status, boolean total, String total_tax, List<TaxLines> taxes, List<MetaData> meta_data) {
        this.id = id;
        this.name = name;
        this.tax_class = tax_class;
        this.tax_status = tax_status;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTax_class() {
        return tax_class;
    }

    public void setTax_class(String tax_class) {
        this.tax_class = tax_class;
    }

    public String getTax_status() {
        return tax_status;
    }

    public void setTax_status(String tax_status) {
        this.tax_status = tax_status;
    }

    public boolean isTotal() {
        return total;
    }

    public void setTotal(boolean total) {
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

    @Override
    public String toString() {
        return "FeeLines{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tax_class='" + tax_class + '\'' +
                ", tax_status='" + tax_status + '\'' +
                ", total=" + total +
                ", total_tax='" + total_tax + '\'' +
                ", taxes=" + taxes +
                ", meta_data=" + meta_data +
                '}';
    }
}
