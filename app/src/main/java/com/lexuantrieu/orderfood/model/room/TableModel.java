package com.lexuantrieu.orderfood.model.room;

public class TableModel {
    private int IdTable;
    private String NameTable;
    private int ImageTable;
    private int statust;

    public TableModel(int idTable, String nameTable, int imageTable, int statust) {
        IdTable = idTable;
        NameTable = nameTable;
        ImageTable = imageTable;
        this.statust = statust;
    }

    public int getIdTable() {
        return IdTable;
    }

    public void setIdTable(int idTable) {
        IdTable = idTable;
    }

    public String getNameTable() {
        return NameTable;
    }

    public void setNameTable(String nameTable) {
        NameTable = nameTable;
    }

    public int getImageTable() {
        return ImageTable;
    }

    public void setImageTable(int imageTable) {
        ImageTable = imageTable;
    }

    public int getStatust() {
        return statust;
    }

    public void setStatust(int statust) {
        this.statust = statust;
    }
}