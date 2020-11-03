package com.lexuantrieu.orderfood.presenter;

import android.content.Context;

import com.lexuantrieu.orderfood.model.Food;

import java.util.List;

public interface ListFoodCustomPresenter {
    void invokeData(int tableid);
    void InsertOrderList(int tabeID, Food food, int quantity, int pos);
    void UpdateOrderList(int tableID, Food food, int quantity, int pos);
    interface View{
        void onInvokeDataSuccess();
        void onInvokeDataFail();
        void onInvokeDataPending();
        void onStartProcessBar(String message);
        void onStopProcessBar();
        void initAdapter(Context context, List<Food> listData);
        void initRecyclerView();
        void onFailSetFood();

        void onSuccessSetFood(Food food, int pos);//-1 onUpdateSuccess
    }
}
