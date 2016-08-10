package pl.gda.pg.eti.kask.soundmeterpg.Dialogs;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import pl.gda.pg.eti.kask.soundmeterpg.R;

/**
 * Created by Daniel on 04.08.2016 at 11:54 :) at 12:12 :).
 */
public class NoInternetDialog {
    public static final int REQUEST_CODE_INTERNET =4991 ;

    public static AlertDialog create(final Activity ownerDialog, final Fragment ownerFragment){
        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        InformationToCreateDialog info = new InformationToCreateDialog(intent,ownerDialog,ownerFragment,REQUEST_CODE_INTERNET, R.layout.no_internet_dialog);
        return SimpleDialogWithTextView.createDialog(info);
    }
}
