package com.lexuantrieu.orderfood.presenter;

public interface LogoutAccountPresenter {
    void onLogout();

    interface View {
        void onLogoutPending();

        void onLogoutSuccess();

        void onLogoutFail();

        void onStartProcessBar(String message);

        void onStopProcessBar();
    }
}
