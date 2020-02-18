package com.gaspercloud.gotozeal.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity
public class Customers implements Serializable {

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
    private String billing;
    @SerializedName("is_paying_customer")
    private boolean is_paying_customer;
    @SerializedName("avatar_url")
    private String avatar_url;

    public Customers() {
    }

    //This method is to create a simple Customer
    @Ignore
    public Customers(String email, String first_name, String last_name, String username, String password, String billing, boolean is_paying_customer) {
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.password = password;
        this.billing = billing;
        this.is_paying_customer = is_paying_customer;
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

    public String getBilling() {
        return billing;
    }

    public void setBilling(String billing) {
        this.billing = billing;
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
                ", is_paying_customer=" + is_paying_customer +
                ", avatar_url='" + avatar_url + '\'' +
                '}';
    }
}
