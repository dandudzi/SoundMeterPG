package pl.gda.pg.eti.kask.soundmeterpg.Dialogs;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import pl.gda.pg.eti.kask.soundmeterpg.Fragments.Settings;
import pl.gda.pg.eti.kask.soundmeterpg.R;

/**
 * Created by Daniel on 04.08.2016 at 12:10 :).
 */
public class SimpleDialogWithTextView {

    public static AlertDialog createDialogForResultOnYes(final InformationToCreateDialog info){
        Button accept_btn;
        Button cancel_btn;
        TextView textView;
        AlertDialog.Builder dialog = new AlertDialog.Builder(info.ownerDialog);
        dialog.setCancelable(false);
        dialog.setPositiveButton(null,null);
        dialog.setNegativeButton(null,null);
        LayoutInflater inflater = info.ownerDialog.getLayoutInflater();
        View dialogView = inflater.inflate(info.layout, null);
        final AlertDialog alertDialog = dialog.create();

        acceptButtonAction(info, dialogView, alertDialog);
        cancelButtonAction(info, dialogView, alertDialog);
        initializeTextView(info, dialogView);
        alertDialog.setView(dialogView);
        return alertDialog;
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

    private static void acceptButtonAction(final InformationToCreateDialog info, View dialogView, final AlertDialog alertDialog) {
        Button accept_btn;
        accept_btn = (Button) dialogView.findViewById(R.id.accept_btn);
        accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Open activityForResult","With request code: "+info.requestCode+" intent:" + info.intent.getAction());
                info.ownerFragment.startActivityForResult(info.intent,info.requestCode);
                alertDialog.dismiss();
            }
        });
    }

    private static void cancelButtonAction(final InformationToCreateDialog info, View dialogView, final AlertDialog alertDialog) {
        Button cancel_btn;
        cancel_btn = (Button) dialogView.findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("CancelActivityForResult","With request code: "+info.requestCode+" intent:" + info.intent.getAction());
                alertDialog.dismiss();
            }
        });
    }

    private static void initializeTextView(InformationToCreateDialog info, View dialogView) {
        TextView textView;
        textView = (TextView) dialogView.findViewById(R.id.edit_text_no_gps_dialog);
        switch (info.requestCode){
            case NoGPS.REQUEST_CODE_GPS :
                textView.setText(info.ownerDialog.getApplicationContext().getResources().getString(R.string.no_gps_text_dialog));
                break;
            case NoInternet.REQUEST_CODE_INTERNET :
                textView.setText(info.ownerDialog.getApplicationContext().getResources().getString(R.string.no_internet_text_dialog));
                break;
        }
    }
}
