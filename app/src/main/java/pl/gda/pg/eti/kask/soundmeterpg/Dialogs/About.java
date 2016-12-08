package pl.gda.pg.eti.kask.soundmeterpg.Dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.LastDateException;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.VersionException;
import pl.gda.pg.eti.kask.soundmeterpg.InformationAboutThisApplication;
import pl.gda.pg.eti.kask.soundmeterpg.R;

/**
 * Created by Daniel on 04.08.2016 at 11:59 :) at 12:13 :).
 */
public class About {

    public static AlertDialog create(final Activity ownerDialog) throws VersionException, LastDateException {

        AlertDialog.Builder aboutDialog = new AlertDialog.Builder(ownerDialog);
        aboutDialog.setPositiveButton(null, null);
        aboutDialog.setNegativeButton(null,null);
        final Context ownerContext = ownerDialog.getBaseContext();

        LayoutInflater inflater = ownerDialog.getLayoutInflater();

        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.about_dialog, null);

        final AlertDialog alertDialog = aboutDialog.create();

        Button btnOK = (Button) dialogView.findViewById(R.id.ok_btn);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        TextView contact = (TextView) dialogView.findViewById(R.id.contact_about_dialog);
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sendMail(ownerDialog);
               /* Intent gmail = new Intent(Intent.ACTION_VIEW);
                gmail.setClassName("com.google.android.gm","com.google.android.gm.ComposeActivityGmail");
                gmail.putExtra(Intent.EXTRA_EMAIL, new String[] { "jckdsilva@gmail.com" });
                gmail.setData(Uri.parse("jckdsilva@gmail.com"));
                gmail.putExtra(Intent.EXTRA_SUBJECT, "enter something");
                gmail.setType("plain/text");
                gmail.putExtra(Intent.EXTRA_TEXT, "hi android jack!");
                gmail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ownerContext.startActivity(gmail);*/
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"soudmeterpg@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Question to you");
                i.putExtra(Intent.EXTRA_TEXT   , "I have question to you : ");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    ownerDialog.startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

                String version = InformationAboutThisApplication.getVersionOfApplication(ownerContext);
        setTextView(dialogView,R.id.version_about_dialog,version );

        String lastBuild =  InformationAboutThisApplication.getLastDateBuildApplication(ownerContext);
        setTextView(dialogView,R.id.last_build_about_dialog,lastBuild );

        alertDialog.setView(dialogView);
        return alertDialog;
    }



    private static void setTextView(View view, int dialogID, String text){
        TextView tmp = (TextView) view.findViewById(dialogID);
        tmp.setText(text);
    }

}
