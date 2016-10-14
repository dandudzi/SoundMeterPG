package pl.gda.pg.eti.kask.soundmeterpg.Dialogs;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Daniel on 04.08.2016 at 12:10 :).
 */
public class SimpleDialogWithTextView {

    public static AlertDialog createDialogForResultOnYes(final InformationToCreateDialog info){
        AlertDialog.Builder dialog = new AlertDialog.Builder(info.ownerDialog);
        LayoutInflater inflater = info.ownerDialog.getLayoutInflater();

        View dialogView = inflater.inflate(info.layout, null);

        dialog.setCancelable(false);
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick( final DialogInterface dialog,  final int id) {
                Log.i("Open activityForResult","With request code: "+info.requestCode+" intent:" + info.intent.getAction());
                info.ownerFragment.startActivityForResult(info.intent,info.requestCode);
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog,  final int id) {
                Log.i("CancelActivityForResult","With request code: "+info.requestCode+" intent:" + info.intent.getAction());
                dialog.cancel();
            }
        });
        dialog.setView(dialogView);
        return dialog.create();
    }

    public static AlertDialog createDialog(String msg, Context context, String title){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setMessage(msg);
        dialog.setCancelable(false);
        dialog.setNeutralButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        return dialog.create();
    }
}
