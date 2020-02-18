package com.gaspercloud.gotozeal.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@Entity
public class ProductOLD implements Serializable {

    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("slug")
    private String slug;
    @SerializedName("permalink")
    private String permalink;
    @SerializedName("date_created")
    private String date_created;
    @SerializedName("date_created_gmt")
    private String date_created_gmt;
    @SerializedName("date_modified")
    private String date_modified;
    @SerializedName("date_modified_gmt")
    private String date_modified_gmt;
    @SerializedName("type")
    private String type;
    @SerializedName("status")
    private String status;
    @SerializedName("featured")
    private boolean featured;
    @SerializedName("catalog_visibility")
    private String catalog_visibility;
    @SerializedName("description")
    private String description;
    @SerializedName("short_description")
    private String short_description;
    @SerializedName("sku")
    private String sku;
    @SerializedName("price")
    private String price;
    @SerializedName("regular_price")
    private String regular_price;
    @SerializedName("sale_price")
    private String sale_price;
    @SerializedName("date_on_sale_from")
    private String date_on_sale_from;
    @SerializedName("date_on_sale_from_gmt")
    private String date_on_sale_from_gmt;
    @SerializedName("date_on_sale_to")
    private String date_on_sale_to;
    @SerializedName("date_on_sale_to_gmt")
    private String date_on_sale_to_gmt;
    @SerializedName("price_html")
    private String price_html;
    @SerializedName("on_sale")
    private boolean on_sale;
    @SerializedName("purchasable")
    private boolean purchasable;
    @SerializedName("total_sales")
    private int total_sales;
    @SerializedName("virtual")
    private boolean virtual;
    @SerializedName("downloadable")
    private boolean downloadable;
    @SerializedName("downloads")
    private List<Downloads> downloads;
    @SerializedName("download_limit")
    private int download_limit;
    @SerializedName("download_expiry")
    private int download_expiry;
    @SerializedName("external_url")
    private String external_url;
    @SerializedName("button_text")
    private String button_text;
    @SerializedName("tax_status")
    private String tax_status;
    @SerializedName("tax_class")
    private String tax_class;
    @SerializedName("manage_stock")
    private boolean manage_stock;
    @SerializedName("stock_quantity")
    private int stock_quantity;
    @SerializedName("stock_status")
    private String stock_status;
    @SerializedName("backorders")
    private String backorders;
    @SerializedName("backorders_allowed")
    private boolean backorders_allowed;
    @SerializedName("backordered")
    private boolean backordered;
    @SerializedName("sold_individually")
    private boolean sold_individually;
    @SerializedName("weight")
    private String weight;
    @SerializedName("dimensions")
    private Dimensions dimensions;
    @SerializedName("shipping_required")
    private boolean shipping_required;
    @SerializedName("shipping_taxable")
    private boolean shipping_taxable;
    @SerializedName("shipping_class")
    private String shipping_class;
    @SerializedName("shipping_class_id")
    private int shipping_class_id;
    @SerializedName("reviews_allowed")
    private boolean reviews_allowed;
    @SerializedName("average_rating")
    private String average_rating;
    @SerializedName("rating_count")
    private int rating_count;
    @SerializedName("related_ids")
    private List<Integer> related_ids;
    @SerializedName("upsell_ids")
    private List<Integer> upsell_ids;
    @SerializedName("cross_sell_ids")
    private List<Integer> cross_sell_ids;
    @SerializedName("parent_id")
    private int parent_id;
    @SerializedName("purchase_note")
    private String purchase_note;
    @SerializedName("categories")
    private List<ProductCategories> categories;
    @SerializedName("tags")
    private List<ProductCategories> tags;
    @SerializedName("images")
    private List<ImageProperties> images;
    @SerializedName("attributes")
    private List<Attributes> attributes;
    @SerializedName("default_attributes")
    private List<Attributes> default_attributes;
    @SerializedName("variations")
    private List<Integer> variations;
    @SerializedName("grouped_products")
    private List<Integer> grouped_products;
    @SerializedName("menu_order")
    private int menu_order;
    @SerializedName("meta_data")
    private List<MetaData> meta_data;

