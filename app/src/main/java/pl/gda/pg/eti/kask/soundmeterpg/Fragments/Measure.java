package pl.gda.pg.eti.kask.soundmeterpg.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import pl.gda.pg.eti.kask.soundmeterpg.R;
import pl.gda.pg.eti.kask.soundmeterpg.Services.ServiceDetector;
import pl.gda.pg.eti.kask.soundmeterpg.Services.SampleCreator;

/**
 * Created by Daniel on 10.08.2016 at 17:53 :).
 */
public class Measure extends Fragment{

    private Context context;

    private TextView currentNoiseLevel;
    private Button measureButton;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            currentNoiseLevel.setText("Juz");
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
        currentNoiseLevel = (TextView) activity.findViewById(R.id.current_db_measure_fragment);
        measureButton = (Button) activity.findViewById(R.id.measure_button_fragment);

        measureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMeasureButtonClick(v);
            }
        });
        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver, new IntentFilter("custom-event-name"));
    }

    @Override
    public void onResume() {
        boolean isMeasureServiceRunning = isMeasureServiceRunning();
        if(isMeasureServiceRunning){
            measureButton.setText("Stop");
        }else{
            measureButton.setText("Start");
            currentNoiseLevel.setText("0 db");
        }

        super.onResume();
    }


    public void onMeasureButtonClick(View view){
        boolean isMeasureServiceRunning = isMeasureServiceRunning();
        if(isMeasureServiceRunning){
            measureButton.setText("Start");
            currentNoiseLevel.setText("0 db");
            Intent stopServiceIntent = new Intent(pl.gda.pg.eti.kask.soundmeterpg.Services.Measure.END_ACTION);
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(stopServiceIntent);
        }else{
            measureButton.setText("Stop");
            Intent intent = new Intent(getActivity(), pl.gda.pg.eti.kask.soundmeterpg.Services.Measure.class);
            getActivity().startService(intent);

        }


    }

    private boolean isMeasureServiceRunning() {
        return ServiceDetector.isMyServiceRunning(pl.gda.pg.eti.kask.soundmeterpg.Services.Measure.class, context);
    }
}
