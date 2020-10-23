package com.lexuantrieu.orderfood.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.lexuantrieu.orderfood.R;
import com.lexuantrieu.orderfood.Service.APIUtils;
import com.lexuantrieu.orderfood.Service.DataClient;
import com.lexuantrieu.orderfood.model.Category;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetFoodActivity extends AppCompatActivity {

    String realPath = "";
    ImageView imgFood;
    ImageButton btnCamera, btnFolder;
    Spinner spnCategory;
    EditText edtNameFood, edtPriceFood;
    Button btnAdd;
    ProgressBar progressBar;
    final int REQUEST_CODE_CAMERA = 123, REQUEST_CODE_FOLDER = 456;
    ArrayList<Category> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_food);

        imgFood = findViewById(R.id.imgfood_setFood);
        btnCamera = findViewById(R.id.imgButtonCamera_setFood);
        btnFolder = findViewById(R.id.imgButtonFolder_setFood);
        edtNameFood = findViewById(R.id.edtNameFood_setFood);
        spnCategory = findViewById(R.id.spinnerCategoryFood_setFood);
        edtPriceFood = findViewById(R.id.edtPriceFood_setFood);
        btnAdd = findViewById(R.id.buttonAddChange_setFood);
        progressBar = findViewById(R.id.progressBar);
        GetCategoryFood();
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
                Category catItem = (Category) spnCategory.getSelectedItem();
                int check = 1;
                if( TextUtils.isEmpty(edtNameFood.getText())){
                    edtNameFood.setError("Nhập tên món ăn");
                    check = -1;
                }
                if( TextUtils.isEmpty(edtPriceFood.getText())){
                    edtPriceFood.setError("Nhập giá tiền");
                    check = -1;
                }
                if(check != -1) {
                    String nameFood = edtNameFood.getText().toString().trim();
                    Float priceFood = Float.valueOf(edtPriceFood.getText().toString().trim());

                    if (CheckBeforeAdd(nameFood, priceFood, catItem.getId()) == 1) {
                        String slug = covertToString(nameFood);
                        UploadFood(catItem.getId(),nameFood,slug,99,priceFood, (float) -1.0,1,1);
                    }
                }
            }
        });
    }//end of onCreate
    private void SetAdapter() {
        ArrayAdapter<Category> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategory.setAdapter(adapter);
    }
    private void GetCategoryFood() {
        SwapStatus(-1);
        DataClient dataClient = APIUtils.getData();
        Call<List<Category>> callback = dataClient.GetCategory();
        callback.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                assert response.body() != null;
                if(!response.body().equals("null")) {
                    arrayList = (ArrayList<Category>) response.body();
                    Log.d("LXT_Log", String.valueOf(arrayList));
                    SetAdapter();
                    SwapStatus(1);
                } else {
                    Toast.makeText(SetFoodActivity.this, "Xảy ra lỗi.", Toast.LENGTH_SHORT).show();
                    Log.d("LXT_Log", "response: "+response.body());
                }
            }
            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.d("LXT_Error", "v: "+t.getMessage());
            }
        });
    }
    private void UploadFood(String catid, String name, String image, int number, Float price, Float pricesale, int created_by, int status) {
        if(realPath.equals("")){
            Toast.makeText(this, "Vui lòng chọn ảnh đại diện", Toast.LENGTH_SHORT).show();
            return;
        }
        String chPriceSale = (pricesale == -1.0) ? "NULL" : pricesale.toString();
        SwapStatus(-1);
        File file = new File(realPath);
        String file_path = file.getAbsolutePath();
        String[] arr1 = file_path.split("\\.");
        String typeFile = arr1[1];
//        String path = file_path.substring(0,file_path.lastIndexOf('/'));
//        file_path = path+"/"+nameImage+"."+typeFile;
        file_path = image+"."+typeFile;
//        file_path = arr1[0]+ System.currentTimeMillis()+"."+arr1[1];
        Log.d("LXT_Log", "file_path: "+file_path);

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload_file",file_path,requestBody);

        DataClient dataClient = APIUtils.getData();
        Call<String> callback = dataClient.UploadPhoto(body);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("LXT_Log", "\nresponse.body :"+response.body()+"\nresponse.message: "+response.message());
                assert response.body() != null;
                if(!response.body().equals("Failed")) {
                    String imgLink = response.body();
                    //
                    DataClient InsertFood = APIUtils.getData();
                    Call<String> callback = InsertFood.InsertFood(catid+"",name+"",imgLink+"",number+"",price+"",chPriceSale+"",created_by+"",status+"");
                    callback.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Log.d("LXT_Log", "\nresponse.body :"+response.body()+"\nresponse.message: "+response.message());
                            assert response.body() != null;
                            if(response.body().equals("success")){
                                Toast.makeText(SetFoodActivity.this, "Đã thêm thành công.", Toast.LENGTH_SHORT).show();
                                SwapStatus(1);
                                ClearContent();
                            } else {
                                Toast.makeText(SetFoodActivity.this, "Xảy ra lỗi. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                                SwapStatus(1);
                            }
                        }
                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(SetFoodActivity.this, "Lỗi Sever. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                            Log.d("LXT_Error", "onFailure InsertFood :"+t.getMessage());
                            SwapStatus(1);
                        }
                    });
                    //
                } else {
                    Toast.makeText(SetFoodActivity.this, "Xảy ra lỗi. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                    SwapStatus(1);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(SetFoodActivity.this, "Lỗi Sever. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                Log.d("LXT_Error", "onFailure UploadImage:"+t.getMessage());
                SwapStatus(1);
            }
        });
    }



    private void SwapStatus(int key) {
        if(key == -1) {
            btnAdd.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }
        else {
            btnAdd.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void ClearContent() {
        edtNameFood.setText(null);
        edtPriceFood.setText(null);
        imgFood.setImageResource(R.drawable.imagepreview);
        realPath = "";
    }

    private int CheckBeforeAdd(String nameFood, Float priceFood, String catID) {
        //

        return 1;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "data", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI (Uri contentUri) {
        String path = null;
        String[] proj = { MediaStore.MediaColumns.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }



    public static String covertToString(String value) {
        value = value.trim().replace(' ', '-');
        try {
            String temp = Normalizer.normalize(value, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(temp).replaceAll("");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imgFood.setImageBitmap(photo);
            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            Uri tempUri = getImageUri(getApplicationContext(), photo);
            realPath = getRealPathFromURI(tempUri);
            Log.d("LXT_Log","realPath: "+ realPath);
        }
        if(requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null) {
            Bitmap photo;
            Uri uri = data.getData();
            realPath = getRealPathFromURI(uri);
            Log.d("LXT_Log","realPath: "+ realPath);
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                photo = BitmapFactory.decodeStream(inputStream);
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                imgFood.setImageBitmap(photo);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}