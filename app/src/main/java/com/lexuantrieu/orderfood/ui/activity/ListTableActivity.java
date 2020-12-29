package com.lexuantrieu.orderfood.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.lexuantrieu.orderfood.R;
import com.lexuantrieu.orderfood.model.TableModel;
import com.lexuantrieu.orderfood.presenter.ListTableActivityPresenter;
import com.lexuantrieu.orderfood.presenter.impl.ListTableActivityPresenterImpl;
import com.lexuantrieu.orderfood.ui.adapter.TableAdapter;
import com.lexuantrieu.orderfood.ui.dialog.AlertDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class ListTableActivity extends AppCompatActivity implements ListTableActivityPresenter.View {

    ListTableActivityPresenter presenter;
    ProgressDialog progressDialog;
    GridView gridView;
    ArrayList<TableModel> arrayTable;
    TableAdapter adapter;
//
    int idTableSelected;
    String nameTableSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_table);
        //---------------------------------------------------------------
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Bàn ăn");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Back
        //---------------------------------------------------------------
        init();
        presenter.invokeData();
        //---------------------------------------------------------------
        //Event click on GridView
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(ListTableActivity.this, ListFoodCustom.class);
            Bundle bundle = new Bundle();
            bundle.putInt("tableId",arrayTable.get(position).getId());
            bundle.putString("tableName",arrayTable.get(position).getName());
            intent.putExtras(bundle);
            startActivity(intent);
        });
        registerForContextMenu(gridView);
    }//end onCreate
    private void init() {
        presenter = new ListTableActivityPresenterImpl(this, this);
        progressDialog = new ProgressDialog(this);
        gridView = findViewById(R.id.gridviewTable);
    }
    //----------------------------------------------------------------------------------------------
    /*
    private void GetListTable() {
        Cursor cursor = MainActivity.database.GetData("SELECT * FROM TableFood");
        arrayTable.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);//idTable
            String nameTable = cursor.getString(1);//nameTable
            int status = cursor.getInt(2);//statusTable
            switch (status) {
                case 0:
                    arrayTable.add(new Table(id, nameTable, R.drawable.tablefree, status));
                    break;
                case 1:
                    arrayTable.add(new Table(id, nameTable, R.drawable.tablebusy, status));
                    break;
                case 2:
                    arrayTable.add(new Table(id, nameTable, R.drawable.tablewait, status));
                    break;
                case 3:
                    arrayTable.add(new Table(id, nameTable, R.drawable.tableeating, status));
                    break;
            }
        }
        adapter.notifyDataSetChanged();
    }
    */
    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_popup_list_table, menu);
        // Get the list item position
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        int position = info.position;

        menu.setHeaderTitle(arrayTable.get(position).getName());
    }

//    @Override
//    public boolean onContextItemSelected(@NonNull MenuItem item) {
//        Intent intent;
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//        int position = info.position;
//
////        idTableSelected = arrayTable.get(position).getId();
////        nameTableSelected = arrayTable.get(position).getName();
//        if(!CheckOrderListOfTable(arrayTable.get(position).getId())) {
//            Toast.makeText(this, "Vui lòng chọn bàn và đặt món", Toast.LENGTH_SHORT).show();
//            return super.onContextItemSelected(item);
//        }
//        else {
//            idBill = cursor.getInt(0);
//            cursor.close();
//            int check =0;
//            cursor = MainActivity.database.GetData("SELECT * FROM OrderedDetails WHERE idBill = " + idBill + " AND status > 0");
//            if (cursor.moveToFirst()) {
//                check = 1;
//                cursor.close();
//            }
//
//            switch (item.getItemId()) {
//                case R.id.menu_ordered:
//                    if(check != 0) {
//                        intent = new Intent(this, ListOrderedActivity.class);
//                        startActivity(intent);
//                    }
//                    else
//                        Toast.makeText(this, "Vui lòng đặt món", Toast.LENGTH_SHORT).show();
//                    break;
//                case R.id.menu_pay:
//                    if(check != 0 ){
//                        intent = new Intent(this, BillActivity.class);
//                        startActivity(intent);
//                    } else
//                        Toast.makeText(this, "Bạn chưa có đơn hàng nào", Toast.LENGTH_SHORT).show();
//                    break;
//                case R.id.menu_table_empty:
//                    if(check != 0 ){
//                        Toast.makeText(this, "KHÔNG THỂ XÓA", Toast.LENGTH_SHORT).show();
//                    } else {
//                        MainActivity.database.QueryData("DELETE FROM OrderedDetails WHERE idBill = " + idBill);
//                        MainActivity.database.QueryData("DELETE FROM Bill WHERE idBill = " + idBill);
//                        MainActivity.database.QueryData("UPDATE TableFood SET Status = 0 WHERE idTable = " + idTableClick);
//                        Toast.makeText(this, "ĐÃ XÓA", Toast.LENGTH_SHORT).show();
//                        GetListTable();
//                    }
//                    break;
//
//
//            }
//        }
//        return super.onContextItemSelected(item);
//    }

    private boolean CheckOrderListOfTable(Integer tableId) {


        return false;
    }


//    //Create menu on MainActivity
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_in_gridtable, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------


    @Override
    public void onInvokeDataSuccess() {
        onStopProcessBar();
    }

    @Override
    public void onInvokeDataFail() {
        onStopProcessBar();
        AlertDialogFragment dialogFragment = new AlertDialogFragment(this, "Lỗi tải dữ liệu", "Tải lại", resultOk -> {
            if (resultOk == Activity.RESULT_OK) {
                presenter.invokeData();
            } else {
                finish();
                onBackPressed();
            }
        });
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        dialogFragment.show(fragmentManager, "DialogListTable");
    }

    @Override
    public void onInvokeDataPending() {
        onStartProcessBar("Chờ chút...");
    }

    @Override
    public void onStartProcessBar(String message) {
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void onStopProcessBar() {
        progressDialog.dismiss();
    }


    @Override
    public void initAdapter(Context context, List<TableModel> listData) {
        arrayTable = (ArrayList<TableModel>) listData;
        adapter = new TableAdapter(this, arrayTable);
    }

    @Override
    public void initGridView() {
        gridView.setAdapter(adapter);
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
        onStopProcessBar();
        presenter.invokeData();
    }
    //------------------------------------------------------------------------------------
}