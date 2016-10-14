package pl.gda.pg.eti.kask.soundmeterpg.Dialogs;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import pl.gda.pg.eti.kask.soundmeterpg.R;

/**
 * Created by Daniel on 04.08.2016 at 11:49 :) at 12:12 :).
 */
public class NoGPS {
    public static final int REQUEST_CODE_GPS =1994 ;

    public static AlertDialog create(final Activity ownerDialog, final Fragment ownerFragment){
        Intent intent =new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        InformationToCreateDialog info = new InformationToCreateDialog(intent,ownerDialog,ownerFragment,REQUEST_CODE_GPS, R.layout.no_gps_dialog);
        return SimpleDialogWithTextView.createDialogForResultOnYes(info);
    }
}
