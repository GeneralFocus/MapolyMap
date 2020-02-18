package com.gaspercloud.gotozeal.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TaxLines implements Serializable {

    @SerializedName("id")
    private int id;
    @SerializedName("rate_code")
    private String rate_code;
    @SerializedName("rate_id")
    private String rate_id;
    @SerializedName("label")
    private String label;
    @SerializedName("compound")
    private boolean compound;
    @SerializedName("tax_total")
    private String tax_total;
    @SerializedName("shipping_tax_total")
    private String shipping_tax_total;
    @SerializedName("meta_data")
    private List<MetaData> meta_data;

    public TaxLines() {
    }

    public TaxLines(int id, String rate_code, String rate_id, String label, boolean compound, String tax_total, String shipping_tax_total, List<MetaData> meta_data) {
        this.id = id;
        this.rate_code = rate_code;
        this.rate_id = rate_id;
        this.label = label;
        this.compound = compound;
        this.tax_total = tax_total;
        this.shipping_tax_total = shipping_tax_total;
        this.meta_data = meta_data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRate_code() {
        return rate_code;
    }

    public void setRate_code(String rate_code) {
        this.rate_code = rate_code;
    }

    public String getRate_id() {
        return rate_id;
    }

    public void setRate_id(String rate_id) {
        this.rate_id = rate_id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isCompound() {
        return compound;
    }

    public void setCompound(boolean compound) {
        this.compound = compound;
    }

    public String getTax_total() {
        return tax_total;
    }

    public void setTax_total(String tax_total) {
        this.tax_total = tax_total;
    }

    public String getShipping_tax_total() {
        return shipping_tax_total;
    }

    public void setShipping_tax_total(String shipping_tax_total) {
        this.shipping_tax_total = shipping_tax_total;
    }

    public List<MetaData> getMeta_data() {
        return meta_data;
    }

    public void setMeta_data(List<MetaData> meta_data) {
        this.meta_data = meta_data;
    }

    @Override
    public String toString() {
        return "TaxLines{" +
                "id=" + id +
                ", rate_code='" + rate_code + '\'' +
                ", rate_id='" + rate_id + '\'' +
                ", label='" + label + '\'' +
                ", compound=" + compound +
                ", tax_total='" + tax_total + '\'' +
                ", shipping_tax_total='" + shipping_tax_total + '\'' +
                ", meta_data=" + meta_data +
                '}';
    }
}
