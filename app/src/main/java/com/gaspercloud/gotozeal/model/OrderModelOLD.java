package com.gaspercloud.gotozeal.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@Entity
public class OrderModelOLD implements Serializable {

    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    private int id;
    @SerializedName("parent_id")
    private int parent_id;
    @SerializedName("number")
    private int number;
    @SerializedName("order_key")
    private String order_key;
    @SerializedName("created_via")
    private String created_via;
    @SerializedName("version")
    private String version;
    @SerializedName("status")
    private String status;
    @SerializedName("currency")
    private String currency;
    @SerializedName("date_created")
    private String date_created;
    @SerializedName("date_created_gmt")
    private String date_created_gmt;
    @SerializedName("date_modified")
    private String date_modified;
    @SerializedName("date_modified_gmt")
    private String date_modified_gmt;
    @SerializedName("discount_total")
    private String discount_total;
    @SerializedName("discount_tax")
    private String discount_tax;
    @SerializedName("shipping_total")
    private String shipping_total;
    @SerializedName("shipping_tax")
    private String shipping_tax;
    @SerializedName("cart_tax")
    private String cart_tax;
    @SerializedName("total")
    private String total;
    @SerializedName("total_tax")
    private String total_tax;
    @SerializedName("prices_include_tax")
    private boolean prices_include_tax;
    @SerializedName("customer_id")
    private int customer_id;
    @SerializedName("customer_ip_address")
    private String customer_ip_address;
    @SerializedName("customer_user_agent")
    private String customer_user_agent;
    @SerializedName("customer_note")
    private String customer_note;
    @SerializedName("billing")
    private Billing billing;
    @SerializedName("shipping")
    private Shipping shipping;
    @SerializedName("payment_method")
    private String payment_method;
    @SerializedName("payment_method_title")
    private String payment_method_title;
    @SerializedName("transaction_id")
    private String transaction_id;
    @SerializedName("date_paid")
    private String date_paid;
    @SerializedName("date_paid_gmt")
    private String date_paid_gmt;
    @SerializedName("date_completed")
    private String date_completed;
    @SerializedName("date_completed_gmt")
    private String date_completed_gmt;
    @SerializedName("cart_hash")
    private String cart_hash;
    @SerializedName("meta_data")
    private List<MetaData> meta_data;
    @SerializedName("line_items")
    private List<LineItems> line_items;
    @SerializedName("tax_lines")
    private List<TaxLines> tax_lines;
    @SerializedName("shipping_lines")
    private List<ShippingLines> shipping_lines;
    @SerializedName("fee_lines")
    private List<FeeLines> fee_lines;
    @SerializedName("coupon_lines")
    private List<CouponLines> coupon_lines;
    @SerializedName("refunds")
    private List<Refunds> refunds;
    @SerializedName("set_paid")
    private boolean set_paid;

    public OrderModelOLD() {
    }

//    public OrderModel(String currency, int customer_id, Billing billing, Shipping shipping, String payment_method, String payment_method_title, String transaction_id, List<LineItems> line_items, boolean set_paid, List<ShippingLines> shipping_lines) {
//        this.currency = currency;
//        this.customer_id = customer_id;
//        this.billing = billing;
//        this.shipping = shipping;
//        this.payment_method = payment_method;
//        this.payment_method_title = payment_method_title;
//        this.transaction_id = transaction_id;
//        this.line_items = line_items;
//        this.set_paid = set_paid;
//        this.shipping_lines = shipping_lines;
//    }

