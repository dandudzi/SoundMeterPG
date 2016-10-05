package pl.gda.pg.eti.kask.soundmeterpg.Services;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.support.v4.content.LocalBroadcastManager;

import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.InsufficientGPSPermissionsException;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.InsufficientPermissionsException;
import pl.gda.pg.eti.kask.soundmeterpg.IntentActionsAndKeys;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.AudioRecorder;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Location;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.PreferenceParser;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Sample;

/**
 * Created by gierl on 01.10.2016.
 */

public class Measure extends IntentService {


    private GoogleAPILocalization googleAPILocalization;
    private Sender sender;

    private ServiceConnection localizationConnection = new GoogleApiServiceConnection(this);
    private ServiceConnection senderConnection= new SenderServiceConnection(this);
    private Thread binderThread;
    private PreferenceParser preferences;
    private AudioRecorder recorder;
    private volatile boolean endMeasure = false;

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
    protected void onHandleIntent(Intent _intent) {
        waitForBindAllService();

        long startTime,endTime;
        int oneMinute = 1000;
        try {
            initializeAudioRecorder();
            while (!endMeasure) {
                try {
                    startTime = System.currentTimeMillis();
                        sendSampleToMeasure(this.measure());
                    endTime = System.currentTimeMillis();
                    Thread.sleep(oneMinute - (endTime - startTime));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }catch (InsufficientPermissionsException e) {
            handleMeasureError(e);
        }
    }

    private void initializeAudioRecorder() throws InsufficientGPSPermissionsException {
        synchronized (this) {
            if(preferences.hasPermissionToUseMicrophone()){
                recorder = new AudioRecorder(getBaseContext());
            }else{
                throw new InsufficientGPSPermissionsException("There is not permission to use microphone");
            }

        }
    }

    private void waitForBindAllService() {
        try {
            binderThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void handleMeasureError(InsufficientPermissionsException e) {
        Intent intent = new Intent(IntentActionsAndKeys.ERROR_MEASURE_ACTION.toString());
        if(e instanceof InsufficientGPSPermissionsException)
            intent.putExtra(IntentActionsAndKeys.ERROR_KEY.toString(),IntentActionsAndKeys.GPS_ERROR_KEY.toString());
        LocalBroadcastManager.getInstance(Measure.this).sendBroadcast(intent);
    }

    private synchronized Sample measure() throws InsufficientPermissionsException{
        if(preferences.hasPermissionToUseMicrophone()){
            int noiseLevel = recorder.getNoiseLevel();
            return new Sample(noiseLevel, new Location(20.0,20.0));
        }else{
            throw new InsufficientGPSPermissionsException("There is not permission to use microphone");
        }
    }

    private void sendSampleToMeasure(Sample sample) {
        Intent intent = new Intent(IntentActionsAndKeys.SAMPLE_RECEIVE_ACTION.toString());
        intent.putExtra(IntentActionsAndKeys.SAMPLE_KEY.toString(), sample);
        LocalBroadcastManager.getInstance(Measure.this).sendBroadcast(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LocalBroadcastManager.getInstance(this).registerReceiver(endTaskReceiver, new IntentFilter(IntentActionsAndKeys.END_ACTION.toString()));
        bindServices();
        setUpThreads();

        preferences = new PreferenceParser(getBaseContext());
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
    public void onDestroy() {
        super.onDestroy();
        synchronized (this) {
            if (googleAPILocalization != null)
                getBaseContext().unbindService(localizationConnection);

            if(sender !=  null)
                getBaseContext().unbindService(senderConnection);
        }
    }

    synchronized public void setSender(Sender sender){
        this.sender = sender;
    }

    synchronized public void  setGoogleAPILocalization(GoogleAPILocalization googleAPILocalization){
        this.googleAPILocalization = googleAPILocalization;
    }
}
