package com.gaspercloud.gotozeal.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PayStackVerify_Transactions implements Serializable {

    @SerializedName("status")
    private boolean status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private Data_Verify data;

    public PayStackVerify_Transactions() {
    }

    public PayStackVerify_Transactions(boolean status, String message, Data_Verify data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data_Verify getData() {
        return data;
    }

    public void setData(Data_Verify data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PayStackVerify_Transactions{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

}

class Data_Verify implements Serializable {

    @SerializedName("amount")
    private int amount;
    @SerializedName("currency")
    private String currency;
    @SerializedName("transaction_date")
    private String transaction_date;
    @SerializedName("status")
    private String status;
    @SerializedName("reference")
    private String reference;
    @SerializedName("domain")
    private String domain;
    @SerializedName("gateway_response")
    private String gateway_response;
    @SerializedName("message")
    private String message;
    @SerializedName("channel")
    private String channel;
    @SerializedName("message")
    private String ip_address;
    @SerializedName("log")
    private String log;
    @SerializedName("metadata")
    private MetaData_PayStack metadata;
    @SerializedName("fees")
    private String fees;
    @SerializedName("authorization")
    private Authorize_payStack authorization;
    @SerializedName("customer")
    private Customer_PayStack customer;
    @SerializedName("plan")
    private String plan;

    public Data_Verify() {
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(String transaction_date) {
        this.transaction_date = transaction_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getGateway_response() {
        return gateway_response;
    }

    public void setGateway_response(String gateway_response) {
        this.gateway_response = gateway_response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public Authorize_payStack getAuthorization() {
        return authorization;
    }

    public void setAuthorization(Authorize_payStack authorization) {
        this.authorization = authorization;
    }

    public MetaData_PayStack getMetadata() {
        return metadata;
    }

    public void setMetadata(MetaData_PayStack metadata) {
        this.metadata = metadata;
    }

    public Customer_PayStack getCustomer() {
        return customer;
    }

    public void setCustomer(Customer_PayStack customer) {
        this.customer = customer;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    @Override
    public String toString() {
        return "Data_Verify{" +
                "amount=" + amount +
                ", currency='" + currency + '\'' +
                ", transaction_date='" + transaction_date + '\'' +
                ", status='" + status + '\'' +
                ", reference='" + reference + '\'' +
                ", domain='" + domain + '\'' +
                ", metadata=" + metadata +
                ", gateway_response='" + gateway_response + '\'' +
                ", message='" + message + '\'' +
                ", channel='" + channel + '\'' +
                ", ip_address='" + ip_address + '\'' +
                ", log='" + log + '\'' +
                ", fees='" + fees + '\'' +
                ", authorization=" + authorization +
                ", customer='" + customer + '\'' +
                ", plan='" + plan + '\'' +
                '}';
    }
}

class Authorize_payStack implements Serializable {

    @SerializedName("authorization_code")
    private String authorization_code;
    @SerializedName("bin")
    private String bin;
    @SerializedName("last4")
    private String last4;
    @SerializedName("exp_month")
    private String exp_month;
    @SerializedName("exp_year")
    private String exp_year;
    @SerializedName("channel")
    private String channel;
    @SerializedName("card_type")
    private String card_type;
    @SerializedName("bank")
    private String bank;
    @SerializedName("country_code")
    private String country_code;
    @SerializedName("brand")
    private String brand;
    @SerializedName("reusable")
    private boolean reusable;
    @SerializedName("signature")
    private String signature;

    public Authorize_payStack() {
    }

    public String getAuthorization_code() {
        return authorization_code;
    }

    public void setAuthorization_code(String authorization_code) {
        this.authorization_code = authorization_code;
    }

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public String getLast4() {
        return last4;
    }

    public void setLast4(String last4) {
        this.last4 = last4;
    }

    public String getExp_month() {
        return exp_month;
    }

    public void setExp_month(String exp_month) {
        this.exp_month = exp_month;
    }

    public String getExp_year() {
        return exp_year;
    }

    public void setExp_year(String exp_year) {
        this.exp_year = exp_year;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public boolean isReusable() {
        return reusable;
    }

    public void setReusable(boolean reusable) {
        this.reusable = reusable;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "Authorize_payStack{" +
                "authorization_code='" + authorization_code + '\'' +
                ", bin='" + bin + '\'' +
                ", last4='" + last4 + '\'' +
                ", exp_month='" + exp_month + '\'' +
                ", exp_year='" + exp_year + '\'' +
                ", channel='" + channel + '\'' +
                ", card_type='" + card_type + '\'' +
                ", bank='" + bank + '\'' +
                ", country_code='" + country_code + '\'' +
                ", brand='" + brand + '\'' +
                ", reusable=" + reusable +
                ", signature='" + signature + '\'' +
                '}';
    }
}


class Customer_PayStack implements Serializable {

    @SerializedName("id")
    private int id;
    @SerializedName("first_name")
    private String first_name;
    @SerializedName("last_name")
    private String last_name;
    @SerializedName("email")
    private String email;
    @SerializedName("customer_code")
    private String customer_code;
    @SerializedName("phone")
    private String phone;
    @SerializedName("metadata")
    private MetaData_PayStack metadata;
    @SerializedName("risk_action")
    private String risk_action;

    public Customer_PayStack() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(String customer_code) {
        this.customer_code = customer_code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public MetaData_PayStack getMetadata() {
        return metadata;
    }

    public void setMetadata(MetaData_PayStack metadata) {
        this.metadata = metadata;
    }

    public String getRisk_action() {
        return risk_action;
    }

    public void setRisk_action(String risk_action) {
        this.risk_action = risk_action;
    }

    @Override
    public String toString() {
        return "Customer_PayStack{" +
                "id=" + id +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", customer_code='" + customer_code + '\'' +
                ", phone='" + phone + '\'' +
                ", metadata=" + metadata +
                ", risk_action='" + risk_action + '\'' +
                '}';
    }
}

class MetaData_PayStack implements Serializable {

    @SerializedName("custom_fields")
    private List<Custome_Fields_PayStack> custom_fields;

    public MetaData_PayStack() {
    }

    public List<Custome_Fields_PayStack> getCustom_fields() {
        return custom_fields;
    }

    public void setCustom_fields(List<Custome_Fields_PayStack> custom_fields) {
        this.custom_fields = custom_fields;
    }

    @Override
    public String toString() {
        return "MetaData_PayStack{" +
                "custom_fields=" + custom_fields +
                '}';
    }
}

class Custome_Fields_PayStack implements Serializable {
    @SerializedName("display_name")
    private String display_name;
    @SerializedName("variable_name")
    private String variable_name;
    @SerializedName("value")
    private String value;

    public Custome_Fields_PayStack() {
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getVariable_name() {
        return variable_name;
    }

    public void setVariable_name(String variable_name) {
        this.variable_name = variable_name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Custome_Fields_PayStack{" +
                "display_name='" + display_name + '\'' +
                ", variable_name='" + variable_name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}