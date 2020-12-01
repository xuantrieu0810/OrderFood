package com.lexuantrieu.orderfood.presenter;

import com.lexuantrieu.orderfood.model.room.User;

public interface MainActivityPresenter {
    void invokeData();
    void onLogout();
    interface View{
        void onLogoutPending();
        void onLogoutSuccess();
        void onLogoutFail();
        void onInvokeDataSuccess(User user);
        void onInvokeDataFail();
        void onStartProcessBar(String message);
        void onStopProcessBar();
    }
}
