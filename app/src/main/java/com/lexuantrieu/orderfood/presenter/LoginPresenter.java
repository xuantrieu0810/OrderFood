package com.lexuantrieu.orderfood.presenter;

public interface LoginPresenter {

    void requestLogin(String username,String password);
    void onCheckToken();
    interface View{
        void onLoginPending();
        void onLoginSuccess();
        void onLoginFail();
        void onStartProcessBar(String message);
        void onStopProcessBar();
    }
}
