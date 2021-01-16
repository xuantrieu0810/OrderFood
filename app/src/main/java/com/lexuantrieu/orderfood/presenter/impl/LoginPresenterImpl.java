package com.lexuantrieu.orderfood.presenter.impl;


import android.content.Context;
import android.content.SharedPreferences;
import android.security.KeyPairGeneratorSpec;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.lexuantrieu.orderfood.model.ProfileModel;
import com.lexuantrieu.orderfood.model.room.User;
import com.lexuantrieu.orderfood.model.room.database.AppDatabase;
import com.lexuantrieu.orderfood.network.RestClient;
import com.lexuantrieu.orderfood.presenter.LoginPresenter;
import com.lexuantrieu.orderfood.service.LoginService;
import com.lexuantrieu.orderfood.utils.LibraryString;
import com.lexuantrieu.orderfood.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.security.auth.x500.X500Principal;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenterImpl implements LoginPresenter {

    private final Context context;
    private final LoginPresenter.View view;
    private AppDatabase db;
    private KeyStore keyStore;

    public LoginPresenterImpl(Context context, LoginPresenter.View view, AppDatabase db) {

        this.context = context;
        this.view = view;
        this.db = db;
        ///add new
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
        }
        catch(Exception ignored) {

        }
    }

    @Override
    public void onCheckToken() {
//        view.onStartProcessBar("Loading...");
        String token = Utils.GetTokenLocal(context);
        if(!token.isEmpty()) {
            view.onLoginSuccess();
            return;
        }

        /*db.getUserDao().getListUser().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.get(0).getToken() != null) {
                        view.onLoginSuccess();                    }
                }, throwable -> {
                    view.onStopProcessBar();
                    throwable.printStackTrace();
                });*/
    }

    @Override
    public void requestLogin(String username, String password) {
        String passwordSHA1 = null;
        try {
            passwordSHA1 = LibraryString.SHA1(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        view.onLoginPending();
        LoginService service = RestClient.createService(LoginService.class);
        service.requetLogin(username, passwordSHA1).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.getError().equals("null")) {
                        ProfileModel mUser = response.getData();
                        User user = new User(mUser.getUsername(),mUser.getFullname(),mUser.getRole(),"Not here!");
                        db.getUserDao().insertNote(user);
                        //Luu token
//                        view.onLoginSuccess();
                        createNewKeys("TOKEN", mUser.getToken());
                        return;
                    } else {
                        view.onLoginFail();
                        Log.e("LXT_Log", "ErrorCode: " + response.getError());
                    }
                }, throwable -> {
                    Log.e("LXT_Log_Error","Response Login: "+throwable.getMessage());
                    throwable.printStackTrace();
                });

    }



    public void createNewKeys(String alias, String token) {

        if(token.isEmpty()) {
            Toast.makeText(context, "TOKEN API NULL", Toast.LENGTH_LONG).show();
            view.onLoginFail();
            return;
        }
        try {
            // Create new key if needed
            if (keyStore.containsAlias(alias)) {
                deleteKey(alias);
            }
            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            end.add(Calendar.YEAR, 1);
            KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(context)
                    .setAlias(alias)
                    .setSubject(new X500Principal("CN=Sample Name, O=Android Authority"))
                    .setSerialNumber(BigInteger.ONE)
                    .setStartDate(start.getTime())
                    .setEndDate(end.getTime())
                    .build();
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
            generator.initialize(spec);

            KeyPair keyPair = generator.generateKeyPair();
        } catch (Exception e) {
            Toast.makeText(context, "Vui lòng thử lại", Toast.LENGTH_LONG).show();//Call fail
            Log.e("LXT_Log", Log.getStackTraceString(e));
            view.onLoginFail();
            return;
        }



        String[] splits = token.split("\\.", 3);
//            Log.e("LXT_Log", splits[0]);
//            Log.e("LXT_Log", splits[1]);
//            Log.e("LXT_Log", splits[2]);
        encryptString(alias,splits[0],"TokenApi_0");
        encryptString(alias,splits[1],"TokenApi_1");
        encryptString(alias,splits[2],"TokenApi_2");
    }


    public void deleteKey(final String alias) {
        try {
            keyStore.deleteEntry(alias);
        } catch (KeyStoreException e) {
            Toast.makeText(context, "Exception " + e.getMessage() + " occured", Toast.LENGTH_LONG).show();//Call fail
            Log.e("LXT_Log", Log.getStackTraceString(e));
        }
    }


    public void encryptString(String alias, String source, String nameSubToken) {
        try {
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(alias, null);
//            RSAPublicKey publicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();
            PublicKey publicKey = privateKeyEntry.getCertificate().getPublicKey(); // Don't TypeCast to RSAPublicKey

            Cipher inCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            inCipher.init(Cipher.ENCRYPT_MODE, publicKey);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CipherOutputStream cipherOutputStream = new CipherOutputStream( outputStream, inCipher);
            cipherOutputStream.write(source.getBytes("UTF-8"));
            cipherOutputStream.close();

            byte [] vals = outputStream.toByteArray();

            SharedPreferences sharedPreferences = context.getSharedPreferences("DataLocalApp", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(nameSubToken, (Base64.encodeToString(vals, Base64.DEFAULT)));
            editor.apply();

        } catch (Exception e) {
            Toast.makeText(context, "Exception " + e.getMessage() + " occured", Toast.LENGTH_LONG).show();
            Log.e("LXT_Log", Log.getStackTraceString(e));
            view.onLoginFail();
            return;
        }
        view.onLoginSuccess();
    }
}
