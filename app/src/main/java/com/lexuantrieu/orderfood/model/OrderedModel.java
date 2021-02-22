package com.lexuantrieu.orderfood.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderedModel {

    @SerializedName("stt")
    @Expose
    private Integer stt;
    @SerializedName("bill_id")
    @Expose
    private Integer billId;
    @SerializedName("food_id")
    @Expose
    private Integer foodId;
    @SerializedName("food_name")
    @Expose
    private String foodName;
    @SerializedName("table_name")
    @Expose
    private String tableName;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;//SL đã đặt
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("status")
    @Expose
    private Integer status;


    public OrderedModel(OrderedModel m) {
        this.stt = m.stt;
        this.billId = m.billId;
        this.foodId = m.foodId;
        this.foodName = m.foodName;
        this.tableName = m.tableName;
        this.price = m.price;
        this.quantity = m.quantity;
        this.comment = m.comment;
        this.status = m.status;
    }

    public Integer getStt() {
        return stt;
    }

    public void setStt(Integer stt) {
        this.stt = stt;
    }

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}