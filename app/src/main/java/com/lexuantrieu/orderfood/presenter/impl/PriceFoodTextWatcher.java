package com.lexuantrieu.orderfood.presenter.impl;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

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
        String valueOnTextChanged = String.valueOf(s);
        if (!valueOnTextChanged.isEmpty() && !valueOnTextChanged.equals("0") && !valueOnTextChanged.endsWith("00")) {
            valueOnTextChanged = valueOnTextChanged.concat("00");
            editText.setText(valueOnTextChanged);
            editText.setSelection(valueOnTextChanged.length() - 2);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
