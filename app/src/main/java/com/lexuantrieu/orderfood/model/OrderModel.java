package com.lexuantrieu.orderfood.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderModel {

    @SerializedName("stt")
    @Expose
    private Integer stt;
    @SerializedName("food_id")
    @Expose
    private Integer idFood;
    @SerializedName("bill_id")
    @Expose
    private Integer idBill;
    @SerializedName("food_name")
    @Expose
    private String nameFood;
    @SerializedName("table_name")
    @Expose
    private String nameTable;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;//SL đã đặt
    @SerializedName("comment")
    @Expose
    private String commentFood;
    @SerializedName("status")
    @Expose
    private Integer statusFood;


    public OrderModel(int stt, int idBill, int idFood, String nameFood, int quantity, String nameTable, int statusFood, String commentFood) {
        this.stt = stt;
        this.idBill = idBill;
        this.idFood = idFood;
        this.nameFood = nameFood;
        this.quantity = quantity;
        this.nameTable = nameTable;
        this.statusFood = statusFood;
        this.commentFood = commentFood;
    }

    public Integer getStt() {
        return stt;
    }

    public void setStt(Integer stt) {
        this.stt = stt;
    }

    public Integer getIdFood() {
        return idFood;
    }

    public void setIdFood(Integer idFood) {
        this.idFood = idFood;
    }

    public Integer getIdBill() {
        return idBill;
    }

    public void setIdBill(Integer idBill) {
        this.idBill = idBill;
    }

    public String getNameFood() {
        return nameFood;
    }

    public void setNameFood(String nameFood) {
        this.nameFood = nameFood;
    }

    public String getNameTable() {
        return nameTable;
    }

    public void setNameTable(String nameTable) {
        this.nameTable = nameTable;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCommentFood() {
        return commentFood;
    }

    public void setCommentFood(String commentFood) {
        this.commentFood = commentFood;
    }

    public Integer getStatusFood() {
        return statusFood;
    }

    public void setStatusFood(Integer statusFood) {
        this.statusFood = statusFood;
    }
}