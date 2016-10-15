package pl.gda.pg.eti.kask.soundmeterpg.Dialogs;

/**
 * Created by gierl on 14.10.2016.
 */

public class MoreInformationMeasurement {
}
/*
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

import pl.gda.pg.eti.kask.soundmeterpg.R;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Measurement;


 * Created by gierl on 13.10.2016.


public class MenuMeasurement implements  DialogInterface.OnClickListener{
    private Measurement measurement;
    private Activity ownerDialog;
    public MenuMeasurement(Activity ownerDialog, Measurement measurement){
        this.measurement = measurement;
        this.ownerDialog = ownerDialog;
    }

    public AlertDialog create() {
        AlertDialog.Builder menuDialog = new AlertDialog.Builder(ownerDialog);

        menuDialog.setTitle("Select option :");
        menuDialog.setItems(R.array.menuMeasurement, (DialogInterface.OnClickListener) this);
        // menuDialog.setItems(Integer.parseInt("More info"), this);


        return menuDialog.create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int position) {
        switch (position){
            case 0:
        }
    }
}
 */