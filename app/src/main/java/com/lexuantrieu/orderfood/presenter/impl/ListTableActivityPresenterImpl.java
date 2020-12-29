package com.lexuantrieu.orderfood.presenter.impl;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.lexuantrieu.orderfood.R;
import com.lexuantrieu.orderfood.model.TableModel;
import com.lexuantrieu.orderfood.network.RestClient;
import com.lexuantrieu.orderfood.presenter.ListTableActivityPresenter;
import com.lexuantrieu.orderfood.service.GetListTableService;
import com.lexuantrieu.orderfood.utils.Utils;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ListTableActivityPresenterImpl implements ListTableActivityPresenter {

    private Context context;
    private ListTableActivityPresenterImpl.View view;

    public ListTableActivityPresenterImpl(Context context, ListTableActivityPresenterImpl.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void invokeData() {
        view.onInvokeDataPending();
        //Lay token
        String token = Utils.GetTokenLocal(context);
        if(token.isEmpty()) {
            view.onInvokeDataFail();
            Log.e("LXT_Log", "Token null");
            return;
        }
        GetListTableService service = RestClient.createService(GetListTableService.class);
        service.getListTable("Bearer " + token).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(data->{
                    List<TableModel> list = data.getData();
                    for(TableModel m: list){
                        switch (m.getStatus()) {
                            case 1:
                                m.setImage(R.drawable.tablebusy);
                                break;
                            case 2:
                                m.setImage(R.drawable.tablewait);
                                break;
                            case 3:
                                m.setImage(R.drawable.tableeating);
                                break;
                            default:
                                m.setImage(R.drawable.tablefree);
                                break;
                        }
                    }
                    return true;
                })
                .subscribe(response->{
//                    Log.e("LXT_Log", new Gson().toJson(response));
                    if (response.getError().equals("null")) {
                        view.initAdapter(context, response.getData());
                        view.initGridView();
                        view.onInvokeDataSuccess();
                    } else {
                        view.onInvokeDataFail();
                        Log.e("LXT_Log", "ErrorCode: " + response.getError());
                        Toast.makeText(context, "ErrorCode: " + response.getError(), Toast.LENGTH_SHORT).show();
                    }
                },throwable -> {
                    view.onInvokeDataFail();
                    throwable.printStackTrace();
                });
        //-end
    }

    // Kiểm tra có món đã đặt chưa
    @Override
    public boolean CheckOrderListOfTable(int tableId) {

        String token = Utils.GetTokenLocal(context);
        if(token.isEmpty()) {
            view.onInvokeDataFail();
            Log.e("LXT_Log", "Token null");
            return false;
        }
        GetListTableService service = RestClient.createService(GetListTableService.class);
        service.getListTable("Bearer "+ token).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(data->{
                    List<TableModel> list = data.getData();
                    for(TableModel m: list){
                        switch (m.getStatus()) {
                            case 1:
                                m.setImage(R.drawable.tablebusy);
                                break;
                            case 2:
                                m.setImage(R.drawable.tablewait);
                                break;
                            case 3:
                                m.setImage(R.drawable.tableeating);
                                break;
                            default:
                                m.setImage(R.drawable.tablefree);
                                break;
                        }
                    }
                    return true;
                })
                .subscribe(response->{
//                    Log.e("LXT_Log", new Gson().toJson(response));
                    if (response.getError().equals("null")) {
                        view.initAdapter(context, response.getData());
                        view.initGridView();
                        view.onInvokeDataSuccess();
                    } else {
                        view.onInvokeDataFail();
                        Log.e("LXT_Log", "ErrorCode: " + response.getError());
                        Toast.makeText(context, "ErrorCode: " + response.getError(), Toast.LENGTH_SHORT).show();
                    }
                },throwable -> {
                    view.onInvokeDataFail();
                    throwable.printStackTrace();
                });
//    }
        return false;
    }

}
