package com.lexuantrieu.orderfood.presenter;

import android.content.Context;

import com.lexuantrieu.orderfood.model.TableModel;

import java.util.List;

public interface ListTableActivityPresenter {
    void invokeData();
    //
    boolean CheckOrderListOfTable(int tableId);

    void checkTableStatus();

    interface View {
        void onInvokeDataSuccess();

        void onInvokeDataFail();

        void onInvokeDataPending();

        void onStartProcessBar(String message);

        void onStopProcessBar();

        void initAdapter(Context context, List<TableModel> listData);

        void initGridView();
    }
}
