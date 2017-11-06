package net.flow9.android.firebasemessage.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by pc on 11/3/2017.
 */

public class DialogUtil {

    public static void showDialog(String msg, final Activity activity, final boolean activityFinish){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        dialogBuilder.setTitle("Notice");
        dialogBuilder.setMessage(msg);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if(activityFinish)
                    activity.finish();
            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }
}
