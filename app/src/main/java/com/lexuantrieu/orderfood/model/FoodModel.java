package com.lexuantrieu.orderfood.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FoodModel {
    @SerializedName("stt")
    @Expose
    private Integer stt;
    @SerializedName("food_id")
    @Expose
    private Integer foodId;
    @SerializedName("food_name")
    @Expose
    private String foodName;

    private String categoryName;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("food_image")
    @Expose
    private String foodImage;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    private String foodNameNonAccent;//tiếng việt không dấu
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("status")
    @Expose
    private Integer status;

    public FoodModel(FoodModel model) {
        this.stt = model.stt;
        this.foodId = model.foodId;
        this.foodName = model.foodName;
        this.categoryName = model.categoryName;
        this.price = model.price;
        this.foodImage = model.foodImage;
        this.quantity = model.quantity;
        this.foodNameNonAccent = model.foodNameNonAccent;
        this.comment = model.comment;
        this.status = model.status;
    }

    public Integer getStt() {
        return stt;
    }

    public void setStt(Integer stt) {
        this.stt = stt;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getFoodNameNonAccent() {
        return foodNameNonAccent;
    }

    public void setFoodNameNonAccent(String foodNameNonAccent) {
        this.foodNameNonAccent = foodNameNonAccent;
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