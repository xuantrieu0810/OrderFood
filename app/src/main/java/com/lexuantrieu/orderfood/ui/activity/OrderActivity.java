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
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.lexuantrieu.orderfood.R;
import com.lexuantrieu.orderfood.presenter.OrderActivityPresenter;
import com.lexuantrieu.orderfood.presenter.impl.OrderActivityPresenterImpl;
import com.lexuantrieu.orderfood.ui.adapter.ViewPagerAdapter;
import com.lexuantrieu.orderfood.ui.dialog.AlertDialogFragment;

public class OrderActivity extends AppCompatActivity implements OrderActivityPresenter.View {

    public static int billID = -1, tableID = -1;
    public static String tableName;
    public static int idCategory;
    public static String nameCategory;
    ProgressDialog progressDialog;
    Context context;
    OrderActivityPresenter presenter;
    private TabLayout tablayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //-----------------------------------------------------------
        this.context = this;
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
        setContentView(R.layout.activity_order);
        //-----------------------------------------------------------
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Thực đơn");
        actionBar.setSubtitle(tableName);
        //actionBar.hide(); //Ẩn ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Back
        //-----------------------------------------------------------
        presenter = new OrderActivityPresenterImpl(this, this);
        presenter.getBillId(tableID);
        init();
    }

    private void init() {
        tablayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        progressDialog = new ProgressDialog(context);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        //add frag
        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);

//        tablayout.getTabAt(0).setIcon(R.drawable.ic_popular);
//        tablayout.getTabAt(1).setIcon(R.drawable.ic_category);
//        tablayout.getTabAt(2).setIcon(R.drawable.ic_all_food);
    }

    //Create menu on Activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_in_order, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        Bundle bundle;
        switch (item.getItemId()) {
            case R.id.menuItemCart:
                intent = new Intent(OrderActivity.this, ListOrderedActivity.class);
                bundle = new Bundle();
                bundle.putInt("tableId", tableID);
                bundle.putString("tableName", tableName);
                bundle.putInt("billId", billID);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.menuItemSearch:
                intent = new Intent(OrderActivity.this, SearchActivity.class);
                bundle = new Bundle();
                bundle.putInt("tableId", tableID);
                bundle.putString("tableName", tableName);
                bundle.putInt("billId", billID);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
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
    public void getBillIdSuccess(int bill_id) {
        onStopProcessBar();
        this.billID = bill_id;
        initAdapter();
    }

    @Override
    public void getBillIdFail() {
        onStopProcessBar();
        AlertDialogFragment dialogFragment = new AlertDialogFragment(context, "Chưa có dữ liệu Bill", "Tải lại", resultOk -> {
            if (resultOk == Activity.RESULT_OK) {
                presenter.getBillId(tableID);
            } else {
                this.getSupportFragmentManager().popBackStack();
                this.finish();
            }
        });
        dialogFragment.show(this.getSupportFragmentManager(), "Dialog");
    }

    @Override
    public void onStartProcessBar(String message) {
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void onStopProcessBar() {
        if (progressDialog.isShowing()) progressDialog.dismiss();
    }

    @Override
    public void initAdapter() {
        viewPager.setAdapter(adapter);
    }
}