package com.lexuantrieu.orderfood.presenter.impl;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.lexuantrieu.orderfood.network.RestClient;
import com.lexuantrieu.orderfood.presenter.OrderPresenter;
import com.lexuantrieu.orderfood.service.BillService;
import com.lexuantrieu.orderfood.utils.Utils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class OrderPresenterImpl implements OrderPresenter {
    
    private Context context;
    private OrderPresenter.View view;
    
    public OrderPresenterImpl(Context context, OrderPresenter.View view) {
        this.context = context;
        this.view = view;
    }
    
    
    @Override
    public void getBillId(int table_id) {
        //get token
        String token = Utils.GetTokenLocal(context);
        if(token.isEmpty()) {
            view.onInvokeDataFail();
            Log.e("LXT_Log", "Token null");
            return;
        }
        //
        BillService service = RestClient.createService(BillService.class);

        service.GetBillId("Bearer " + token, table_id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response-> {
                    Log.e("LXT_Log", new Gson().toJson(response));
                    if (response.getError().equals("null")) {
                        view.getBillIdSuccess(Integer.parseInt(response.getData()));
                    } else {
                        view.getBillIdFail();
                        Log.e("LXT_Log", "ErrorCode: " + response.getError());
                    }

                },throwable -> {
                    view.onInvokeDataFail();
                    throwable.printStackTrace();

                });
    }

    @Override
    public void invokeData() {

    }
}
