package com.lexuantrieu.orderfood.presenter;

public interface OrderPresenter {
    void getBillId(int table_id);
    void invokeData();
    interface View{
        void onInvokeDataSuccess();
        void onInvokeDataFail();
        void getBillIdSuccess(int bill_id);
        void getBillIdFail();
        void onInvokeDataPending();
        void onStartProcessBar(String message);

        void onStopProcessBar();

        void initAdapter();


    }
}
