package pl.gda.pg.eti.kask.soundmeterpg;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

import pl.gda.pg.eti.kask.soundmeterpg.Database.DataBaseHandler;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.InsufficientInternalStoragePermissionsException;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.FakeLocation;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Location;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Measurement;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.MeasurementStatistics;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.PreferenceParser;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Sample;

/**
 * Created by Daniel on 06.10.2016 at 13:25 :).
 */

public class MeasurementDataBaseManager {
    private static final int MINUTES_PER_MEASUREMENT = 1;
    private static final int MINUTE_IN_MILLISECONDS = 60000;
    private ArrayList<Sample> list  = new ArrayList<>();
    private long startTime = -1;
    private PreferenceParser preference;

    public MeasurementDataBaseManager(Context context, PreferenceParser preference){
        this.context = context;
        this.preference = preference;
    }

    public void sendToDataBase(Sample sample){
        if(startTime == -1)
            startTime = System.currentTimeMillis();

        list.add(sample);
        //Log.i("Sample", sample.toString() + " counter: " + list.size());

        long currentTime = System.currentTimeMillis();
        if((currentTime - startTime) >= (MINUTES_PER_MEASUREMENT * MINUTE_IN_MILLISECONDS)){
            startTime = currentTime;
            flush();
        }
    }

    public void flush(){
        MeasurementStatistics avg = Measurement.calculateMeasureStatistics(list);
        Location location;
        boolean isStoreOnWebServer = false;
        if(list.isEmpty())
            return;

        if(preference.hasPermissionToUseGPS() )
            location =  list.get(0).getLocation();
        else
            location = new FakeLocation();

        //TODO is logIN
        if(preference.hasPermissionToUseInternet())
            isStoreOnWebServer = true;

        Date date =  new Date();
        Measurement measurement =  new Measurement(avg, location, isStoreOnWebServer, date);
        list.clear();

        //TODO sendTOSERVER
        DataBaseHandler dataBaseHandler =new DataBaseHandler(context, context.getResources().getString(R.string.table));
        try {
            dataBaseHandler.insert(measurement);
        } catch (InsufficientInternalStoragePermissionsException e) {
            e.printStackTrace();
        }
        dataBaseHandler.close();
        Log.i("Measurement", measurement.toString());
    }

}
