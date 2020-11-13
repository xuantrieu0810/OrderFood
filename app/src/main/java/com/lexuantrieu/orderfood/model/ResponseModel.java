package com.lexuantrieu.orderfood.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseModel<T> {
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("data")
    @Expose
    private T data = null;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
