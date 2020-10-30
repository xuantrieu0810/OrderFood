package com.lexuantrieu.orderfood.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lexuantrieu.orderfood.R;
import com.lexuantrieu.orderfood.model.Food;
import com.lexuantrieu.orderfood.network.Server;
import com.lexuantrieu.orderfood.service.APIUtils;
import com.lexuantrieu.orderfood.service.DataClient;
import com.lexuantrieu.orderfood.ui.adapter.FoodAdapter;
import com.lexuantrieu.orderfood.utils.CheckConnection;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;

public class ListFoodCustom extends AppCompatActivity {

    int tableID = -1; //default = -1
    boolean ckGetFoodData = false;
    RecyclerView rvFood;
    ArrayList<Food> arrayFood;
    FoodAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_food_custom);
        rvFood = findViewById(R.id.recyclerViewSearch);
        arrayFood = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvFood.setLayoutManager(layoutManager);
        rvFood.setItemAnimator(null);
        rvFood.setItemAnimator(null);
        rvFood.addItemDecoration(new DividerItemDecoration(rvFood.getContext(), DividerItemDecoration.VERTICAL));
        if(CheckConnection.isNetworkAvailable(getApplicationContext())) {
            GetDataWebSV(9);
        } else {
            Toast.makeText(this, "Vui lòng kết nối internet!!!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void SetAdapter() {
        adapter = new FoodAdapter(this, arrayFood);
        rvFood.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_food, menu);
        MenuItem item = menu.findItem(R.id.actionSearch);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

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

    private void GetDataWebSV(int tableID) {
        DataClient dataClient = APIUtils.getData();
        Call<List<Food>> callback = dataClient.GetFoodByTable(tableID);
        callback.enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, retrofit2.Response<List<Food>> response) {
                Log.i("LXT_Log", "onResponse GetFoodByTable: "+response.body());
                if(response.body() != null) {
                    arrayFood = (ArrayList<Food>) response.body();
                    for(Food f: arrayFood){
                        f.setNameFoodNonVN(covertToString(f.getNameFood()));
                    }
                    Log.i("LXT_Log", "arrayFood: "+arrayFood.size());
                    SetAdapter();
                } else {
                    Toast.makeText(ListFoodCustom.this, "Xảy ra lỗi.", Toast.LENGTH_SHORT).show();
//                    ShowDialogConfirm();
                }
//                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {

            }
        });
    }

    public static String covertToString(String value) {
        try {
            String temp = Normalizer.normalize(value, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(temp).replaceAll("");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void SetCountFood(int position, int quantity, Food food) {
        int idFood = food.getIdFood();
        int stt = food.getStt();
        if (stt != -1 && food.getStatusFood() == 0) {
            UpdateOrderList(stt, idFood, quantity);
        } else {
            InsertOrderList(idFood, quantity);
        }
//        GetDataWebSV(urlGetData);
//        adapter.notifyDataSetChanged();
    }

    private void InsertOrderList(final int idFood, final int quantity) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlInsertOL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("LXT_LOG", "response:" + response.trim());
                        if (response.trim().equals("success")) {
                            Toast.makeText(ListFoodCustom.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ListFoodCustom.this, "Lỗi không thêm được", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("LXT_LOG", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                Log.d("LXT_LOG", "food: " + idFood + " quality:" + quantity);
                params.put("tableid", tableID + "");
                params.put("foodid", idFood + "");
                params.put("quantity", quantity + "");
                Log.d("LXT_LOG", params.toString());
                return params;
            }
        };
        Log.d("LXT_LOG", "stringRequest: " + stringRequest.toString());
        requestQueue.add(stringRequest);
    }

    private void UpdateOrderList(int stt, int idFood, int quantity) {

    }
}