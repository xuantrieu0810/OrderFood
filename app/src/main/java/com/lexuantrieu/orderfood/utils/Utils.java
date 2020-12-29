package com.lexuantrieu.orderfood.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;

public class Utils {
    public static final String TAG = "LXT_Log";
    

    /*public static String GetTokenCurrent(Context context) {
        final String[] tokenResult = new String[1];
        tokenResult[0] = "hello";
        AppDatabase db = AppDatabase.getInstance(context);
        db.getUserDao().getListUser().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    User user = (User) response.get(0);
                    if(user != null){
                        tokenResult[0] = user.getToken();
                    } else {
                        tokenResult[0] = null;
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                });

        return tokenResult[0];
    }*/
    
    public static String GetTokenLocal(Context context){
        String result = "";
        KeyStore keyStore;
        try {
            try {
                keyStore = KeyStore.getInstance("AndroidKeyStore");
                keyStore.load(null);
            }
            catch(Exception e) {
                return "";
            }
            //
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry("TOKEN", null);
//            RSAPrivateKey privateKey = (RSAPrivateKey) privateKeyEntry.getPrivateKey();
            PrivateKey privateKey = privateKeyEntry.getPrivateKey(); // Don't TypeCast to RSAPrivateKey

            Cipher output = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            output.init(Cipher.DECRYPT_MODE, privateKey);

            SharedPreferences sharedPreferences = context.getSharedPreferences("DataLocalApp", Context.MODE_PRIVATE);
            String destination = decryptString(sharedPreferences.getString("TokenApi_0", ""),output)   +"."
                    + decryptString(sharedPreferences.getString("TokenApi_1", ""),output)              +"."
                    + decryptString(sharedPreferences.getString("TokenApi_2", ""),output);
            if(destination.equals(".."))
                return "";
            else
                return destination;
        } catch (Exception e) {
            Toast.makeText(context, "Exception GetTokenLocal " + e.getMessage() + " occured", Toast.LENGTH_LONG).show();
            Log.e("LXT_Log", Log.getStackTraceString(e));
        }
        return "";
    }
    private static String decryptString(String destination,Cipher output) {
        if(destination.isEmpty()) {
            return "";
        }
        try {
            CipherInputStream cipherInputStream = new CipherInputStream(
                    new ByteArrayInputStream(Base64.decode(destination, Base64.DEFAULT)), output);
            ArrayList<Byte> values = new ArrayList<>();
            int nextByte;
            while ((nextByte = cipherInputStream.read()) != -1) {
                values.add((byte)nextByte);
            }

            byte[] bytes = new byte[values.size()];
            for(int i = 0; i < bytes.length; i++) {
                bytes[i] = values.get(i).byteValue();
            }

            return new String(bytes, 0, bytes.length, StandardCharsets.UTF_8);

        }catch (Exception e) {
            return "";
        }
    }

    public static void RemoveTokenLocal(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("DataLocalApp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.remove("TokenApi_0");
//        editor.remove("TokenApi_1");
//        editor.remove("TokenApi_2");
//
//        editor.apply(); // Apply changes
        //Clear all
        editor.clear();
        editor.apply(); // Apply changes
    }
}
