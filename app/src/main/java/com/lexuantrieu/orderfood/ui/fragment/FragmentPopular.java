package com.lexuantrieu.orderfood.ui.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.lexuantrieu.orderfood.R;
import com.lexuantrieu.orderfood.model.FoodModel;
import com.lexuantrieu.orderfood.presenter.ListFoodCustomPresenter;
import com.lexuantrieu.orderfood.presenter.impl.ListFoodCustomPresenterImpl;
import com.lexuantrieu.orderfood.ui.activity.ListOrderedActivity;
import com.lexuantrieu.orderfood.ui.activity.OrderActivity;
import com.lexuantrieu.orderfood.ui.adapter.FoodAdapter;
import com.lexuantrieu.orderfood.ui.adapter.listener.FoodAdapterListener;
import com.lexuantrieu.orderfood.ui.dialog.AlertDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class FragmentPopular extends Fragment implements ListFoodCustomPresenter.View, FoodAdapterListener, SwipeRefreshLayout.OnRefreshListener{

    Context mContext;
    ListFoodCustomPresenter presenter;
    ProgressDialog progressDialog;
    int tableID = -1; //default = -1
    int billID = -1; //default = -1
    String tableName;
    RecyclerView recyclerView;
    ArrayList<FoodModel> arrayFoodModel;
    FoodAdapter adapter;
    SwipeRefreshLayout refreshLayout;
    View viewFrag;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        billID = OrderActivity.billID;
        tableID = OrderActivity.tableID;
        tableName = OrderActivity.tableName;
    }
    public FragmentPopular() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = container.getContext();
        viewFrag = inflater.inflate(R.layout.fragment_all_food,container,false);
        init();
        presenter = new ListFoodCustomPresenterImpl(this.getContext(), this);
        progressDialog = new ProgressDialog(mContext);
        arrayFoodModel = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(null);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        onStartProcessBar("Đang load...");
        presenter.invokeData(tableID,0);
        return viewFrag;
    }

    private void init() {
        recyclerView = viewFrag.findViewById(R.id.recyclerViewFood_FragAllFood);
        refreshLayout = viewFrag.findViewById(R.id.swipe_rf_frag_allfood);
    }

    //----------------------------------------------------------------------------------------------
    @Override
    public void ChangeFoodQuantity(int position, FoodModel foodModel) {
        int stt = foodModel.getStt();
        if (stt != -1 && foodModel.getStatusFood() == 0) {
            presenter.UpdateOrderList(billID, tableID, position, foodModel);
        } else {
            presenter.InsertOrderList(billID, tableID, position, foodModel);
        }
    }
    //----------------------------------------------------------------------------------------------

    @Override
    public void onInvokeDataSuccess() {
        if(refreshLayout.isRefreshing()) refreshLayout.setRefreshing(false);
        onStopProcessBar();
    }

    @Override
    public void onInvokeDataFail() {
        onStopProcessBar();
        AlertDialogFragment dialogFragment = new AlertDialogFragment(mContext, "Lỗi tải dữ liệu", "Tải lại", resultOk -> {
            if (resultOk == Activity.RESULT_OK) {
                presenter.invokeData(tableID,0);
            } else {
                getActivity().getFragmentManager().popBackStack();
            }
        });
        dialogFragment.show(getFragmentManager(), "Dialog");
    }

    @Override
    public void onStartProcessBar(String message) {
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void onStopProcessBar() {
        if(progressDialog.isShowing()) progressDialog.dismiss();
    }


    @Override
    public void initAdapter(Context context, List<FoodModel> listData) {
        arrayFoodModel = (ArrayList<FoodModel>) listData;
        adapter = new FoodAdapter(mContext, arrayFoodModel, (FoodAdapterListener) this);
    }

    @Override
    public void initRecyclerView() {
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onFailSetFood() {
        Toast.makeText(mContext, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessSetFood(FoodModel foodModel, int pos) {
        arrayFoodModel.set(pos, foodModel);
        adapter.notifyItemChanged(pos);
    }

    //------------------------------------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //-------------------------
            case android.R.id.home:
                getActivity().getFragmentManager().popBackStack();
                return true;
            case R.id.itemCart:
                //check sl orderlist
                Intent intent = new Intent(mContext, ListOrderedActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.invokeData(tableID,0);
    }

    @Override
    public void onRefresh() {
        presenter.invokeData(tableID,0);
    }
}