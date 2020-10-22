package com.lexuantrieu.orderfood.Service;

import com.lexuantrieu.orderfood.utils.Server;

public class APIUtils {
    public static final String Base_Url = Server.localhost;

    //method gui/nhan du lieu thong qua interface DataClient
    public static DataClient getData() {
        return RetrofitClient.getClient(Base_Url).create(DataClient.class);
    }
}
