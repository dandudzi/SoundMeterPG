package pl.gda.pg.eti.kask.soundmeterpg.Dialogs;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Daniel on 04.08.2016 at 12:10 :).
 */
class SimpleDialogWithTextView {

    public static AlertDialog createDialog(final InformationToCreateDialog info){
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
}