    public OrderModelOLD(int id, int parent_id, int number, String order_key, String created_via, String version, String status, String currency, String date_created, String date_created_gmt, String date_modified, String date_modified_gmt, String discount_total, String discount_tax, String shipping_total, String shipping_tax, String cart_tax, String total, String total_tax, boolean prices_include_tax, int customer_id, String customer_ip_address, String customer_user_agent, String customer_note, Billing billing, Shipping shipping, String payment_method, String payment_method_title, String transaction_id, String date_paid, String date_paid_gmt, String date_completed, String date_completed_gmt, String cart_hash, List<MetaData> meta_data, List<LineItems> line_items, List<TaxLines> tax_lines, List<ShippingLines> shipping_lines, List<FeeLines> fee_lines, List<CouponLines> coupon_lines, List<Refunds> refunds, boolean set_paid) {
        this.id = id;
        this.parent_id = parent_id;
        this.number = number;
        this.order_key = order_key;
        this.created_via = created_via;
        this.version = version;
        this.status = status;
        this.currency = currency;
        this.date_created = date_created;
        this.date_created_gmt = date_created_gmt;
        this.date_modified = date_modified;
        this.date_modified_gmt = date_modified_gmt;
        this.discount_total = discount_total;
        this.discount_tax = discount_tax;
        this.shipping_total = shipping_total;
        this.shipping_tax = shipping_tax;
        this.cart_tax = cart_tax;
        this.total = total;
        this.total_tax = total_tax;
        this.prices_include_tax = prices_include_tax;
        this.customer_id = customer_id;
        this.customer_ip_address = customer_ip_address;
        this.customer_user_agent = customer_user_agent;
        this.customer_note = customer_note;
        this.billing = billing;
        this.shipping = shipping;
        this.payment_method = payment_method;
        this.payment_method_title = payment_method_title;
        this.transaction_id = transaction_id;
        this.date_paid = date_paid;
        this.date_paid_gmt = date_paid_gmt;
        this.date_completed = date_completed;
        this.date_completed_gmt = date_completed_gmt;
        this.cart_hash = cart_hash;
        this.meta_data = meta_data;
        this.line_items = line_items;
        this.tax_lines = tax_lines;
        this.shipping_lines = shipping_lines;
        this.fee_lines = fee_lines;
        this.coupon_lines = coupon_lines;
        this.refunds = refunds;
        this.set_paid = set_paid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getOrder_key() {
        return order_key;
    }

    public void setOrder_key(String order_key) {
        this.order_key = order_key;
    }

    public String getCreated_via() {
        return created_via;
    }

    public void setCreated_via(String created_via) {
        this.created_via = created_via;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    public String getDiscount_total() {
        return discount_total;
    }

    public void setDiscount_total(String discount_total) {
        this.discount_total = discount_total;
    }

    public String getDiscount_tax() {
        return discount_tax;
    }

    public void setDiscount_tax(String discount_tax) {
        this.discount_tax = discount_tax;
    }

    public String getShipping_total() {
        return shipping_total;
    }

    public void setShipping_total(String shipping_total) {
        this.shipping_total = shipping_total;
    }

    public String getShipping_tax() {
        return shipping_tax;
    }

    public void setShipping_tax(String shipping_tax) {
        this.shipping_tax = shipping_tax;
    }

    public String getCart_tax() {
        return cart_tax;
    }

    public void setCart_tax(String cart_tax) {
        this.cart_tax = cart_tax;
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

    public boolean isPrices_include_tax() {
        return prices_include_tax;
    }

    public void setPrices_include_tax(boolean prices_include_tax) {
        this.prices_include_tax = prices_include_tax;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_ip_address() {
        return customer_ip_address;
    }

    public void setCustomer_ip_address(String customer_ip_address) {
        this.customer_ip_address = customer_ip_address;
    }

    public String getCustomer_user_agent() {
        return customer_user_agent;
    }

    public void setCustomer_user_agent(String customer_user_agent) {
        this.customer_user_agent = customer_user_agent;
    }

    public String getCustomer_note() {
        return customer_note;
    }

    public void setCustomer_note(String customer_note) {
        this.customer_note = customer_note;
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

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getPayment_method_title() {
        return payment_method_title;
    }

    public void setPayment_method_title(String payment_method_title) {
        this.payment_method_title = payment_method_title;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getDate_paid() {
        return date_paid;
    }

    public void setDate_paid(String date_paid) {
        this.date_paid = date_paid;
    }

    public String getDate_paid_gmt() {
        return date_paid_gmt;
    }

    public void setDate_paid_gmt(String date_paid_gmt) {
        this.date_paid_gmt = date_paid_gmt;
    }

    public String getDate_completed() {
        return date_completed;
    }

    public void setDate_completed(String date_completed) {
        this.date_completed = date_completed;
    }

    public String getDate_completed_gmt() {
        return date_completed_gmt;
    }

    public void setDate_completed_gmt(String date_completed_gmt) {
        this.date_completed_gmt = date_completed_gmt;
    }

    public String getCart_hash() {
        return cart_hash;
    }

    public void setCart_hash(String cart_hash) {
        this.cart_hash = cart_hash;
    }

    public List<MetaData> getMeta_data() {
        return meta_data;
    }

    public void setMeta_data(List<MetaData> meta_data) {
        this.meta_data = meta_data;
    }

    public List<LineItems> getLine_items() {
        return line_items;
    }

    public void setLine_items(List<LineItems> line_items) {
        this.line_items = line_items;
    }

    public List<TaxLines> getTax_lines() {
        return tax_lines;
    }

    public void setTax_lines(List<TaxLines> tax_lines) {
        this.tax_lines = tax_lines;
    }

    public List<ShippingLines> getShipping_lines() {
        return shipping_lines;
    }

    public void setShipping_lines(List<ShippingLines> shipping_lines) {
        this.shipping_lines = shipping_lines;
    }

    public List<FeeLines> getFee_lines() {
        return fee_lines;
    }

    public void setFee_lines(List<FeeLines> fee_lines) {
        this.fee_lines = fee_lines;
    }

    public List<CouponLines> getCoupon_lines() {
        return coupon_lines;
    }

    public void setCoupon_lines(List<CouponLines> coupon_lines) {
        this.coupon_lines = coupon_lines;
    }

    public List<Refunds> getRefunds() {
        return refunds;
    }

    public void setRefunds(List<Refunds> refunds) {
        this.refunds = refunds;
    }

    public boolean isSet_paid() {
        return set_paid;
    }

    public void setSet_paid(boolean set_paid) {
        this.set_paid = set_paid;
    }

    @Override
    public String toString() {
        return "OrderModel{" +
                "id=" + id +
                ", parent_id=" + parent_id +
                ", number=" + number +
                ", order_key='" + order_key + '\'' +
                ", created_via='" + created_via + '\'' +
                ", version='" + version + '\'' +
                ", status='" + status + '\'' +
                ", currency='" + currency + '\'' +
                ", date_created='" + date_created + '\'' +
                ", date_created_gmt='" + date_created_gmt + '\'' +
                ", date_modified='" + date_modified + '\'' +
                ", date_modified_gmt='" + date_modified_gmt + '\'' +
                ", discount_total='" + discount_total + '\'' +
                ", discount_tax='" + discount_tax + '\'' +
                ", shipping_total='" + shipping_total + '\'' +
                ", shipping_tax='" + shipping_tax + '\'' +
                ", cart_tax='" + cart_tax + '\'' +
                ", total='" + total + '\'' +
                ", total_tax='" + total_tax + '\'' +
                ", prices_include_tax=" + prices_include_tax +
                ", customer_id=" + customer_id +
                ", customer_ip_address='" + customer_ip_address + '\'' +
                ", customer_user_agent='" + customer_user_agent + '\'' +
                ", customer_note='" + customer_note + '\'' +
                ", billing=" + billing +
                ", shipping=" + shipping +
                ", payment_method='" + payment_method + '\'' +
                ", payment_method_title='" + payment_method_title + '\'' +
                ", transaction_id='" + transaction_id + '\'' +
                ", date_paid='" + date_paid + '\'' +
                ", date_paid_gmt='" + date_paid_gmt + '\'' +
                ", date_completed='" + date_completed + '\'' +
                ", date_completed_gmt='" + date_completed_gmt + '\'' +
                ", cart_hash='" + cart_hash + '\'' +
                ", meta_data=" + meta_data +
                ", line_items=" + line_items +
                ", tax_lines=" + tax_lines +
                ", shipping_lines=" + shipping_lines +
                ", fee_lines=" + fee_lines +
                ", coupon_lines=" + coupon_lines +
                ", refunds=" + refunds +
                ", set_paid=" + set_paid +
                '}';
    }
}
