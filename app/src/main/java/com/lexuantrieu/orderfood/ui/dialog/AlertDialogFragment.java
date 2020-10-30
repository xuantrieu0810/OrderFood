package com.lexuantrieu.orderfood.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AlertDialogFragment extends DialogFragment {

    public static final String ARG_TITLE = "Title";
    public static final String ARG_MESSAGE = "Message";

    private AlertDialogFragmentListener listener;

    public interface AlertDialogFragmentListener {

        void onClickResultDialog(int resultOk, @Nullable Intent data);
    }

    public AlertDialogFragment() {
    }

    public void setOnAlertDialogFragmentListener(AlertDialogFragmentListener listener) {
        listener = listener;
    }

//    @Override
//    public void onAttachFragment(@NonNull Fragment fragment) {
//        if (fragment instanceof AlertDialogFragment) {
//            AlertDialogFragment alertDialogFragment = (AlertDialogFragment) fragment;
//            alertDialogFragment.setOnAlertDialogFragmentListener(listener);
//        }
//    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        String title = args.getString(ARG_TITLE);
        String message = args.getString(ARG_MESSAGE);

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        buttonOkClick();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        buttonNoClick();
                    }
                })
                .create();
    }

    private void buttonOkClick() {
        int resultCode = Activity.RESULT_OK;
        Intent data  = null;
//        if(this.listener != null)  {
            this.listener.onClickResultDialog(Activity.RESULT_OK , data);
//        }
        // Open this DialogFragment from another Fragment.
//        else {
//            // Send result to your TargetFragment.
//            // See (Your) TargetFragment.onActivityResult()
//            getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, data);
//        }
    }

    private void buttonNoClick() {
        int resultCode = Activity.RESULT_CANCELED;
        Intent data  = null;
        // Open this DialogFragment from an Activity.
//        if(this.listener != null)  {
            this.listener.onClickResultDialog(resultCode, data);
//        }
        // Open this DialogFragment from another Fragment.
//        else {
//            // Send result to your TargetFragment.
//            // See (Your) TargetFragment.onActivityResult()
//            getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, data);
//        }
    }

}