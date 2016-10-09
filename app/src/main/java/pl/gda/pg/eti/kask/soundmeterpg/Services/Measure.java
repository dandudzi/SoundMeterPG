package pl.gda.pg.eti.kask.soundmeterpg.Services;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.support.v4.content.LocalBroadcastManager;

import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.InsufficientMicrophonePermissionsException;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.InsufficientPermissionsException;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.TurnOffGPSException;
import pl.gda.pg.eti.kask.soundmeterpg.IntentActionsAndKeys;
import pl.gda.pg.eti.kask.soundmeterpg.MeasurementDataBaseManager;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.AudioRecorder;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.FakeLocation;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Location;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.PreferenceParser;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Sample;

/**
 * Created by gierl on 01.10.2016.
 */

public class Measure extends IntentService {
    private static final int SAMPLE_PER_MILLISECONDS = 1000;

    private volatile boolean endMeasure = false;

    private GoogleAPILocalization googleAPILocalization;
    private Sender sender;

    private ServiceConnection localizationConnection = new GoogleApiServiceConnection(this);
    private ServiceConnection senderConnection= new SenderServiceConnection(this);
    private Thread binderThread;

    private MeasurementDataBaseManager dataBaseManger;
    private PreferenceParser preferences;
    private AudioRecorder recorder;





    private BroadcastReceiver endTaskReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            IntentActionsAndKeys action =  IntentActionsAndKeys.valueOf(intent.getAction());
                switch(action){
                    case END_ACTION:
                        synchronized (Measure.this) {
                            endMeasure =  true;
                        }
                        break;
                }
        }
    };

    public Measure() {
        super("Measure");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        LocalBroadcastManager.getInstance(this).registerReceiver(endTaskReceiver, new IntentFilter(IntentActionsAndKeys.END_ACTION.toString()));
        bindServices();
        setUpThreads();
        preferences = new PreferenceParser(this);
        dataBaseManger =  new MeasurementDataBaseManager(preferences);
        binderThread.start();
    }

    private void bindServices() {
        Intent googleIntent = new Intent(getBaseContext(),GoogleAPILocalization.class);
        Intent senderIntent = new Intent(getBaseContext(),Sender.class);
        getBaseContext().bindService(googleIntent, localizationConnection, Context.BIND_AUTO_CREATE);
        getBaseContext().bindService(senderIntent, senderConnection, Context.BIND_AUTO_CREATE);
    }

    private void setUpThreads() {
        binderThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (sender != null && googleAPILocalization != null)
                        break;
                }
            }
        });
    }

    @Override
    protected void onHandleIntent(Intent _intent) {
        waitForBindAllService();

        try {
            initializeAudioRecorder();
            while (!endMeasure) {
                measureAction();
            }
        }catch (Exception e) {
            handleMeasureError(e);
        }
    }

    private void waitForBindAllService() {
        try {
            binderThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void initializeAudioRecorder() throws InsufficientMicrophonePermissionsException {
        synchronized (this) {
            if(preferences.hasPermissionToUseMicrophone()){
                recorder = new AudioRecorder(getBaseContext());
            }else{
                throw new InsufficientMicrophonePermissionsException("There is not permission to use microphone");
            }

        }
    }

    private void measureAction() throws InsufficientPermissionsException, TurnOffGPSException {
        long startTime,endTime;
        try {
            startTime = System.currentTimeMillis();

                measure();

            endTime = System.currentTimeMillis();
            Thread.sleep(SAMPLE_PER_MILLISECONDS - (endTime - startTime));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private synchronized void measure() throws InsufficientPermissionsException, TurnOffGPSException {
        Sample sample = null;
        sample = measureSample();
        sendSampleToUI(sample);
        dataBaseManger.sendToDataBase(sample);
    }

    private void handleMeasureError(Exception e) {
        Intent intent = new Intent(IntentActionsAndKeys.ERROR_MEASURE_ACTION.toString());

        if(e instanceof  InsufficientMicrophonePermissionsException)
            intent.putExtra(IntentActionsAndKeys.ERROR_KEY.toString(), IntentActionsAndKeys.MICROPHONE_ERROR_KEY.toString());
        if(e instanceof  TurnOffGPSException)
            intent.putExtra(IntentActionsAndKeys.ERROR_KEY.toString(), IntentActionsAndKeys.GPS_TURN_OFF_KEY.toString());

        LocalBroadcastManager.getInstance(Measure.this).sendBroadcast(intent);
    }

    private  Sample measureSample() throws InsufficientPermissionsException, TurnOffGPSException {
        Location currentLocation = null;
        int noiseLevel = 0;

        isMicrophoneAvailable();
        noiseLevel = recorder.getNoiseLevel();

        if(preferences.hasPermissionToUseGPS()) {
            currentLocation = googleAPILocalization.getLocation();
        }
        if(currentLocation == null){
            currentLocation = new FakeLocation();
        }
        return new Sample(noiseLevel,currentLocation);
    }

    private void isMicrophoneAvailable() throws InsufficientMicrophonePermissionsException {
        if(!preferences.hasPermissionToUseMicrophone())
            throw new InsufficientMicrophonePermissionsException("There is not permission to use microphone");
    }

    private void sendSampleToUI(Sample sample) {
        Intent intent = new Intent(IntentActionsAndKeys.SAMPLE_RECEIVE_ACTION.toString());
        intent.putExtra(IntentActionsAndKeys.SAMPLE_KEY.toString(), sample);
        LocalBroadcastManager.getInstance(Measure.this).sendBroadcast(intent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        synchronized (this) {
            if(recorder != null)
                recorder.onDestroy();

            if (googleAPILocalization != null)
                getBaseContext().unbindService(localizationConnection);

            if(sender !=  null)
                getBaseContext().unbindService(senderConnection);

            if(dataBaseManger != null)
                dataBaseManger.flush();
        }
    }

    synchronized public void setSender(Sender sender){
        this.sender = sender;
    }

    synchronized public void  setGoogleAPILocalization(GoogleAPILocalization googleAPILocalization){
        this.googleAPILocalization = googleAPILocalization;
    }
}