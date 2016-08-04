package pl.gda.pg.eti.kask.soundmeterpg.Dialogs;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;

/**
 * Created by Daniel on 04.08.2016 at 11:51 :).
 */
class InformationToCreateDialog{
    final int layout;
    final int requestCode;
    final Intent intent;
    final Activity ownerDialog;
    final Fragment ownerFragment;

    public InformationToCreateDialog(Intent intent, Activity ownerDialog, Fragment ownerFragment, int requestCode, int layout) {
        this.intent = intent;
        this.ownerDialog = ownerDialog;
        this.ownerFragment = ownerFragment;
        this.requestCode = requestCode;
        this.layout = layout;
    }


}
