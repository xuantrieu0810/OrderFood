package com.lexuantrieu.orderfood.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lexuantrieu.orderfood.R;
import com.lexuantrieu.orderfood.model.Category;
import com.lexuantrieu.orderfood.utils.Server;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SetFoodActivity extends AppCompatActivity {

    boolean ckUploadImage = false;
    ImageView imgFood;
    public static Bitmap bitmap = null;
    ImageButton btnCamera, btnFolder;
    Spinner spnCategory;
    EditText edtnameFood, edtpriceFood;
    Button btnAdd;
    final int REQUEST_CODE_CAMERA = 123, REQUEST_CODE_FOLDER = 456;
    ArrayList<Category> arrayCategory;
    ArrayList<String> arrayNameCat;
    ArrayAdapter arrayAdapter;
    int idCategoryClick=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_food);

        imgFood =  (ImageView) findViewById(R.id.imgfood_setFood);
        btnCamera = (ImageButton) findViewById(R.id.imgButtonCamera_setFood);
        btnFolder = (ImageButton) findViewById(R.id.imgButtonFolder_setFood);
        edtnameFood = (EditText) findViewById(R.id.edtNameFood_setFood);
        spnCategory = (Spinner) findViewById(R.id.spinnerCategoryFood_setFood);
        edtpriceFood = (EditText) findViewById(R.id.edtPriceFood_setFood);
        btnAdd = (Button) findViewById(R.id.buttonAddChange_setFood);

        arrayNameCat = new ArrayList<>();
        arrayCategory = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,arrayNameCat);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        GetCategoryFood();
        GetArrayListCat();
        spnCategory.setAdapter(arrayAdapter);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent,REQUEST_CODE_CAMERA);
                ActivityCompat.requestPermissions(
                        SetFoodActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        REQUEST_CODE_CAMERA
                );
            }
        });
        btnFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType("image/*");
//                startActivityForResult(intent,REQUEST_CODE_FOLDER);
                ActivityCompat.requestPermissions(
                        SetFoodActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_FOLDER
                );
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int check = 1;
                if( TextUtils.isEmpty(edtnameFood.getText())){
                    edtnameFood.setError("Nhập tên món ăn");
                    check = -1;
                }
                if( TextUtils.isEmpty(edtpriceFood.getText())){
                    edtpriceFood.setError("Nhập giá tiền");
                    check = -1;
                }
                if(check != -1) {
                    String nameFood = edtnameFood.getText().toString().trim();
                    Double priceFood = Double.valueOf(edtpriceFood.getText().toString().trim());

                    if (CheckBeforeAdd(nameFood, priceFood, idCategoryClick) == 1) {
                        UploadImage(nameFood);
                        if(ckUploadImage) {
                            AddFoodToData();
                            Toast.makeText(SetFoodActivity.this, "Đã thêm!!!", Toast.LENGTH_SHORT).show();
                            edtnameFood.setText("");
                            edtpriceFood.setText("");
                            imgFood.setImageResource(R.drawable.imagepreview);
                        }
                    }
                }
            }
        });
        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idCategoryClick = arrayCategory.get(position).getIdCategory();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }//end of onCreate

    private void AddFoodToData() {
    }

    private String ImageToString(Bitmap bitmap) {

        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        SetFoodActivity.bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArray);

        byte[] imageFood = byteArray.toByteArray();
        return Base64.encodeToString(imageFood,Base64.DEFAULT);
    }

    private  void UploadImage (final String nameImage) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlUploadImage,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ckUploadImage = true;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SetFoodActivity.this, "Xảy ra lỗi. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("name", nameImage.trim());
                params.put("image", ImageToString(bitmap));

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private int CheckBeforeAdd(String nameFood, Double priceFood, int idCategoryClick) {
        //check

        return 1;
    }

    private void GetArrayListCat() {
        int n = arrayCategory.size();
        for(int i = 0 ; i < n; i++)
            arrayNameCat.add(arrayCategory.get(i).getNameCategory());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_CAMERA:
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,REQUEST_CODE_CAMERA);
                } else Toast.makeText(this, "Bạn không cho phép mở Camera", Toast.LENGTH_SHORT).show();
                break;
            case REQUEST_CODE_FOLDER:
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent,REQUEST_CODE_FOLDER);
                } else Toast.makeText(this, "Bạn không cho phép mở Thư viện ảnh", Toast.LENGTH_SHORT).show();
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null) {
            bitmap = (Bitmap) data.getExtras().get("data");
            imgFood.setImageBitmap(bitmap);
        }
        if(requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(inputStream);
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                imgFood.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void GetCategoryFood() {

    }
}