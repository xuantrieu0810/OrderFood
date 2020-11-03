package com.lexuantrieu.orderfood.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Food {
    @SerializedName("stt")
    @Expose
    private Integer stt;
    @SerializedName("id")
    @Expose
    private Integer idFood;
    @SerializedName("name")
    @Expose
    private String nameFood;

    private String nameCategory;
    @SerializedName("price")
    @Expose
    private Double priceFood;
    @SerializedName("image")
    @Expose
    private String imageFood;
    @SerializedName("quantity")
    @Expose
    private Integer countFood;
    private String nameFoodNonVN;//tiếng việt không dấu
    @SerializedName("comment")
    @Expose
    private String commentFood;
    @SerializedName("status")
    @Expose
    private Integer statusFood;

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public int getIdFood() {
        return idFood;
    }

    public void setIdFood(int idFood) {
        this.idFood = idFood;
    }

    public String getNameFood() {
        return nameFood;
    }

    public void setNameFood(String nameFood) {
        this.nameFood = nameFood;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public Double getPriceFood() {
        return priceFood;
    }

    public void setPriceFood(Double priceFood) {
        this.priceFood = priceFood;
    }

    public String getImageFood() {
        return imageFood;
    }

    public void setImageFood(String imageFood) {
        this.imageFood = imageFood;
    }

    public int getCountFood() {
        return countFood;
    }

    public void setCountFood(int countFood) {
        this.countFood = countFood;
    }

    public String getNameFoodNonVN() {
        return nameFoodNonVN;
    }

    public void setNameFoodNonVN(String nameFoodNonVN) {
        this.nameFoodNonVN = nameFoodNonVN;
    }

    public String getCommentFood() {
        return commentFood;
    }

    public void setCommentFood(String commentFood) {
        this.commentFood = commentFood;
    }

    public int getStatusFood() {
        return statusFood;
    }

    public void setStatusFood(int statusFood) {
        this.statusFood = statusFood;
    }
}