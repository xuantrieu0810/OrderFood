package com.lexuantrieu.orderfood.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.lexuantrieu.orderfood.R;
import com.lexuantrieu.orderfood.model.FoodModel;
import com.lexuantrieu.orderfood.presenter.ListFoodCustomPresenter;
import com.lexuantrieu.orderfood.presenter.impl.ListFoodCustomPresenterImpl;
import com.lexuantrieu.orderfood.ui.adapter.ListOrderedAdapter;
import com.lexuantrieu.orderfood.ui.adapter.listener.FoodAdapterListener;
import com.lexuantrieu.orderfood.ui.dialog.AlertDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class ListOrderedActivity extends AppCompatActivity implements ListFoodCustomPresenter.View, FoodAdapterListener, SwipeRefreshLayout.OnRefreshListener {

    ListFoodCustomPresenter presenter;
    ProgressDialog progressDialog;
    SwipeRefreshLayout refreshLayout;
    String tableName;
    int tableID = -1, billID = -1;
    TextView txtTotal;
    ListView listViewCart;
    ArrayList<FoodModel> arrayCart;
    ListOrderedAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //-----------------------------------------------------------
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            tableName = bundle.getString("tableName");
            tableID = bundle.getInt("tableId");
            billID = bundle.getInt("billId");
            if (tableID == -1 || billID == -1) {

                Log.d("LXT_Log", "table_id: " + tableID + "- bill_id: " + billID);
                finish();
                Toast.makeText(this, "Xảy ra lỗi.", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            finish();
            Toast.makeText(this, "Xảy ra lỗi.", Toast.LENGTH_SHORT).show();
            return;
        }
        setContentView(R.layout.activity_list_ordered);
        //-----------------------------------------------------------
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Danh sách món đã đặt");
        actionBar.setSubtitle(tableName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Back
        //----------------------------------------------------------
//        init();
        presenter = new ListFoodCustomPresenterImpl(this, this);
        refreshLayout = findViewById(R.id.swipe_rf_list_ordered);
        progressDialog = new ProgressDialog(this);
        listViewCart = findViewById(R.id.listViewCart);
        txtTotal = findViewById(R.id.txtTotalCart);
        arrayCart = new ArrayList<>();
        adapter = new ListOrderedAdapter(this, R.layout.item_list_ordered, arrayCart, this);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        //---
        onStartProcessBar("Đang load...");
        presenter.invokeDataOrderList(tableID, billID);

//        GetListInfoBill();
    }//end of onCreate

    @Override
    public void onInvokeDataSuccess() {
        onStopProcessBar();
    }

    @Override
    public void onInvokeDataFail() {
        onStopProcessBar();
        AlertDialogFragment dialogFragment = new AlertDialogFragment(this, "Lỗi tải dữ liệu", "Tải lại", resultOk -> {
            if (resultOk == Activity.RESULT_OK) {
                presenter.invokeDataOrderList(tableID, billID);
            } else {
                finish();
                onBackPressed();
            }
        });
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        dialogFragment.show(fragmentManager, "DialogListFood");
    }

    @Override
    public void onStartProcessBar(String message) {
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void onStopProcessBar() {
        if (refreshLayout.isRefreshing()) refreshLayout.setRefreshing(false);
        if (progressDialog.isShowing()) progressDialog.dismiss();
    }

    @Override
    public void initAdapter(Context context, List<FoodModel> listData) {
        arrayCart = (ArrayList<FoodModel>) listData;
        adapter = new ListOrderedAdapter(this, R.layout.item_list_ordered, arrayCart, this);
    }

    @Override
    public void initRecyclerView() {
        listViewCart.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onFailSetFood() {
        onStopProcessBar();
        Toast.makeText(this, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessSetFood(FoodModel foodModel, int pos) {
        if (foodModel.getQuantity() == 0)
            arrayCart.remove(pos);
        else
            arrayCart.set(pos, foodModel);
        adapter.notifyDataSetChanged();
        onStopProcessBar();
    }

    //------------------------------------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //-------------------------
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.invokeDataOrderList(tableID, billID);
        onStopProcessBar();
    }

    @Override
    public void onRefresh() {
        presenter.invokeDataOrderList(tableID, billID);
        onStopProcessBar();
    }

    @Override
    public void ChangeFoodItem(int position, FoodModel foodModel) {
        int stt = foodModel.getStt();
        int quantity = foodModel.getQuantity();
        int status = foodModel.getStatus();
        if (stt != -1 && status == 0) {
            if (quantity == 0) {
                AlertDialogFragment dialogFragment = new AlertDialogFragment(this, "Hủy chọn món đã đặt", "[ " + foodModel.getFoodName() + "]" +
                        "\nBạn có chắc không?", resultOk -> {
                    if (resultOk == Activity.RESULT_OK) {
                        onStartProcessBar("Đang xóa...");
                        presenter.UpdateOrderList(billID, tableID, position, foodModel);
                    } else {
                        getSupportFragmentManager().popBackStack();
                    }
                });
                dialogFragment.show(getSupportFragmentManager(), "Dialog");
            } else {
                presenter.UpdateOrderList(billID, tableID, position, foodModel);
            }
        } else {
            presenter.InsertOrderList(billID, tableID, position, foodModel);
        }
    }
}
