package com.lexuantrieu.orderfood.model;

import com.lexuantrieu.orderfood.utils.Server;

public class Food {
    private int stt;
    private int idFood;
    private String nameFood;
    private String nameCategory;
    private Double priceFood;
    private String imageFood;
    private int countFood;
    private String nameFoodNonVN;//tiếng việt không dấu
    private String commentFood;
    private int statusFood;

    public Food(int stt, int idFood, String nameFood, Double priceFood, String imageFood, int countFood, String commentFood, int statusFood, String nameFoodNonVN) {
        this.stt = stt;
        this.idFood = idFood;
        this.nameFood = nameFood;
        this.priceFood = priceFood;
        this.imageFood = Server.urlImage+"product/"+imageFood;
        this.countFood = countFood;
        this.commentFood = commentFood;
        this.statusFood = statusFood;
        this.nameFoodNonVN = nameFoodNonVN;
    }
    public Food(int idFood, String nameFood, Double priceFood, String imageFood, int countFood, String commentFood, int statusFood, String nameFoodNonVN) {
        this.idFood = idFood;
        this.nameFood = nameFood;
        this.priceFood = priceFood;
        this.imageFood = imageFood;
        this.countFood = countFood;
        this.commentFood = commentFood;
        this.statusFood = statusFood;
        this.nameFoodNonVN = nameFoodNonVN;
    }
    public Food(int idFood, String nameFood, Double priceFood, String imageFood, int countFood, String commentFood, int statusFood) {
        this.idFood = idFood;
        this.nameFood = nameFood;
        this.priceFood = priceFood;
        this.imageFood = imageFood;
        this.countFood = countFood;
        this.commentFood = commentFood;
        this.statusFood = statusFood;
    }

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
