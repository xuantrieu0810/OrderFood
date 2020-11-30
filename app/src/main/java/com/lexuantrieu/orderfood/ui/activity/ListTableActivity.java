package com.lexuantrieu.orderfood.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.lexuantrieu.orderfood.R;
import com.lexuantrieu.orderfood.model.room.TableModel;
import com.lexuantrieu.orderfood.ui.adapter.TableAdapter;

import java.util.ArrayList;

public class ListTableActivity extends AppCompatActivity {

    public static int idTableClick = -1;
    public static String nameTableClick;
    public static int idBill = -1;
    GridView gvTable;
    ArrayList<TableModel> arrayTable;
    TableAdapter tableAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_table);
        //-----------------------------------------------
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Bàn ăn");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Back
        //----------------------------------------------------------
        //------------------------------------------------------------------------------------------
        gvTable = (GridView) findViewById(R.id.gridviewTable);
        arrayTable = new ArrayList<>();
        tableAdapter = new TableAdapter(this, R.layout.item_table_cell, arrayTable);
        gvTable.setAdapter(tableAdapter);
        GetListTable();//Set Database into GridView
        tableAdapter.notifyDataSetChanged();
        //------------------------------------------------------------------------------------------
        //Event click on GridView
        gvTable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListTableActivity.this, OrderActivity.class);
                idTableClick = arrayTable.get(position).getIdTable();
                nameTableClick = arrayTable.get(position).getNameTable();
                startActivity(intent);
            }
        });
        registerForContextMenu(gvTable);
    }//end onCreate
    //----------------------------------------------------------------------------------------------
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
        tableAdapter.notifyDataSetChanged();
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_popup_in_gridview, menu);
        // Get the list item position
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        int position = info.position;
        idTableClick = arrayTable.get(position).getIdTable();
        nameTableClick = arrayTable.get(position).getNameTable();
        menu.setHeaderTitle(arrayTable.get(position).getNameTable());
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        Intent intent;
        Cursor cursor = MainActivity.database.GetData("SELECT idBill FROM Bill WHERE idTable = " + idTableClick+" AND statusBill = 0");
        if(!cursor.moveToFirst()) {
            Toast.makeText(this, "Vui lòng chọn bàn và đặt món", Toast.LENGTH_SHORT).show();
            return super.onContextItemSelected(item);
        }
        else {
            idBill = cursor.getInt(0);
            cursor.close();
            int check =0;
            cursor = MainActivity.database.GetData("SELECT * FROM OrderedDetails WHERE idBill = " + idBill + " AND status > 0");
            if (cursor.moveToFirst()) {
                check = 1;
                cursor.close();
            }

            switch (item.getItemId()) {
                case R.id.menu_ordered:
                    if(check != 0) {
                        intent = new Intent(this, ListOrderedActivity.class);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(this, "Vui lòng đặt món", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.menu_pay:
                    if(check != 0 ){
                        intent = new Intent(this, BillActivity.class);
                        startActivity(intent);
                    } else
                        Toast.makeText(this, "Bạn chưa có đơn hàng nào", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.menu_delete:
                    if(check != 0 ){
                        Toast.makeText(this, "KHÔNG THỂ XÓA", Toast.LENGTH_SHORT).show();
                    } else {
                        MainActivity.database.QueryData("DELETE FROM OrderedDetails WHERE idBill = " + idBill);
                        MainActivity.database.QueryData("DELETE FROM Bill WHERE idBill = " + idBill);
                        MainActivity.database.QueryData("UPDATE TableFood SET Status = 0 WHERE idTable = " + idTableClick);
                        Toast.makeText(this, "ĐÃ XÓA", Toast.LENGTH_SHORT).show();
                        GetListTable();
                    }
                    break;


            }
        }
        return super.onContextItemSelected(item);
    }

    //Create menu on MainActivity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_in_gridtable, menu);
        return super.onCreateOptionsMenu(menu);
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuRefreshTable:
                CheckStatusTable();
                Toast.makeText(this, "Refresh", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void CheckStatusTable() {
        MainActivity.database.QueryData("UPDATE TableFood SET Status = 0");

        MainActivity.database.QueryData("UPDATE TableFood SET Status = 3 WHERE Status = 0 AND idTable IN (SELECT idTable FROM Bill WHERE statusBill = 0)");
        Cursor cursor = MainActivity.database.GetData("SELECT idTable, idBill FROM Bill WHERE statusBill = 0");
        while (cursor.moveToNext()) {

            MainActivity.database.QueryData("UPDATE TableFood SET Status = 2 " +
                    "WHERE idTable = "+cursor.getInt(0)+" AND  EXISTS (SELECT * FROM OrderedDetails  " +
                    "WHERE idBill = "+cursor.getInt(1)+" AND Status > 0 AND Status < 4)");
            MainActivity.database.QueryData("UPDATE TableFood SET Status = 1 " +
                    "WHERE idTable = "+cursor.getInt(0)+" AND NOT EXISTS (SELECT * FROM OrderedDetails  " +
                    "WHERE idBill = "+cursor.getInt(1)+" AND Status > 0)");
        }
        GetListTable();
    }
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onResume() {
        super.onResume();
        GetListTable();
    }
}