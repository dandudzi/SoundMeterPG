package pl.gda.pg.eti.kask.soundmeterpg;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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
    private static final int MINUTES_PER_MEASUREMENT = 5;
    private static final int MINUTE_IN_MILLISECONDS = 60000;
    private static final int HALF_TIME_IN_SECONDS = (int) (TimeUnit.MILLISECONDS.toSeconds(MINUTE_IN_MILLISECONDS * MINUTES_PER_MEASUREMENT)/2);
    private static final double CONST_VALUE = 0.01;
    private static final int X_MIN = 0;
    private static final int X_MAX = 1000;
    private ArrayList<Sample> list  = new ArrayList<>();
    private long startTime = -1;
    private PreferenceParser preference;
    private Context context;

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
        long currentTime = System.currentTimeMillis();
        long diffTime = currentTime - startTime;
        int x = (int) TimeUnit.MILLISECONDS.toSeconds(diffTime);



        double y=0;
        y = functionToCalculateWeight(x);
        int wynik_normalizacji =  normalization(y);



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
        //Trzeba sprawdzac czy lokacja fake, jezeli tak to nie moze tego wysyłąć na serwer.
        if(preference.hasPermissionToSendToServer() && !(location instanceof FakeLocation))
            isStoreOnWebServer = true;

        Date date =  new Date();
        Measurement measurement =  new Measurement(avg, location, isStoreOnWebServer, date);
        Log.i("MeasureService","Counter samples : " + list.size());
        list.clear();

        //TODO sendTOSERVER
        DataBaseHandler dataBaseHandler =new DataBaseHandler(context, context.getResources().getString(R.string.database_name));
        try {
            dataBaseHandler.insert(measurement);
        } catch (InsufficientInternalStoragePermissionsException e) {
            e.printStackTrace();
        }
        dataBaseHandler.close();
        Log.i("Measurement", measurement.toString());


    }

    private  double functionToCalculateWeight(int x) {
        double value= 0.0;
        if (x < HALF_TIME_IN_SECONDS)
           value = 1 / (double)(HALF_TIME_IN_SECONDS - x) - (1 / (double)HALF_TIME_IN_SECONDS);
        else if (x == HALF_TIME_IN_SECONDS)
            value =  1 / ((double)HALF_TIME_IN_SECONDS - (x - 1)) - (1 / (double)HALF_TIME_IN_SECONDS) + (double)CONST_VALUE;
        else {
            double middle_value = 1 / ((double)HALF_TIME_IN_SECONDS - ((double)HALF_TIME_IN_SECONDS - 1)) - (1 / (double)HALF_TIME_IN_SECONDS) + (double)CONST_VALUE;
            value = middle_value + (double)CONST_VALUE * (double)(x - HALF_TIME_IN_SECONDS);
        }
        return  value;
    }
    private  int normalization(double value) {

        double middle_value = (double)1 / ((double) HALF_TIME_IN_SECONDS - ((double) HALF_TIME_IN_SECONDS - 1)) - (1 / (double) HALF_TIME_IN_SECONDS) + (double) CONST_VALUE;
        double MIN = (double) 1/(HALF_TIME_IN_SECONDS -1) - (double) 1 / HALF_TIME_IN_SECONDS;
        double MAX = middle_value + (double) CONST_VALUE * (double) (HALF_TIME_IN_SECONDS );
        int NEW_MIN = 1;
        int NEW_MAX = 1000;
        Double wynik = (value - MIN) / (MAX - MIN) * (double)(NEW_MAX - NEW_MIN) +(double)NEW_MIN;
        return wynik.intValue();
    }
}
