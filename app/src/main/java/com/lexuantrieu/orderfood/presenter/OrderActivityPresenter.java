package com.lexuantrieu.orderfood.presenter;

public interface OrderActivityPresenter {
    void getBillId(int table_id);

    //    void invokeData();
    interface View {
        //        void onInvokeDataSuccess();
//        void onInvokeDataFail();
        void getBillIdSuccess(int bill_id);

        void getBillIdFail();

        void onStartProcessBar(String message);

        void onStopProcessBar();

        void initAdapter();


    }
}
