package pl.gda.pg.eti.kask.soundmeterpg.Dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import pl.gda.pg.eti.kask.soundmeterpg.R;

/**
 * Created by Daniel on 04.08.2016 at 12:00 :) at 12:13 :).
 */
public class FAQ {
    public static AlertDialog create(Activity ownerDialog) {
        AlertDialog.Builder aboutDialog = new AlertDialog.Builder(ownerDialog);
        aboutDialog.setPositiveButton(null,null);
        LayoutInflater inflater = ownerDialog.getLayoutInflater();

        final AlertDialog faqDialog = aboutDialog.create();

        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.faq_dialog, null);

        TextView tmp = (TextView) dialogView.findViewById(R.id.question_5);
        if(tmp.getLinksClickable())
        tmp.setMovementMethod(LinkMovementMethod.getInstance());

        tmp = (TextView) dialogView.findViewById(R.id.question_6);
        tmp.setMovementMethod(LinkMovementMethod.getInstance());

        tmp = (TextView) dialogView.findViewById(R.id.question_7);
        tmp.setMovementMethod(LinkMovementMethod.getInstance());

        tmp = (TextView) dialogView.findViewById(R.id.question_8);
        tmp.setMovementMethod(LinkMovementMethod.getInstance());

        Button btnOK = (Button) dialogView.findViewById(R.id.ok_btn);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                faqDialog.dismiss();
            }
        });


        aboutDialog.setView(dialogView);
        aboutDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.i("FAQ Dialog","Press OK");
            }
        });
        faqDialog.setView(dialogView);
        return faqDialog;
    }
}
