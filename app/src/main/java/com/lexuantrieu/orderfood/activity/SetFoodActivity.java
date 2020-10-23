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
import android.widget.AdapterView;
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
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetFoodActivity extends AppCompatActivity {

    String realPath = "";
    boolean ckUploadImage = false;
    ImageView imgFood;
    ImageButton btnCamera, btnFolder;
    Spinner spnCategory;
    EditText edtnameFood, edtpriceFood;
    Button btnAdd;
    ProgressBar progressBar;
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
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
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
//                        if(ckUploadImage) {
//                            AddFoodToData();
//                            Toast.makeText(SetFoodActivity.this, "Đã thêm!!!", Toast.LENGTH_SHORT).show();
//                            edtnameFood.setText("");
//                            edtpriceFood.setText("");
//                            imgFood.setImageResource(R.drawable.imagepreview);
//                        }
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

    private  void UploadImage (final String nameImage) {
        if(realPath.equals("")){
            Toast.makeText(this, "Vui lòng chọn ảnh đại diện", Toast.LENGTH_SHORT).show();
            return;
        }
        SwapStatus(-1);
        File file = new File(realPath);
        String file_path = file.getAbsolutePath();
        String[] arr1 = file_path.split("\\.");
        String typeFile = arr1[1];
        String[] arr2 = file_path.split("\\/");
        String pathFile = file_path.delete;
        file_path = covertToString(nameImage)+"."+typeFile;
//        file_path = arr1[0]+ System.currentTimeMillis()+"."+arr1[1];
        Log.d("LXT_Log", "file_path: "+file_path);

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload_file",file_path,requestBody);

        DataClient dataClient = APIUtils.getData();
        Call<String> callBack = dataClient.UploadPhoto(body);
        callBack.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("LXT_Log", "response.body :"+response.body()+"\nresponse.message: "+response.message());
                assert response.body() != null;
                if(!response.body().toString().equals("Failed"))
                    Toast.makeText(SetFoodActivity.this, "Đã upload ảnh.", Toast.LENGTH_SHORT).show();
                SwapStatus(1);
                ClearContent();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(SetFoodActivity.this, "Lỗi Sever. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                Log.d("LXT_Error", "onFailure :"+t.getMessage());
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
        edtnameFood.setText(null);
        edtpriceFood.setText(null);
        imgFood.setImageResource(R.drawable.imagepreview);
        realPath = "";
    }

    private int CheckBeforeAdd(String nameFood, Double priceFood, int idCategoryClick) {
        //

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
            Bitmap photo = null;
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

    private void GetCategoryFood() {

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
}