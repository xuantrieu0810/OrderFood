package com.lexuantrieu.orderfood.presenter;

import android.content.Context;

import com.lexuantrieu.orderfood.model.FoodModel;

import java.util.List;

public interface ListFoodCustomPresenter {
    void invokeData(int tableid);

    void InsertOrderList(int tabeID, FoodModel foodModel, int quantity, int pos);

    void UpdateOrderList(int tableID, FoodModel foodModel, int quantity, int pos);

    interface View {
        void onInvokeDataSuccess();

        void onInvokeDataFail();

        void onInvokeDataPending();

        void onStartProcessBar(String message);

        void onStopProcessBar();

        void initAdapter(Context context, List<FoodModel> listData);

        void initRecyclerView();

        void onFailSetFood();

        void onSuccessSetFood(FoodModel foodModel, int pos);//-1 onUpdateSuccess
    }
}
