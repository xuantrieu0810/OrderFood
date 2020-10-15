package com.lexuantrieu.orderfood;

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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ListFoodCustom extends AppCompatActivity {

    RecyclerView rvFood;
    ArrayList<Food> arrayFood;
    FoodAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_food_custom);
        rvFood = findViewById(R.id.recyclerViewSearch);
        arrayFood = new ArrayList<>();
        GetDataWebSV(Server.urlGetFood);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvFood.setLayoutManager(layoutManager);
        rvFood.setItemAnimator(null);
        rvFood.setItemAnimator(null);
        rvFood.addItemDecoration(new DividerItemDecoration(rvFood.getContext(), DividerItemDecoration.VERTICAL));
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

    private void GetDataWebSV(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        arrayFood.clear();
                        boolean error = false;
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);
                                arrayFood.add(new Food(
                                        object.getInt("stt"),//stt
                                        object.getInt("id"),//idFood
                                        object.getString("name"),//nameFood
                                        object.getDouble("price"),//priceFood
                                        object.getString("image"),//imageFood
                                        object.getInt("quantity"),//quantity
                                        object.getString("comment"),//comment
                                        object.getInt("status"),//Status
                                        covertToString(object.getString("name"))
                                ));
                            } catch (JSONException e) {
                                error = true;
                                Log.d("LXT_Error", "GetFood: at #" + i);
                                e.printStackTrace();
                            }
                        }
                        if (error) {
                            Toast.makeText(ListFoodCustom.this, "Xảy ra lỗi trong quá trình load.", Toast.LENGTH_SHORT).show();
                        }
                        Log.d("LXT_Log", "arrFood.size: " + arrayFood.size());
                        SetAdapter();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("LXT_Error", error.toString());
                    }
                });
        requestQueue.add(jsonArrayRequest);
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
                params.put("tableid", 99 + "");
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