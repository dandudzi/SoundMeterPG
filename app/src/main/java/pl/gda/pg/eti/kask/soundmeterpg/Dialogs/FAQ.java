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

import pl.gda.pg.eti.kask.soundmeterpg.Activities.MainActivity;
import pl.gda.pg.eti.kask.soundmeterpg.R;

/**
 * Created by Daniel on 04.08.2016 at 12:00 :) at 12:13 :).
 */
public class FAQ {
    public static AlertDialog create(Activity ownerDialog) {
        AlertDialog.Builder aboutDialog = new AlertDialog.Builder(ownerDialog);

        LayoutInflater inflater = ownerDialog.getLayoutInflater();

        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.faq_dialog, null);

        TextView tmp = (TextView) dialogView.findViewById(R.id.github_hyperlink_text_view_faq_dialog);
        tmp.setMovementMethod(LinkMovementMethod.getInstance());

        tmp = (TextView) dialogView.findViewById(R.id.help_hyperlink_text_view_faq_dialog);
        tmp.setMovementMethod(LinkMovementMethod.getInstance());

        tmp = (TextView) dialogView.findViewById(R.id.soundmeterpg_hyperlink_text_view_faq_dialog);
        tmp.setMovementMethod(LinkMovementMethod.getInstance());

        tmp = (TextView) dialogView.findViewById(R.id.licence_hyperlink_text_view_faq_dialog);
        tmp.setMovementMethod(LinkMovementMethod.getInstance());

        aboutDialog.setView(dialogView);
        aboutDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.i("FAQ Dialog","Press OK");
            }
        });
        return aboutDialog.create();
    }
}
