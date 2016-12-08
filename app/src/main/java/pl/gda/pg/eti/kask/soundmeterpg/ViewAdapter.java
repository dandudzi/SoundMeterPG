package pl.gda.pg.eti.kask.soundmeterpg;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import java.util.ArrayList;

import pl.gda.pg.eti.kask.soundmeterpg.Database.DataBaseHandler;
import pl.gda.pg.eti.kask.soundmeterpg.Database.MeasurementDataBaseObject;
import pl.gda.pg.eti.kask.soundmeterpg.Interfaces.PreferenceManager;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.FakeLocation;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Measurement;

/**
 * Created by gierl on 28.09.2016.
 */
public class ViewAdapter extends ArrayAdapter<MeasurementDataBaseObject> implements  CompoundButton.OnCheckedChangeListener{
    private  final Context context;
    private  ArrayList<MeasurementDataBaseObject> measurementArrayList;
    private SharedPreferences sharedPreferences;
    private  DataBaseHandler dataBaseHandler;
    public ViewAdapter(Context context, ArrayList<MeasurementDataBaseObject> measurements) {
        super(context, R.layout.samples_item, measurements);
        this.measurementArrayList = measurements;
        this.context = context;
        this.sharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        dataBaseHandler = new DataBaseHandler(context, context.getResources().getString(R.string.database_name));
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.samples_item, parent, false);
        TextView tvAVG = (TextView) rowView.findViewById(R.id.decibels);
        TextView tvLatitude = (TextView) rowView.findViewById(R.id.dateMeasurement);
        CheckBox cbStoredSample = (CheckBox) rowView.findViewById(R.id.stored_measurement);
        MeasurementDataBaseObject measurement = getItem(position);
        tvAVG.setText( String.valueOf(measurement.getAvg()));
        tvLatitude.setText(measurement.getDate());
        if(measurement.getStoredState())
            cbStoredSample.setChecked(true);
        cbStoredSample.setTag(measurement.getID());
        cbStoredSample.setOnCheckedChangeListener(this);

        return rowView;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        Log.i("Main Adapter", "value = " + b);
        MeasurementDataBaseObject measurement = dataBaseHandler.getMeasurement((int)compoundButton.getTag());


        int position = -1;
        for(int i  = 0; i<measurementArrayList.size(); i++){
            if(measurement.getID() == measurementArrayList.get(i).getID())
                position = i;
        }

        if(measurement.getLocation().equals(new FakeLocation())) {
            createAlert("Cannot store measurement which not have real location !\"");
            measurement.storedOnWebServer = false;
        }
        else {

            String user = sharedPreferences.getString(context.getResources().getString(R.string.login_key),"NOUSER");
            if(user.equals("NOUSER")){
                createAlert("Cannot store measurement which not have user !\"");
            }
            else {

                dataBaseHandler.changeStoreOnServerFlag((int)compoundButton.getTag(), b);
                measurementArrayList.get(position).storedOnWebServer = b;
            }
        }
        notifyDataSetChanged();


    }

    private void createAlert(String msg) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage(msg);
        dialog.create();
        dialog.show();
    }
}
