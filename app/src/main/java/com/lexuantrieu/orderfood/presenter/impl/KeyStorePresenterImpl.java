//package com.lexuantrieu.orderfood.presenter.impl;
//
//import android.content.Context;
//import android.content.DialogInterface;
//import android.security.KeyPairGeneratorSpec;
//import android.util.Base64;
//import android.util.Log;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AlertDialog;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.math.BigInteger;
//import java.security.KeyPair;
//import java.security.KeyPairGenerator;
//import java.security.KeyStore;
//import java.security.KeyStoreException;
//import java.security.PrivateKey;
//import java.security.PublicKey;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Enumeration;
//import java.util.List;
//
//import javax.crypto.Cipher;
//import javax.crypto.CipherInputStream;
//import javax.crypto.CipherOutputStream;
//import javax.security.auth.x500.X500Principal;
//
//public class KeyStorePresenterImpl  {
//
//    private KeyStore keyStore;
//    private Context context;
//    private KeyStorePresenterImpl.View view;
//    List<String> keyAliases;
//
//    public KeyStorePresenterImpl(KeyStore keyStore, Context context, KeyStorePresenterImpl.View view) {
//        this.keyStore = keyStore;
//        this.context = context;
//        this.view = view;
//        ///add new
//        try {
//            keyStore = KeyStore.getInstance("AndroidKeyStore");
//            keyStore.load(null);
//        }
//        catch(Exception e) {}
//    }
//
//
//    @Override
//    public void createNewKeys(String alias) {
//        try {
//            // Create new key if needed
//            if (!keyStore.containsAlias(alias)) {
//                Calendar start = Calendar.getInstance();
//                Calendar end = Calendar.getInstance();
//                end.add(Calendar.YEAR, 1);
//                KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(context)
//                        .setAlias(alias)
//                        .setSubject(new X500Principal("CN=Sample Name, O=Android Authority"))
//                        .setSerialNumber(BigInteger.ONE)
//                        .setStartDate(start.getTime())
//                        .setEndDate(end.getTime())
//                        .build();
//                KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
//                generator.initialize(spec);
//
//                KeyPair keyPair = generator.generateKeyPair();
//            }
//        } catch (Exception e) {
//            Toast.makeText(context, "Exception " + e.getMessage() + " occured", Toast.LENGTH_LONG).show();//Call fail
//            Log.e("LXT_Log", Log.getStackTraceString(e));
//        }
//        refreshKeys();
//    }
//
//    @Override
//    public void deleteKey(final String alias) {
//        AlertDialog alertDialog = new AlertDialog.Builder(context)
//                .setTitle("Delete Key")
//                .setMessage("Do you want to delete the key \"" + alias + "\" from the keystore?")
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        try {
//                            keyStore.deleteEntry(alias);
//                            refreshKeys();
//                        } catch (KeyStoreException e) {
//                            Toast.makeText(context, "Exception " + e.getMessage() + " occured", Toast.LENGTH_LONG).show();//Call fail
//                            Log.e("LXT_Log", Log.getStackTraceString(e));
//                        }
//                        dialog.dismiss();
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .create();
//        alertDialog.show();
//    }
//
//    @Override
//    public String encryptString(String alias, String source) {
//        try {
//            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(alias, null);
////            RSAPublicKey publicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();
//            PublicKey publicKey = privateKeyEntry.getCertificate().getPublicKey(); // Don't TypeCast to RSAPublicKey
//
//            if(source.isEmpty()) {
//                Toast.makeText(context, "Enter text in the 'Initial Text' widget", Toast.LENGTH_LONG).show();
//                return null;
//            }
//
//            Cipher inCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//            inCipher.init(Cipher.ENCRYPT_MODE, publicKey);
//
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            CipherOutputStream cipherOutputStream = new CipherOutputStream(
//                    outputStream, inCipher);
//            cipherOutputStream.write(source.getBytes("UTF-8"));
//            cipherOutputStream.close();
//
//            byte [] vals = outputStream.toByteArray();
//            return (Base64.encodeToString(vals, Base64.DEFAULT));
//        } catch (Exception e) {
//            Toast.makeText(context, "Exception " + e.getMessage() + " occured", Toast.LENGTH_LONG).show();
//            Log.e("LXT_Log", Log.getStackTraceString(e));
//        }
//        return null;
//    }
//
//    @Override
//    public String decryptString(String alias, String destination) {
//        try {
//            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(alias, null);
////            RSAPrivateKey privateKey = (RSAPrivateKey) privateKeyEntry.getPrivateKey();
//            PrivateKey privateKey = privateKeyEntry.getPrivateKey(); // Don't TypeCast to RSAPrivateKey
//
//            Cipher output = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//            output.init(Cipher.DECRYPT_MODE, privateKey);
//
//            if(destination.isEmpty()) {
//                Toast.makeText(context, "Enter text in the 'Initial Text' widget", Toast.LENGTH_LONG).show();
//                return alias;
//            }
//
//            CipherInputStream cipherInputStream = new CipherInputStream(
//                    new ByteArrayInputStream(Base64.decode(destination, Base64.DEFAULT)), output);
//            ArrayList<Byte> values = new ArrayList<>();
//            int nextByte;
//            while ((nextByte = cipherInputStream.read()) != -1) {
//                values.add((byte)nextByte);
//            }
//
//            byte[] bytes = new byte[values.size()];
//            for(int i = 0; i < bytes.length; i++) {
//                bytes[i] = values.get(i).byteValue();
//            }
//
//            String finalText = new String(bytes, 0, bytes.length, "UTF-8");
//            return (finalText);
//
//        } catch (Exception e) {
//            Toast.makeText(context, "Exception " + e.getMessage() + " occured", Toast.LENGTH_LONG).show();
//            Log.e("LXT_Log", Log.getStackTraceString(e));
//        }
//        return null;
//    }
//
//    private void refreshKeys() {
//        keyAliases = new ArrayList<>();
//        try {
//            Enumeration<String> aliases = keyStore.aliases();
//            while (aliases.hasMoreElements()) {
//                keyAliases.add(aliases.nextElement());
//            }
//        }
//        catch(Exception e) {}
//
////        if(listAdapter != null)
////            listAdapter.notifyDataSetChanged();
//    }
//}
