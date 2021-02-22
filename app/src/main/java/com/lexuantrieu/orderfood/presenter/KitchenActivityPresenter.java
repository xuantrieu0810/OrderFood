package com.lexuantrieu.orderfood.presenter;

import com.lexuantrieu.orderfood.model.OrderedModel;

import java.util.List;

public interface KitchenActivityPresenter {

    void invokeData();
    void changeStatusOrderList(int pos, OrderedModel mFood ,boolean isLongClick);
    interface View {
        void onInvokeDataSuccess(List<OrderedModel> listData);
        void onInvokeDataFail();

        void onFailSetStatus();
        void onSuccessSetStatus(OrderedModel orderedModel, int pos);

        void onStartProcessBar(String message);
        void onStopProcessBar();
    }
}
