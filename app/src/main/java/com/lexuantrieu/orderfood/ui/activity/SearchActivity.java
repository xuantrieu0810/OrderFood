package com.lexuantrieu.orderfood.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.lexuantrieu.orderfood.R;
import com.lexuantrieu.orderfood.model.FoodModel;
import com.lexuantrieu.orderfood.presenter.ListFoodCustomPresenter;
import com.lexuantrieu.orderfood.presenter.impl.ListFoodCustomPresenterImpl;
import com.lexuantrieu.orderfood.ui.adapter.FoodSearchAdapter;
import com.lexuantrieu.orderfood.ui.adapter.listener.FoodAdapterListener;
import com.lexuantrieu.orderfood.ui.dialog.AlertDialogFragment;
import com.lexuantrieu.orderfood.utils.CheckConnection;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements ListFoodCustomPresenter.View, FoodAdapterListener, SwipeRefreshLayout.OnRefreshListener{

    String keySearch;
    ListFoodCustomPresenter presenter;
    ProgressDialog progressDialog;
    int tableID = -1; //default = -1
    int billID = -1; //default = -1
    String tableName;
    RecyclerView recyclerView;
    ArrayList<FoodModel> arrayFoodModel;
    FoodSearchAdapter adapter;
    SwipeRefreshLayout refreshLayout;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CheckConnection.isNetworkAvailable(getApplicationContext())) {
            AlertDialogFragment dialogFragment = new AlertDialogFragment(this, "Không có internet", "Kết nối lại", resultOk -> {
                if (resultOk == Activity.RESULT_OK) {
                    onRestart();
                } else {
                    finish();
                }
            });
            FragmentManager fragmentManager = this.getSupportFragmentManager();
            dialogFragment.show(fragmentManager, "Dialog");
        }
        setContentView(R.layout.activity_list_food_custom);
        //-----------------------------------------------------------
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            tableName = bundle.getString("tableName");
            tableID = bundle.getInt("tableId");
            billID = bundle.getInt("billId");
        } else {
            finish();
            Toast.makeText(this, "Xảy ra lỗi.", Toast.LENGTH_SHORT).show();
            return;
        }
        //-----------------------------------------------------------
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Thực đơn");
        actionBar.setSubtitle(tableName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Back
//        //-----------------------------------------------------------
        init();
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        onStartProcessBar("Đang load...");
        presenter.invokeData(tableID,0);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(null);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

    }

    private void init() {
        presenter = new ListFoodCustomPresenterImpl(this, this);
        recyclerView = findViewById(R.id.recyclerViewSearch);
        refreshLayout = findViewById(R.id.swipe_rf_search);
        progressDialog = new ProgressDialog(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_food, menu);
        MenuItem item = menu.findItem(R.id.actionSearch);
        searchView = (SearchView) item.getActionView();
        searchView.setIconifiedByDefault(true);
        searchView.setIconified(true);
        searchView.setFocusable(false);
        searchView.requestFocusFromTouch();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                keySearch = newText;
                adapter.getFilter().filter(keySearch);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void ChangeFoodQuantity(int position, FoodModel foodModel) {
        int stt = foodModel.getStt();
        if (stt != -1 && foodModel.getStatusFood() == 0) {
            presenter.UpdateOrderList(billID, tableID, position, foodModel);
        } else {
            presenter.InsertOrderList(billID, tableID, position, foodModel);
        }
    }

    @Override
    public void onInvokeDataSuccess() {
        if(refreshLayout.isRefreshing()) {
            adapter.getFilter().filter(keySearch);



            refreshLayout.setRefreshing(false);
        }
        onStopProcessBar();
    }

    @Override
    public void onInvokeDataFail() {
        onStopProcessBar();
        AlertDialogFragment dialogFragment = new AlertDialogFragment(this, "Lỗi tải dữ liệu", "Tải lại", resultOk -> {
            if (resultOk == Activity.RESULT_OK) {
                presenter.invokeData(tableID,0);
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
        if(progressDialog.isShowing()) progressDialog.dismiss();
    }


    @Override
    public void initAdapter(Context context, List<FoodModel> listData) {
        arrayFoodModel = (ArrayList<FoodModel>) listData;
        adapter = new FoodSearchAdapter(this, arrayFoodModel, this);
    }

    @Override
    public void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onFailSetFood() {
        Toast.makeText(this, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
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
                onBackPressed();
                return true;
            case R.id.itemCart:
                //check sl orderlist
                Intent intent = new Intent(SearchActivity.this, ListOrderedActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();
        onStopProcessBar();
        presenter.invokeData(tableID,0);
    }

    @Override
    public void onRefresh() {
        presenter.invokeData(tableID,0);
    }
    //------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------
}