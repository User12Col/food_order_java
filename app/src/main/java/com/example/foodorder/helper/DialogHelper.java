package com.example.foodorder.helper;

import android.app.Dialog;
import android.content.Context;

import com.example.foodorder.R;

public class DialogHelper {
    private Context context;
    private Dialog dialog;

    public DialogHelper(Context context, Dialog dialog) {
        this.context = context;
        this.dialog = dialog;
    }

    public void showLoadingDialog() {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.setCancelable(false);
        dialog.show();
    }

    public void dismissLoadingDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
