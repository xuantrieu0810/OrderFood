package com.lexuantrieu.orderfood.presenter;

import android.content.Context;

import com.lexuantrieu.orderfood.model.Category;

import java.util.List;

public interface SetFoodPresenter {
    void invokeData();
    interface View{
        void onInvokeDataSuccess();
        void onInvokeDataFail();
        void onInvokeDataPending();
        void onStartProcessBar(String message);
        void onStopProcessBar();
        void initAdapter(Context context, List<Category> listData);
        void initSpinner();
    }
}