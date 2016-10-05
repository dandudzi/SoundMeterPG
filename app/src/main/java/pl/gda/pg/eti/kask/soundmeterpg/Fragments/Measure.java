package pl.gda.pg.eti.kask.soundmeterpg.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import pl.gda.pg.eti.kask.soundmeterpg.IntentActionsAndKeys;
import pl.gda.pg.eti.kask.soundmeterpg.R;
import pl.gda.pg.eti.kask.soundmeterpg.Services.ServiceDetector;
import pl.gda.pg.eti.kask.soundmeterpg.Services.SampleCreator;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Sample;

/**
 * Created by Daniel on 10.08.2016 at 17:53 :).
 */
public class Measure extends Fragment{
    private Context context;

    private TextView currentNoiseLevel;
    private TextView currentLatitude;
    private TextView currentLongitude;
    private Button measureButton;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            IntentActionsAndKeys action =  IntentActionsAndKeys.valueOf(intent.getAction());
            switch (action){
                case SAMPLE_RECEIVE_ACTION:
                    Sample sample = intent.getExtras().getParcelable(IntentActionsAndKeys.SAMPLE_KEY.toString());
                    changeUIMeasurement(sample);
                    break;
                case ERROR_MEASURE_ACTION:
                    String key =  intent.getExtras().getString(IntentActionsAndKeys.ERROR_KEY.toString());
                    Measure.this.handleMeasureError(key);
            }
        }
    };

    private void handleMeasureError(String key) {
        currentNoiseLevel.setText(key);
    }

    private  void changeUIMeasurement(Sample sample) {
        currentNoiseLevel.setText(String.valueOf(sample.getNoiseLevel())+" db");
        currentLatitude.setText("Latitude : " + String.valueOf(sample.getLocation().getLatitude()));
        currentLongitude.setText("Longitude : " + String.valueOf(sample.getLocation().getLongitude()));
    }


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
        currentLatitude = (TextView) activity.findViewById(R.id.latitude_measure_fragment);
        currentLongitude = (TextView) activity.findViewById(R.id.longitude_measure_fragment);
        measureButton = (Button) activity.findViewById(R.id.measure_button_fragment);

        measureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMeasureButtonClick(v);
            }
        });
        IntentFilter filter =  new IntentFilter();
        filter.addAction(IntentActionsAndKeys.END_ACTION.toString());
        filter.addAction(IntentActionsAndKeys.ERROR_MEASURE_ACTION.toString());
        filter.addAction(IntentActionsAndKeys.SAMPLE_RECEIVE_ACTION.toString());
        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver, filter);
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
            Intent stopServiceIntent = new Intent(IntentActionsAndKeys.END_ACTION.toString());
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
