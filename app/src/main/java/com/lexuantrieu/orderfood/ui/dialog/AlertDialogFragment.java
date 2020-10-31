package com.lexuantrieu.orderfood.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AlertDialogFragment extends DialogFragment{

    private Context context;
    private String title = "Title";
    private String message = "Message";
    private AlertDialogFragmentListener listener;

    public AlertDialogFragment(Context context, String title, String message, AlertDialogFragmentListener listener) {
        this.context = context;
        this.title = title;
        this.message = message;
        this.listener = listener;
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        this.setCancelable(false);
        return new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", (dialog, which) -> buttonOkClick())
                .setNegativeButton("Hủy", (dialog, which) -> buttonNoClick())
                .create();
    }

    private void buttonOkClick() {
        this.listener.onClickResultDialog(Activity.RESULT_OK);//-1
    }

    private void buttonNoClick() {
        this.listener.onClickResultDialog(Activity.RESULT_CANCELED);// 0
    }

}