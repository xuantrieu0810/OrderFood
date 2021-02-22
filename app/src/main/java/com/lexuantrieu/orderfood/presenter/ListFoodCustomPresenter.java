package com.lexuantrieu.orderfood.presenter;

import android.content.Context;

import com.lexuantrieu.orderfood.model.FoodModel;

import java.util.List;

public interface ListFoodCustomPresenter {

    void invokeDataMultiType(int tableid, String func);

    void invokeDataOrderList(int tableid, int billid);

    void InsertOrderList(int bill_id, int table_id, int pos, FoodModel foodModel);

    void UpdateOrderList(int bill_id, int table_id, int pos, FoodModel foodModel);

    interface View {
        void onInvokeDataSuccess();

        void onInvokeDataFail();

        void onStartProcessBar(String message);

        void onStopProcessBar();

        void initAdapter(Context context, List<FoodModel> listData);

        void initRecyclerView();

        void onFailSetFood();

        void onSuccessSetFood(FoodModel foodModel, int pos);//-1 onUpdateSuccess
    }
}
