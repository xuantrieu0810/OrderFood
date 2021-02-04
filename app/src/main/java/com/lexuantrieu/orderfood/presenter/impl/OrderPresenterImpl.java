package com.lexuantrieu.orderfood.presenter.impl;

import android.content.Context;
import android.util.Log;

import com.lexuantrieu.orderfood.network.RestClient;
import com.lexuantrieu.orderfood.presenter.OrderPresenter;
import com.lexuantrieu.orderfood.service.BillService;
import com.lexuantrieu.orderfood.utils.Utils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class OrderPresenterImpl implements OrderPresenter {

    private CompositeDisposable compositeDisposable;
    private Context context;
    private OrderPresenter.View view;
    
    public OrderPresenterImpl(Context context, OrderPresenter.View view) {
        compositeDisposable = new CompositeDisposable();
        this.context = context;
        this.view = view;
    }
    
    
    @Override
    public void getBillId(int table_id) {
        //get token
        String token = Utils.GetTokenLocal(context);
        if(token.isEmpty()) {
            view.getBillIdFail();
            Log.e("LXT_Log", "Token null");
            return;
        }
        //
        BillService service = RestClient.createService(BillService.class);

        compositeDisposable.add(service.GetBillId("Bearer " + token, table_id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response-> {
//                    Log.e("LXT_Log","Response GetBillID: "+ new Gson().toJson(response));
                    int idBill = -1;
                    try {
                        idBill= Integer.parseInt(response.getData());
                    }catch (Exception e) {
                        Log.e("LXT_Log", "Error parseInt: " + e.getMessage());
                    }
                    if (response.getError().equals("null")&& idBill!=-1) {
                        view.getBillIdSuccess(idBill);
                    } else {
                        view.getBillIdFail();
                        Log.e("LXT_Log", "ErrorCode: " + response.getError());
                    }

                },throwable -> {
                    Log.e("LXT_Log_Error","Response GetBillID: "+throwable.toString());
                    throwable.printStackTrace();

                })
        );
    }
}
