package com.gaspercloud.gotozeal.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@Entity
public class CustomersOLD implements Serializable {

    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    private int id;
    @SerializedName("date_created")
    private String date_created;
    @SerializedName("date_created_gmt")
    private String date_created_gmt;
    @SerializedName("date_modified")
    private String date_modified;
    @SerializedName("date_modified_gmt")
    private String date_modified_gmt;
    @SerializedName("email")
    private String email;
    @SerializedName("first_name")
    private String first_name;
    @SerializedName("last_name")
    private String last_name;
    @SerializedName("role")
    private String role;
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("billing")
    private Billing billing;
    @SerializedName("shipping")
    private Shipping shipping;
    @SerializedName("is_paying_customer")
    private boolean is_paying_customer;
    @SerializedName("avatar_url")
    private String avatar_url;
    @SerializedName("meta_data")
    private List<MetaData> meta_data;
    @SerializedName("PayStackVerify_Transactions")
    private PayStackVerify_Transactions PayStackVerify_Transactions;

    public CustomersOLD() {
    }

    //This method is to create a simple Customer
    public CustomersOLD(String email, String first_name, String last_name, String username, String password, Billing billing, boolean is_paying_customer) {
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.password = password;
        this.billing = billing;
        this.is_paying_customer = is_paying_customer;
    }

    public com.gaspercloud.gotozeal.model.PayStackVerify_Transactions getPayStackVerify_Transactions() {
        return PayStackVerify_Transactions;
    }

    public void setPayStackVerify_Transactions(com.gaspercloud.gotozeal.model.PayStackVerify_Transactions payStackVerify_Transactions) {
        PayStackVerify_Transactions = payStackVerify_Transactions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getDate_created_gmt() {
        return date_created_gmt;
    }

    public void setDate_created_gmt(String date_created_gmt) {
        this.date_created_gmt = date_created_gmt;
    }

    public String getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(String date_modified) {
        this.date_modified = date_modified;
    }

    public String getDate_modified_gmt() {
        return date_modified_gmt;
    }

    public void setDate_modified_gmt(String date_modified_gmt) {
        this.date_modified_gmt = date_modified_gmt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Billing getBilling() {
        return billing;
    }

    public void setBilling(Billing billing) {
        this.billing = billing;
    }

    public Shipping getShipping() {
        return shipping;
    }

    public void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }

    public boolean isIs_paying_customer() {
        return is_paying_customer;
    }

    public void setIs_paying_customer(boolean is_paying_customer) {
        this.is_paying_customer = is_paying_customer;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public List<MetaData> getMeta_data() {
        return meta_data;
    }

    public void setMeta_data(List<MetaData> meta_data) {
        this.meta_data = meta_data;
    }

    @Override
    public String toString() {
        return "Customers{" +
                "id=" + id +
                ", date_created='" + date_created + '\'' +
                ", date_created_gmt='" + date_created_gmt + '\'' +
                ", date_modified='" + date_modified + '\'' +
                ", date_modified_gmt='" + date_modified_gmt + '\'' +
                ", email='" + email + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", role='" + role + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", billing=" + billing +
                ", shipping=" + shipping +
                ", is_paying_customer=" + is_paying_customer +
                ", avatar_url='" + avatar_url + '\'' +
                ", meta_data=" + meta_data +
                ", PayStackVerify_Transactions=" + PayStackVerify_Transactions +
                '}';
    }
}
