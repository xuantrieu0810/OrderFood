package com.lexuantrieu.orderfood.model;

import com.lexuantrieu.orderfood.utils.Server;

public class Category {

    private int idCategory;
    private String nameCategory;
    private String imageCategory;

    public Category(int idCategory, String nameCategory, String imageCategory) {
        this.idCategory = idCategory;
        this.nameCategory = nameCategory;
        this.imageCategory = Server.urlImage+"category/"+imageCategory;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public String getImageCategory() {
        return imageCategory;
    }

    public void setImageCategory(String imageCategory) {
        this.imageCategory = imageCategory;
    }
}

