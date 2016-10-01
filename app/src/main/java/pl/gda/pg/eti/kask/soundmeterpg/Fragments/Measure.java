package pl.gda.pg.eti.kask.soundmeterpg.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.InsufficientPermissionsException;
import pl.gda.pg.eti.kask.soundmeterpg.Interfaces.MeasurementChangeListener;
import pl.gda.pg.eti.kask.soundmeterpg.R;
import pl.gda.pg.eti.kask.soundmeterpg.Sample;
import pl.gda.pg.eti.kask.soundmeterpg.ServiceDetector;
import pl.gda.pg.eti.kask.soundmeterpg.Services.SampleCreator;

/**
 * Created by Daniel on 10.08.2016 at 17:53 :).
 */
public class Measure extends Fragment implements MeasurementChangeListener{

    private Context context;

    private TextView currentDB;
    private Button measureButton;

    private SampleCreator measureService;
    private ServiceConnection localizationConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            SampleCreator.LocalBinder binder = (SampleCreator.LocalBinder) service;
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(binder == null)
                binder = (SampleCreator.LocalBinder) service;
            measureService = binder.getService();
            measureService.setOnMeasurementChangeListener(Measure.this);
            try {
                measureService.start();
            } catch (InsufficientPermissionsException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            measureService.stop();
            measureService = null; //todo mozliwe wyscigi czy to przypisanie jest przed kompletnym zabiciem serwisu
        }
    };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.measure, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity = getActivity();
        context = activity.getBaseContext();
        currentDB = (TextView) activity.findViewById(R.id.current_db_measure_fragment);
        measureButton = (Button) activity.findViewById(R.id.measure_button_fragment);

        measureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMeasureButtonClick(v);
            }
        });
    }

    @Override
    public void onResume() {
        boolean isMeasureServiceRunning = isMeasureServiceRunning();
        if(isMeasureServiceRunning){
            measureButton.setText("Stop");
        }else{
            measureButton.setText("Start");
            currentDB.setText("0 db");
        }

        super.onResume();
    }

    @Override
    public void onMeasurementChange(Sample newSample) {
        int db = (int)newSample.getAvgNoiseLevel();
        final String msg = db +" db";

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                currentDB.setText(msg);
            }
        });

    }


    public void onMeasureButtonClick(View view){
        boolean isMeasureServiceRunning = isMeasureServiceRunning();
        if(isMeasureServiceRunning){

            context.unbindService(localizationConnection);
            measureButton.setText("Start");
            currentDB.setText("0 db");
        }else{
            measureButton.setText("Stop");
            Intent intent = new Intent(getActivity().getBaseContext(), SampleCreator.class);
            getActivity().getBaseContext().bindService(intent, localizationConnection, Context.BIND_AUTO_CREATE);
        }


    }

    private boolean isMeasureServiceRunning() {
        return ServiceDetector.isMyServiceRunning(SampleCreator.class, context);
    }
}
