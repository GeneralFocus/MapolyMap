package com.gaspercloud.gotozeal.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CouponLines implements Serializable {

    @SerializedName("id")
    private int id;
    @SerializedName("code")
    private String code;
    @SerializedName("discount")
    private String discount;
    @SerializedName("discount_tax")
    private String discount_tax;
    @SerializedName("meta_data")
    private List<MetaData> meta_data;

    public CouponLines() {
    }

    public CouponLines(int id, String code, String discount, String discount_tax, List<MetaData> meta_data) {
        this.id = id;
        this.code = code;
        this.discount = discount;
        this.discount_tax = discount_tax;
        this.meta_data = meta_data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscount_tax() {
        return discount_tax;
    }

    public void setDiscount_tax(String discount_tax) {
        this.discount_tax = discount_tax;
    }

    public List<MetaData> getMeta_data() {
        return meta_data;
    }

    public void setMeta_data(List<MetaData> meta_data) {
        this.meta_data = meta_data;
    }

    @Override
    public String toString() {
        return "CouponLines{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", discount='" + discount + '\'' +
                ", discount_tax='" + discount_tax + '\'' +
                ", meta_data=" + meta_data +
                '}';
    }
}
