package com.lexuantrieu.orderfood.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import com.lexuantrieu.orderfood.R;
import com.lexuantrieu.orderfood.model.Category;
import com.lexuantrieu.orderfood.presenter.SetFoodPresenter;
import com.lexuantrieu.orderfood.presenter.impl.SetFoodPresenterImpl;
import com.lexuantrieu.orderfood.service.APIUtils;
import com.lexuantrieu.orderfood.service.DataClient;
import com.lexuantrieu.orderfood.ui.dialog.AlertDialogFragment;
import com.lexuantrieu.orderfood.utils.LibraryString;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetFoodActivity extends AppCompatActivity implements SetFoodPresenter.View {

    SetFoodPresenter presenter;
    ProgressDialog progressDialog;
    String realPath = "";
    ImageView imgFood;
    ImageButton btnCamera, btnFolder;
    Spinner spnCategory;
    EditText edtNameFood, edtPriceFood, edtNumber, edtSaleFood;
    CheckBox cbSales;
    Button btnAdd;
    ProgressBar progressBar;
    final int REQUEST_CODE_CAMERA = 123, REQUEST_CODE_FOLDER = 456;
    ArrayList<Category> arrayList;
    ArrayAdapter<Category> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_food);
        init();
        presenter.invokeData();
        btnCamera.setOnClickListener(v -> ActivityCompat.requestPermissions(
                SetFoodActivity.this,
                new String[]{Manifest.permission.CAMERA},
                REQUEST_CODE_CAMERA
        ));
        btnFolder.setOnClickListener(v -> ActivityCompat.requestPermissions(
                SetFoodActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE_FOLDER
        ));
        cbSales.setOnCheckedChangeListener((compoundButton, b) -> {
            if(cbSales.isChecked())
                edtSaleFood.setVisibility(View.VISIBLE);
            else
                edtSaleFood.setVisibility(View.INVISIBLE);
        });
        btnAdd.setOnClickListener(v -> {
            if(realPath.equals("")){
                Toast.makeText(getApplicationContext(), "Vui lòng chọn ảnh đại diện", Toast.LENGTH_SHORT).show();
                return;
            }
            if(CheckNullEdt()) {
                CheckNameFood();
            }
        });
    }//end of onCreate

    private void init() {
        presenter = new SetFoodPresenterImpl(this, this);
        imgFood = findViewById(R.id.imgfood_setFood);
        btnCamera = findViewById(R.id.imgButtonCamera_setFood);
        btnFolder = findViewById(R.id.imgButtonFolder_setFood);
        edtNameFood = findViewById(R.id.edtNameFood_setFood);
        spnCategory = findViewById(R.id.spinnerCategoryFood_setFood);
        edtPriceFood = findViewById(R.id.edtPriceFood_setFood);
        edtNumber = findViewById(R.id.edtNumber_setFood);
        edtSaleFood = findViewById(R.id.edtPriceSale_setFood);
        cbSales = findViewById(R.id.checkBoxSales);
        btnAdd = findViewById(R.id.buttonAddChange_setFood);
        progressBar = findViewById(R.id.progressBar);
        progressDialog = new ProgressDialog(this);
    }

    private boolean CheckNullEdt() {
        boolean check = true;
        if( TextUtils.isEmpty(edtNameFood.getText())){
            edtNameFood.setError("Nhập tên món ăn");
            check = false;
        }
        if( TextUtils.isEmpty(edtPriceFood.getText())){
            edtPriceFood.setError("Nhập giá tiền");
            check = false;
        }
        if( TextUtils.isEmpty(edtNumber.getText())){
            edtNumber.setError("Nhập số lượng");
            check = false;
        }
        if(cbSales.isChecked() && TextUtils.isEmpty(edtSaleFood.getText())) {
            edtSaleFood.setError("Nhập giá tiền");
            check = false;
        }
        return  check;
    }

    private void CheckNameFood() {
        String nameFood = edtNameFood.getText().toString().trim();
        String slug = LibraryString.covertStringToSlug(nameFood);
        final boolean[] check = new boolean[1];
        DataClient dataClient = APIUtils.getData();
        Call<String> callback = dataClient.CheckExistsName(slug);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("LXT_Log","onResponse CheckExistsName: "+response.body());
                assert response.body() != null;
                if(response.body().equals("ok")){
                    UploadFood();
                }
                else {
                    edtNameFood.setError("Tên món ăn đã tồn tại.");
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("LXT_Error","onFailure CheckExistsName: "+t.getMessage());
            }
        });
    }

    private void UploadFood() {
        Category catItem = (Category) spnCategory.getSelectedItem();
        int catid = catItem.getId();
        String nameFood = edtNameFood.getText().toString().trim();
        String slug = LibraryString.covertStringToSlug(nameFood);
        float priceFood = (Float.parseFloat(edtPriceFood.getText().toString().trim()));
        int number = (Integer.parseInt(edtNumber.getText().toString().trim()));
        String pricesale = (cbSales.isChecked()) ? String.valueOf(Float.parseFloat(edtSaleFood.getText().toString().trim())) : "NULL";
        int created_by = 1;
        int status = 1;

        File file = new File(realPath);
        String file_path = file.getAbsolutePath();
        String[] arr1 = file_path.split("\\.");
        String typeFile = arr1[1];
//        String path = file_path.substring(0,file_path.lastIndexOf('/'));
//        file_path = path+"/"+nameImage+"."+typeFile;
        file_path = slug + "." + typeFile;
//        file_path = arr1[0]+ System.currentTimeMillis()+"."+arr1[1];
        Log.d("LXT_Log", "file_path: " + file_path);

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload_file",file_path,requestBody);

        DataClient dataClient = APIUtils.getData();
        Call<String> callback = dataClient.UploadPhoto(body);
        SwapStatus(-1);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("LXT_Log", "onResponse UploadPhoto :"+response.body());
                assert response.body() != null;
                if(!response.body().equals("Failed")) {
                    String imgLink = response.body();
                    //
                    DataClient InsertFood = APIUtils.getData();
                    Call<String> callback = InsertFood.InsertFood(catid,nameFood,slug,imgLink,number,priceFood,pricesale,created_by,status);
                    callback.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Log.i("LXT_Log", "onResponse InsertFood :"+response.body());
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
                            Toast.makeText(SetFoodActivity.this, "Lỗi Network.", Toast.LENGTH_SHORT).show();
                            Log.e("LXT_Error", "onFailure InsertFood :"+t.getMessage());
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
                Toast.makeText(SetFoodActivity.this, "Lỗi Network.", Toast.LENGTH_SHORT).show();
                Log.e("LXT_Error", "onFailure UploadImage:"+t.getMessage());
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
        edtSaleFood.setText(null);
        edtNumber.setText(null);
        cbSales.setChecked(false);
        imgFood.setImageResource(R.drawable.imagepreview);
        realPath = "";
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


    @Override
    public void onInvokeDataSuccess() {
        onStopProcessBar();
        edtNameFood.requestFocus();
    }

    @Override
    public void onInvokeDataFail() {
        onStopProcessBar();
       // Create YesNoDialogFragment
        AlertDialogFragment dialogFragment = new AlertDialogFragment(this, "Lỗi tải dữ liệu", "Tải lại", resultOk -> {
            if(resultOk == Activity.RESULT_OK) {
                presenter.invokeData();
            } else {//if(resultCode == Activity.RESULT_CANCELED) {
                finish();
            }
        });
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        // Show:
        dialogFragment.show(fragmentManager, "Dialog");
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
    public void initAdapter(Context context, List<Category> listData) {
        arrayList = (ArrayList<Category>) listData;
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public void initSpinner() {
        spnCategory.setAdapter(adapter);
    }

}