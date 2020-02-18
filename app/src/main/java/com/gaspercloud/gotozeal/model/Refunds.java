package com.gaspercloud.gotozeal.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Refunds implements Serializable {

    @SerializedName("id")
    private int id;
    @SerializedName("reason")
    private String reason;
    @SerializedName("total")
    private String total;

    public Refunds() {
    }

    public Refunds(int id, String reason, String total) {
        this.id = id;
        this.reason = reason;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Refunds{" +
                "id=" + id +
                ", reason='" + reason + '\'' +
                ", total='" + total + '\'' +
                '}';
    }
}
