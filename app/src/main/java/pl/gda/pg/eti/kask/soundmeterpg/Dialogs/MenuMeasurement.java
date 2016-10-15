package pl.gda.pg.eti.kask.soundmeterpg.Dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import pl.gda.pg.eti.kask.soundmeterpg.Database.DataBaseHandler;
import pl.gda.pg.eti.kask.soundmeterpg.Database.MeasurementDataBaseObject;
import pl.gda.pg.eti.kask.soundmeterpg.Fragments.Measurements;
import pl.gda.pg.eti.kask.soundmeterpg.MainAdapter;
import pl.gda.pg.eti.kask.soundmeterpg.R;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.FakeLocation;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.MeasureStatistic;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Measurement;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.SexigesimalLocation;

import static pl.gda.pg.eti.kask.soundmeterpg.R.id.measurementInfo_avg;
import static pl.gda.pg.eti.kask.soundmeterpg.R.id.measurementInfo_date;
import static pl.gda.pg.eti.kask.soundmeterpg.R.id.measurementInfo_latitude;
import static pl.gda.pg.eti.kask.soundmeterpg.R.id.measurementInfo_longitude;
import static pl.gda.pg.eti.kask.soundmeterpg.R.id.measurementInfo_max;
import static pl.gda.pg.eti.kask.soundmeterpg.R.id.measurementInfo_stored;

/**
 * Created by gierl on 13.10.2016.
 */

public class MenuMeasurement implements  DialogInterface.OnClickListener {
    private Activity ownerDialog;
    private  MainAdapter mainAdapter;
    private  int position;
    private static final String SUCCESS = "Succesfuly deleted !";
    private static final String ERROR = "Can not delete object.";
    public MenuMeasurement(Activity ownerDialog, MainAdapter adapter, int position){
        this.ownerDialog = ownerDialog;
        this.mainAdapter = adapter;
        //ListView numeruje od 1 a adapter od 0 stąd to odejmowanie
        if(position >0)
            this.position = position -1;
        else
            this.position = position;
    }

    public AlertDialog create() {
        AlertDialog.Builder menuDialog = new AlertDialog.Builder(ownerDialog);
        menuDialog.setTitle("Select option :");
        menuDialog.setItems(R.array.menuMeasurement, this);
        return menuDialog.create();
    }


    private AlertDialog createMoreInfoView(){
        AlertDialog.Builder menuDialog = new AlertDialog.Builder(ownerDialog);
        LayoutInflater inflater = ownerDialog.getLayoutInflater();
        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.info_measurement, null);
        initializeMeasurementInfo(dialogView);
        menuDialog.setView(dialogView);
        menuDialog.setCancelable(true);
        return menuDialog.create();
    }



    @Override
    public void onClick(DialogInterface dialogInterface, int option) {
        switch (option) {
            case 0:
                this.createMoreInfoView().show();
                break;
            case 1:
                final MeasurementDataBaseObject measurement = mainAdapter.getItem(position);
                AlertDialog alert = createQuestionBeforeDelete(measurement);
                alert.show();
                break;
        }
    }

    private AlertDialog createQuestionBeforeDelete(final MeasurementDataBaseObject measurement) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ownerDialog);
        builder.setTitle("Are you sure?");
        builder.setMessage("Measurement will be destroyed.");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        removeMeasurement(measurement);
                    }
                });
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }

    private void removeMeasurement(MeasurementDataBaseObject measurement) {
        DataBaseHandler dataBaseHandler = new DataBaseHandler(ownerDialog, ownerDialog.getResources().getString(R.string.database_name));
        AlertDialog.Builder dialog = new AlertDialog.Builder(ownerDialog);
        int id = measurement.getID();
        if(dataBaseHandler.erease(measurement.getID()))
            dialog.setMessage(SUCCESS);
        else
            dialog.setMessage(ERROR);
        dialog.create();
        dialog.show();
        mainAdapter.remove(measurement);

    }

    private void initializeMeasurementInfo(View dialogView) {

        MeasurementDataBaseObject measurement = mainAdapter.getItem(position);

            TextView tmp = (TextView) dialogView.findViewById(R.id.measurementInfo_min);
            tmp.setText(tmp.getText() + String.valueOf(measurement.getMin()));

            tmp = (TextView) dialogView.findViewById(measurementInfo_max);
            tmp.setText(tmp.getText() + String.valueOf(measurement.getMax()));

            tmp = (TextView) dialogView.findViewById(measurementInfo_avg);
            tmp.setText(tmp.getText() + String.valueOf(measurement.getAvg()));
        if(!(measurement.getLocation() instanceof FakeLocation)) {
            tmp = (TextView) dialogView.findViewById(measurementInfo_latitude);
            SexigesimalLocation location = measurement.getLocation().convertLocation();
            String lat = measurement.getLocation().getLatitude()>0 ? "N" : "S";
            String lon = measurement.getLocation().getLongitude()>0 ?"E" : "W";

            tmp.setText(tmp.getText() + MeasureStatistic.getLatitude(location) +lat);


            tmp = (TextView) dialogView.findViewById(measurementInfo_longitude);
            tmp.setText(tmp.getText() + MeasureStatistic.getLongitude(location) +lon);
        }
            tmp = (TextView) dialogView.findViewById(measurementInfo_date);
            tmp.setText(tmp.getText() + measurement.getDate());

            tmp = (TextView) dialogView.findViewById(measurementInfo_stored);
            if (measurement.getStoredState())
                tmp.setText(tmp.getText() + "yes");
            else
                tmp.setText(tmp.getText() + "no");
    }
}
