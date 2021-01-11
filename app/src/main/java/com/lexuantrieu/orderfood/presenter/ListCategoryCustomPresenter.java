package com.lexuantrieu.orderfood.presenter;

import android.content.Context;

import com.lexuantrieu.orderfood.model.CategoryModel;

import java.util.List;

public interface ListCategoryCustomPresenter {
    void invokeData();

    interface View {
        void onInvokeDataSuccess();

        void onInvokeDataFail();

        void onInvokeDataPending();

        void onStartProcessBar(String message);

        void onStopProcessBar();

        void initAdapter(Context context, List<CategoryModel> listData);

        void initRecyclerView();
    }
}
