package com.lexuantrieu.orderfood.presenter;

import android.content.Context;

import com.lexuantrieu.orderfood.model.CategoryModel;

import java.util.List;

public interface SetFoodActivityPresenter {
    void invokeData(int func);

    interface View {
        void onInvokeDataSuccess();

        void onInvokeDataFail();

        void onInvokeDataPending();

        void onStartProcessBar(String message);

        void onStopProcessBar();

        void initAdapter(Context context, List<CategoryModel> listData);

        void initSpinner();
    }
}
