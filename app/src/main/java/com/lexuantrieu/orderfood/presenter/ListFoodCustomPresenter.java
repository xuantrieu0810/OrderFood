package com.lexuantrieu.orderfood.presenter;

import android.content.Context;

import com.lexuantrieu.orderfood.model.Food;

import java.util.List;

public interface ListFoodCustomPresenter {
    void invokeData(int tableid);
    void InsertOrderList(int tableid, int foodid, int quantity);
    void UpdateOrderList(int stt, int tableid, int foodid, int quantity);
    interface View{
        void onInvokeDataSuccess();
        void onInvokeDataFail();
        void onInvokeDataPending();
        void onStartProcessBar(String message);
        void onStopProcessBar();
        void initAdapter(Context context, List<Food> listData);
        void initRecyclerView();
    }
}
