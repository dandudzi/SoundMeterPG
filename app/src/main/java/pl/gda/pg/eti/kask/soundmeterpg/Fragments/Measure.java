package pl.gda.pg.eti.kask.soundmeterpg.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import pl.gda.pg.eti.kask.soundmeterpg.Dialogs.SimpleDialogWithTextView;
import pl.gda.pg.eti.kask.soundmeterpg.IntentActionsAndKeys;
import pl.gda.pg.eti.kask.soundmeterpg.MutableInteger;
import pl.gda.pg.eti.kask.soundmeterpg.R;
import pl.gda.pg.eti.kask.soundmeterpg.Services.ServiceDetector;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.FakeLocation;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.MeasureStatistic;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.MeasurementStatistics;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.PreferenceParser;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Sample;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.SexigesimalLocation;

/**
 * Created by Daniel on 10.08.2016 at 17:53 :).
 */
public class Measure extends Fragment{
    private Context context;


    private TextView latitude;
    private TextView longitude;
    private TextView min;
    private TextView max;
    private TextView avg;
    private TextView currentNoiseLevel;
    private MutableInteger counterSampleAvg = new MutableInteger();
    private MeasurementStatistics statistic;

    private PreferenceParser preferences;
    private Button measureButton;
    private PowerManager.WakeLock lock;

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
                    break;
                case ON_BACKGROUND_END_ACTION:
                    synchronized (statistic) {
                        onEndOfBackgroundWork(intent);
                    }
                    break;
            }
        }
    };

    public void onEndOfBackgroundWork(Intent intent) {
        MeasurementStatistics statistic = intent.getExtras().getParcelable(IntentActionsAndKeys.MEASUREMENT_STATISTICS_KEY.toString());
        int counter = intent.getExtras().getInt(IntentActionsAndKeys.COUNTER_KEY.toString());
        if((this.statistic.avg + statistic.avg) < 0){
            this.statistic.avg /= counterSampleAvg.value;
            counterSampleAvg.value = 1;
        }
        this.statistic.avg += statistic.avg;
        if(statistic.min > 0 && this.statistic.min > statistic.min)
            this.statistic.min = statistic.min;
        if(this.statistic.max < statistic.max)
            this.statistic.max = statistic.max;
        counterSampleAvg.value += counter;
    }

    private void handleMeasureError(String _key) {
        if(lock.isHeld())
            lock.release();

        measureButton.setText("Start");
        currentNoiseLevel.setText("0 db");

        IntentActionsAndKeys key = IntentActionsAndKeys.valueOf(_key);
        if(isAdded())
        switch (key){
            case MICROPHONE_ERROR_KEY:
                AlertDialog dialog = SimpleDialogWithTextView.createDialog(getString(R.string.turn_off_microphone_msg_measure),this.getActivity(),getString(R.string.turn_off_microphone_title_measure));
                dialog.show();
                break;
            case GPS_TURN_OFF_KEY:
                dialog = SimpleDialogWithTextView.createDialog(getString(R.string.turn_off_location_msg_measure),this.getActivity(),getString(R.string.turn_off_location_title_measure));
                dialog.show();
                break;
            case INTERNAL_UNKNOWN_ERROR:
                dialog = SimpleDialogWithTextView.createDialog(getString(R.string.unknown_error_msg_measure),this.getActivity(),getString(R.string.unknown_error_title_measure));
                dialog.show();
                break;
        }
    }

    private  void changeUIMeasurement(Sample sample) {
        int noiseLevel =sample.getNoiseLevel();
        int avg;
        synchronized (statistic) {
            avg = MeasureStatistic.setUpStatistic(noiseLevel, statistic, counterSampleAvg);
        }
        currentNoiseLevel.setText(String.valueOf(noiseLevel)+" db");
        min.setText(String.valueOf(statistic.min) +" db");
        max.setText(String.valueOf(statistic.max) +" db");
        this.avg.setText(String.valueOf(avg) +" db");

        if(sample.getLocation() instanceof FakeLocation)
            return;

        SexigesimalLocation location = sample.getLocation().convertLocation();
        String latitude = MeasureStatistic.getLatitude(location);
        String longitude = MeasureStatistic.getLongitude(location);

        this.latitude.setText(latitude);
        this.longitude.setText(longitude);
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
        setUpViewContent(activity);
        preferences = new PreferenceParser(context);
        measureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMeasureButtonClick(v);
            }
        });
        statistic =  new MeasurementStatistics();
        setUpFilters();
        setUpPowerManager();
        changeUiDependOnServiceRunning();
    }

    public void setUpViewContent(Activity activity) {
        currentNoiseLevel = (TextView) activity.findViewById(R.id.current_db_measure_fragment);
        measureButton = (Button) activity.findViewById(R.id.measure_button_fragment);
        min = (TextView) activity.findViewById(R.id.min_measure);
        max = (TextView) activity.findViewById(R.id.max_measure);
        avg = (TextView) activity.findViewById(R.id.avg_measure);
        latitude = (TextView) activity.findViewById(R.id.latitude_measure);
        longitude = (TextView) activity.findViewById(R.id.longitude_measure);
    }

    public void setUpFilters() {
        IntentFilter filter =  new IntentFilter();
        filter.addAction(IntentActionsAndKeys.ERROR_MEASURE_ACTION.toString());
        filter.addAction(IntentActionsAndKeys.SAMPLE_RECEIVE_ACTION.toString());
        filter.addAction(IntentActionsAndKeys.ON_BACKGROUND_END_ACTION.toString());
        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver, filter);
    }

    public void setUpPowerManager() {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        lock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Tag");
    }

    @Override
    public void onResume() {
        changeUiDependOnServiceRunning();
        stopBackgroundService();
        super.onResume();
    }


    public void changeUiDependOnServiceRunning() {
        boolean isMeasureServiceRunning = isMeasureServiceRunning();
        if(isMeasureServiceRunning){
            lock.acquire();
            measureButton.setText("Stop");
        }else{
            measureButton.setText("Start");
            currentNoiseLevel.setText("0 db");
        }
    }

    @Override
    public void onPause() {
        if(lock.isHeld())
            lock.release();
        startBackgroundService();
        super.onPause();
    }

    public void onMeasureButtonClick(View view){
        boolean isMeasureServiceRunning = isMeasureServiceRunning();
        if(isMeasureServiceRunning){
            measureButton.setText("Start");
            sendEndActionToMeasureService();
            if(lock.isHeld())
                lock.release();
        }else{
            measureButton.setText("Stop");
            Intent intent = new Intent(getActivity(), pl.gda.pg.eti.kask.soundmeterpg.Services.Measure.class);
            getActivity().startService(intent);
            lock.acquire();
        }

    }

    public void sendEndActionToMeasureService() {
        statistic = new MeasurementStatistics();
        counterSampleAvg = new MutableInteger();
        Intent stopServiceIntent = new Intent(IntentActionsAndKeys.END_ACTION.toString());
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(stopServiceIntent);
    }

    private boolean isMeasureServiceRunning() {
        return ServiceDetector.isMyServiceRunning(pl.gda.pg.eti.kask.soundmeterpg.Services.Measure.class, context);
    }

    public void startBackgroundService() {
        if(canStartBackgroundWork()){
            Intent intent = new Intent(getActivity(), pl.gda.pg.eti.kask.soundmeterpg.Services.BackgroundWork.class);
            intent.putExtra(IntentActionsAndKeys.MEASUREMENT_STATISTICS_KEY.toString(),statistic);
            intent.putExtra(IntentActionsAndKeys.COUNTER_KEY.toString(),counterSampleAvg.value);
            getActivity().startService(intent);
        }else if(!preferences.hasPermissionToWorkInBackground()){
            sendEndActionToMeasureService();
        }
    }

    public boolean canStartBackgroundWork() {
        return preferences.hasPermissionToWorkInBackground()
                && !ServiceDetector.isMyServiceRunning(pl.gda.pg.eti.kask.soundmeterpg.Services.BackgroundWork.class, context)
                && ServiceDetector.isMyServiceRunning(pl.gda.pg.eti.kask.soundmeterpg.Services.Measure.class, context);
    }

    private void stopBackgroundService() {
        if(ServiceDetector.isMyServiceRunning(pl.gda.pg.eti.kask.soundmeterpg.Services.BackgroundWork.class, context)){
            Intent stopServiceIntent = new Intent(IntentActionsAndKeys.END_ACTION_WORKING_BACKGROUND.toString());
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(stopServiceIntent);
        }
    }
}
