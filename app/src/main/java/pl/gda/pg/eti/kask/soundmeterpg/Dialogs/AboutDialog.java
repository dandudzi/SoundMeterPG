package pl.gda.pg.eti.kask.soundmeterpg.Dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.LastDateException;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.VersionException;
import pl.gda.pg.eti.kask.soundmeterpg.InformationAboutThisApplication;
import pl.gda.pg.eti.kask.soundmeterpg.R;

/**
 * Created by Daniel on 04.08.2016 at 11:59 :) at 12:13 :).
 */
public class AboutDialog {

    public static AlertDialog create(Activity ownerDialog) throws VersionException, LastDateException {

        AlertDialog.Builder aboutDialog = new AlertDialog.Builder(ownerDialog);
        Context ownerContext = ownerDialog.getBaseContext();

        LayoutInflater inflater = ownerDialog.getLayoutInflater();

        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.about_dialog, null);

        String version = InformationAboutThisApplication.getVersionOfApplication(ownerContext);
        setTextView(dialogView,R.id.version_about_dialog,version );

        String lastBuild =  InformationAboutThisApplication.getLastDateBuildApplication(ownerContext);
        setTextView(dialogView,R.id.last_build_about_dialog,lastBuild );

        aboutDialog.setView(dialogView);
        aboutDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.i("AboutDialog","Press Ok");
            }
        });
        return aboutDialog.create();
    }

    private static void setTextView(View view, int dialogID, String text){
        TextView tmp = (TextView) view.findViewById(dialogID);
        tmp.setText(text);
    }

}
