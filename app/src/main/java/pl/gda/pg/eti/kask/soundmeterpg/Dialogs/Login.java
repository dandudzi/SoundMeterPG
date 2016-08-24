package pl.gda.pg.eti.kask.soundmeterpg.Dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import pl.gda.pg.eti.kask.soundmeterpg.R;

/**
 * Created by Daniel on 19.08.2016 at 11:45 :).
 */
public class Login {

    public static AlertDialog create(Activity ownerDialog, final PropertyChangeListener result) {

        final AlertDialog.Builder aboutDialog = new AlertDialog.Builder(ownerDialog);
        LayoutInflater inflater = ownerDialog.getLayoutInflater();

        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.login_dialog, null);

        TextView text =  (TextView) dialogView.findViewById(R.id.cookies_hyperlink_login_dialog);
        text.setMovementMethod(LinkMovementMethod.getInstance());

        aboutDialog.setView(dialogView);
        aboutDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.i("Login dialog","Press Ok");
                result.propertyChange(new PropertyChangeEvent(Boolean.class,"value",Boolean.FALSE,Boolean.TRUE));

            }
        });

        aboutDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.i("Login dialog","Press cancel");
                result.propertyChange(new PropertyChangeEvent(Boolean.class,"value",Boolean.FALSE,Boolean.FALSE));
            }
        });
        return aboutDialog.create();
    }
}