    public ProductOLD() {
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getdate_created() {
        return date_created;
    }

    public void setdate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getdate_created_gmt() {
        return date_created_gmt;
    }

    public void setdate_created_gmt(String date_created_gmt) {
        this.date_created_gmt = date_created_gmt;
    }

    public String getdate_modified() {
        return date_modified;
    }

    public void setdate_modified(String date_modified) {
        this.date_modified = date_modified;
    }

    public String getdate_modified_gmt() {
        return date_modified_gmt;
    }

    public void setdate_modified_gmt(String date_modified_gmt) {
        this.date_modified_gmt = date_modified_gmt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public String getCatalog_visibility() {
        return catalog_visibility;
    }

    public void setCatalog_visibility(String catalog_visibility) {
        this.catalog_visibility = catalog_visibility;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
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

    public String getRegular_price() {
        return regular_price;
    }

    public void setRegular_price(String regular_price) {
        this.regular_price = regular_price;
    }

    public String getSale_price() {
        return sale_price;
    }

    public void setSale_price(String sale_price) {
        this.sale_price = sale_price;
    }

    public String getdate_on_sale_from() {
        return date_on_sale_from;
    }

    public void setdate_on_sale_from(String date_on_sale_from) {
        this.date_on_sale_from = date_on_sale_from;
    }

    public String getdate_on_sale_from_gmt() {
        return date_on_sale_from_gmt;
    }

    public void setdate_on_sale_from_gmt(String date_on_sale_from_gmt) {
        this.date_on_sale_from_gmt = date_on_sale_from_gmt;
    }

    public String getdate_on_sale_to() {
        return date_on_sale_to;
    }

    public void setdate_on_sale_to(String date_on_sale_to) {
        this.date_on_sale_to = date_on_sale_to;
    }

    public String getdate_on_sale_to_gmt() {
        return date_on_sale_to_gmt;
    }

    public void setdate_on_sale_to_gmt(String date_on_sale_to_gmt) {
        this.date_on_sale_to_gmt = date_on_sale_to_gmt;
    }

    public String getPrice_html() {
        return price_html;
    }

    public void setPrice_html(String price_html) {
        this.price_html = price_html;
    }

    public boolean isOn_sale() {
        return on_sale;
    }

    public void setOn_sale(boolean on_sale) {
        this.on_sale = on_sale;
    }

    public boolean isPurchasable() {
        return purchasable;
    }

    public void setPurchasable(boolean purchasable) {
        this.purchasable = purchasable;
    }

    public int getTotal_sales() {
        return total_sales;
    }

    public void setTotal_sales(int total_sales) {
        this.total_sales = total_sales;
    }

    public boolean isVirtual() {
        return virtual;
    }

    public void setVirtual(boolean virtual) {
        this.virtual = virtual;
    }

    public boolean isDownloadable() {
        return downloadable;
    }

    public void setDownloadable(boolean downloadable) {
        this.downloadable = downloadable;
    }

    public List<Downloads> getDownloads() {
        return downloads;
    }

    public void setDownloads(List<Downloads> downloads) {
        this.downloads = downloads;
    }

    public int getDownload_limit() {
        return download_limit;
    }

    public void setDownload_limit(int download_limit) {
        this.download_limit = download_limit;
    }

    public int getDownload_expiry() {
        return download_expiry;
    }

    public void setDownload_expiry(int download_expiry) {
        this.download_expiry = download_expiry;
    }

    public String getExternal_url() {
        return external_url;
    }

    public void setExternal_url(String external_url) {
        this.external_url = external_url;
    }

    public String getButton_text() {
        return button_text;
    }

    public void setButton_text(String button_text) {
        this.button_text = button_text;
    }

    public String getTax_status() {
        return tax_status;
    }

    public void setTax_status(String tax_status) {
        this.tax_status = tax_status;
    }

    public String getTax_class() {
        return tax_class;
    }

    public void setTax_class(String tax_class) {
        this.tax_class = tax_class;
    }

    public boolean isManage_stock() {
        return manage_stock;
    }

    public void setManage_stock(boolean manage_stock) {
        this.manage_stock = manage_stock;
    }

    public int getStock_quantity() {
        return stock_quantity;
    }

    public void setStock_quantity(int stock_quantity) {
        this.stock_quantity = stock_quantity;
    }

    public String getStock_status() {
        return stock_status;
    }

    public void setStock_status(String stock_status) {
        this.stock_status = stock_status;
    }

    public String getBackorders() {
        return backorders;
    }

    public void setBackorders(String backorders) {
        this.backorders = backorders;
    }

    public boolean isBackorders_allowed() {
        return backorders_allowed;
    }

    public void setBackorders_allowed(boolean backorders_allowed) {
        this.backorders_allowed = backorders_allowed;
    }

    public boolean isBackordered() {
        return backordered;
    }

    public void setBackordered(boolean backordered) {
        this.backordered = backordered;
    }

    public boolean isSold_individually() {
        return sold_individually;
    }

    public void setSold_individually(boolean sold_individually) {
        this.sold_individually = sold_individually;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public Dimensions getDimensions() {
        return dimensions;
    }

    public void setDimensions(Dimensions dimensions) {
        this.dimensions = dimensions;
    }

    public boolean isShipping_required() {
        return shipping_required;
    }

    public void setShipping_required(boolean shipping_required) {
        this.shipping_required = shipping_required;
    }

    public boolean isShipping_taxable() {
        return shipping_taxable;
    }

    public void setShipping_taxable(boolean shipping_taxable) {
        this.shipping_taxable = shipping_taxable;
    }

    public String getShipping_class() {
        return shipping_class;
    }

    public void setShipping_class(String shipping_class) {
        this.shipping_class = shipping_class;
    }

    public int getShipping_class_id() {
        return shipping_class_id;
    }

    public void setShipping_class_id(int shipping_class_id) {
        this.shipping_class_id = shipping_class_id;
    }

    public boolean isReviews_allowed() {
        return reviews_allowed;
    }

    public void setReviews_allowed(boolean reviews_allowed) {
        this.reviews_allowed = reviews_allowed;
    }

    public String getAverage_rating() {
        return average_rating;
    }

    public void setAverage_rating(String average_rating) {
        this.average_rating = average_rating;
    }

    public int getRating_count() {
        return rating_count;
    }

    public void setRating_count(int rating_count) {
        this.rating_count = rating_count;
    }

    public List<Integer> getRelated_ids() {
        return related_ids;
    }

    public void setRelated_ids(List<Integer> related_ids) {
        this.related_ids = related_ids;
    }

    public List<Integer> getUpsell_ids() {
        return upsell_ids;
    }

    public void setUpsell_ids(List<Integer> upsell_ids) {
        this.upsell_ids = upsell_ids;
    }

    public List<Integer> getCross_sell_ids() {
        return cross_sell_ids;
    }

    public void setCross_sell_ids(List<Integer> cross_sell_ids) {
        this.cross_sell_ids = cross_sell_ids;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public String getPurchase_note() {
        return purchase_note;
    }

    public void setPurchase_note(String purchase_note) {
        this.purchase_note = purchase_note;
    }

    public List<ProductCategories> getCategories() {
        return categories;
    }

    public void setCategories(List<ProductCategories> categories) {
        this.categories = categories;
    }

    public List<ProductCategories> getTags() {
        return tags;
    }

    public void setTags(List<ProductCategories> tags) {
        this.tags = tags;
    }

    public List<ImageProperties> getImages() {
        return images;
    }

    public void setImages(List<ImageProperties> images) {
        this.images = images;
    }

    public List<Attributes> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attributes> attributes) {
        this.attributes = attributes;
    }

    public List<Attributes> getDefault_attributes() {
        return default_attributes;
    }

    public void setDefault_attributes(List<Attributes> default_attributes) {
        this.default_attributes = default_attributes;
    }

    public List<Integer> getVariations() {
        return variations;
    }

    public void setVariations(List<Integer> variations) {
        this.variations = variations;
    }

    public List<Integer> getGrouped_products() {
        return grouped_products;
    }

    public void setGrouped_products(List<Integer> grouped_products) {
        this.grouped_products = grouped_products;
    }

    public int getMenu_order() {
        return menu_order;
    }

    public void setMenu_order(int menu_order) {
        this.menu_order = menu_order;
    }

    public List<MetaData> getMeta_data() {
        return meta_data;
    }

    public void setMeta_data(List<MetaData> meta_data) {
        this.meta_data = meta_data;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", slug='" + slug + '\'' +
                ", permalink='" + permalink + '\'' +
                ", date_created=" + date_created +
                ", date_created_gmt=" + date_created_gmt +
                ", date_modified=" + date_modified +
                ", date_modified_gmt=" + date_modified_gmt +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", featured=" + featured +
                ", catalog_visibility='" + catalog_visibility + '\'' +
                ", description='" + description + '\'' +
                ", short_description='" + short_description + '\'' +
                ", sku='" + sku + '\'' +
                ", price='" + price + '\'' +
                ", regular_price='" + regular_price + '\'' +
                ", sale_price='" + sale_price + '\'' +
                ", date_on_sale_from=" + date_on_sale_from +
                ", date_on_sale_from_gmt=" + date_on_sale_from_gmt +
                ", date_on_sale_to=" + date_on_sale_to +
                ", date_on_sale_to_gmt=" + date_on_sale_to_gmt +
                ", price_html='" + price_html + '\'' +
                ", on_sale=" + on_sale +
                ", purchasable=" + purchasable +
                ", total_sales=" + total_sales +
                ", virtual=" + virtual +
                ", downloadable=" + downloadable +
                ", downloads=" + downloads +
                ", download_limit=" + download_limit +
                ", download_expiry=" + download_expiry +
                ", external_url='" + external_url + '\'' +
                ", button_text='" + button_text + '\'' +
                ", tax_status='" + tax_status + '\'' +
                ", tax_class='" + tax_class + '\'' +
                ", manage_stock=" + manage_stock +
                ", stock_quantity=" + stock_quantity +
                ", stock_status='" + stock_status + '\'' +
                ", backorders='" + backorders + '\'' +
                ", backorders_allowed=" + backorders_allowed +
                ", backordered=" + backordered +
                ", sold_individually=" + sold_individually +
                ", weight='" + weight + '\'' +
                ", dimensions=" + dimensions +
                ", shipping_required=" + shipping_required +
                ", shipping_taxable=" + shipping_taxable +
                ", shipping_class='" + shipping_class + '\'' +
                ", shipping_class_id=" + shipping_class_id +
                ", reviews_allowed=" + reviews_allowed +
                ", average_rating='" + average_rating + '\'' +
                ", rating_count=" + rating_count +
                ", related_ids=" + related_ids +
                ", upsell_ids=" + upsell_ids +
                ", cross_sell_ids=" + cross_sell_ids +
                ", parent_id=" + parent_id +
                ", purchase_note='" + purchase_note + '\'' +
                ", categories=" + categories +
                ", tags=" + tags +
                ", images=" + images +
                ", attributes=" + attributes +
                ", default_attributes=" + default_attributes +
                ", variations=" + variations +
                ", grouped_products=" + grouped_products +
                ", menu_order=" + menu_order +
                ", meta_data=" + meta_data +
                '}';
    }
}
