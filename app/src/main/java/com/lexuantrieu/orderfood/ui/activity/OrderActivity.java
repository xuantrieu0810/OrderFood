package com.lexuantrieu.orderfood.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.lexuantrieu.orderfood.R;
import com.lexuantrieu.orderfood.presenter.OrderPresenter;
import com.lexuantrieu.orderfood.presenter.impl.OrderPresenterImpl;
import com.lexuantrieu.orderfood.ui.adapter.ViewPagerAdapter;

public class OrderActivity extends AppCompatActivity implements OrderPresenter.View{

    public static int billID = -1, tableID = -1;
    public static String tableName;
    public static int idCategory;
    public static String nameCategory;
    private TabLayout tablayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    OrderPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        setContentView(R.layout.activity_order);
        //-----------------------------------------------------------
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Thực đơn");
        actionBar.setSubtitle(tableName);
        //actionBar.hide(); //Ẩn ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Back
        //-----------------------------------------------------------
        presenter = new OrderPresenterImpl(this,this);
        presenter.getBillId(tableID);
        init();
    }

    private void init() {
        tablayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        adapter = new ViewPagerAdapter(this,getSupportFragmentManager());
        //add frag
        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);

        tablayout.getTabAt(0).setIcon(R.drawable.ic_popular);
        tablayout.getTabAt(1).setIcon(R.drawable.ic_category);
        tablayout.getTabAt(2).setIcon(R.drawable.ic_all_food);
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
        switch (item.getItemId()) {
            case R.id.menuItemCart:
//                String sql = "SELECT * FROM OrderedDetails WHERE idBill = " + idBill ;
//                Cursor cursor = MainActivity.database.GetData(sql);
//                if(cursor.moveToFirst()) {
                Intent intent = new Intent(OrderActivity.this, ListOrderedActivity.class);
                startActivity(intent);
//                }
//                else
//                    Toast.makeText(this, "VUI CHỌN MÓN", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menuItemSearch:
                intent = new Intent(OrderActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
            //-------------------------
            case android.R.id.home:
                onBackPressed();
                return true;
            default: break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onInvokeDataSuccess() {

    }

    @Override
    public void onInvokeDataFail() {

    }

    @Override
    public void getBillIdSuccess(int bill_id) {
        this.billID = bill_id;
//        presenter.invokeData();
        initAdapter();
    }

    @Override
    public void getBillIdFail() {
        Toast.makeText(this, "Xảy ra lỗi. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
        this.finish();
    }

    @Override
    public void onInvokeDataPending() {

    }

    @Override
    public void onStartProcessBar(String message) {

    }

    @Override
    public void onStopProcessBar() {

    }

    @Override
    public void initAdapter() {
        viewPager.setAdapter(adapter);

    }
}