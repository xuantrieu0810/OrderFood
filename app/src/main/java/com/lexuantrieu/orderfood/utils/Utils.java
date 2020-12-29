package com.lexuantrieu.orderfood.utils;

public class Utils {
    public static final String TAG = "LXT_Log";

    /*public static String GetTokenCurrent(Context context) {
        final String[] tokenResult = new String[1];
        tokenResult[0] = "hello";
        AppDatabase db = AppDatabase.getInstance(context);
        db.getUserDao().getListUser().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    User user = (User) response.get(0);
                    if(user != null){
                        tokenResult[0] = user.getToken();
                    } else {
                        tokenResult[0] = null;
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                });

        return tokenResult[0];
    }*/
}
