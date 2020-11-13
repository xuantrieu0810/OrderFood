package com.lexuantrieu.orderfood.presenter.impl;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class PriceFoodTextWatcher implements TextWatcher {

    String valueBefore;
    EditText editText;
    Context context;
    public PriceFoodTextWatcher(EditText editText, Context context) {
        this.editText = editText;
        this.context = context;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        valueBefore = s.toString();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        try {
            int value = Integer.parseInt(s.toString());
            if(value % 100 != 0) {
                editText.setText(String.valueOf(value*100));
            }
        } catch (Exception e) {
            Log.e("LXT_Log", String.valueOf(e));
            editText.setText(valueBefore);
//            editText.setError("Giá trị không hợp lệ.");
            Toast.makeText(context, "Giá trị không hợp lệ.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
