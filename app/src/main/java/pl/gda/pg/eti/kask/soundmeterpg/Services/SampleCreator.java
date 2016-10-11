package pl.gda.pg.eti.kask.soundmeterpg.Services;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;


import pl.gda.pg.eti.kask.soundmeterpg.DataBaseHandler;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.InsufficientPermissionsException;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.NullRecordException;
import pl.gda.pg.eti.kask.soundmeterpg.Interfaces.MeasurementChangeListener;
import pl.gda.pg.eti.kask.soundmeterpg.R;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Measurement;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.PreferenceParser;

/**
 * Created by Filip Gierlowski and Daniel Dudziak
 */
public class SampleCreator extends Service {

    private static final int CHANNEL = AudioFormat.CHANNEL_IN_MONO;
    private static final int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private static final int LOCALIZATION = 0;
    private static final int SENDER = 1;
    private static int BUFFER_SIZE;
    private volatile double avgDB;
    private double amplitude;
    private short[] buffer;
    private int counter = 0;
    private AudioRecord audioRecord;
    private PreferenceParser preferenceParser;
    private final IBinder localBinder = new LocalBinder();
    private static Intent[] intents = new Intent[2];
    private volatile boolean runningThread = false;
    private Thread recordThread;
    private GoogleAPILocalization googleAPILocalization;
    private Sender sender;
    private DataBaseHandler dataBaseHandler;
    private MeasurementChangeListener listener;



    private ServiceConnection localizationConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            GoogleAPILocalization.LocalBinder binder = (GoogleAPILocalization.LocalBinder) service;
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(binder == null)
                binder = (GoogleAPILocalization.LocalBinder) service;
            googleAPILocalization = binder.getService();
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            googleAPILocalization = null;
        }
    };

    private ServiceConnection senderConnection= new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            Sender.LocalBinder binder = (Sender.LocalBinder) service;
            sender = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            sender = null;
        }
    };



    @Override
    public IBinder onBind(final Intent intent) {
        avgDB = 0.0;
        BUFFER_SIZE = AudioRecord.getMinBufferSize(getResources().getInteger(R.integer.sample_rate), CHANNEL, AUDIO_ENCODING);
        buffer = new short[BUFFER_SIZE];
        preferenceParser = new PreferenceParser(this);
        bindServices();
        return localBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
       stop();
        unbindServices();
        return super.onUnbind(intent);
    }

    public void start() throws InsufficientPermissionsException {
        avgDB = 0.0;
        counter = 0;
        if(!preferenceParser.hasPermissionToUseInternalStorage() ||
                !preferenceParser.hasPermissionToUseMicrophone())
            throw new InsufficientPermissionsException("Can not store samples becouse you dont have permission to use Microphone" +
                    " or you can not store samples in internal storage. Check this !");
        dataBaseHandler = new DataBaseHandler(this, getResources().getString(R.string.database_name));
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, getResources().getInteger(R.integer.sample_rate), CHANNEL, AUDIO_ENCODING, BUFFER_SIZE);
        audioRecord.startRecording();
        getNoiseLevel();
        runningThread = true;
         Runnable runnable = new Runnable() {
            public void run() {
                while (runningThread) {
                    if(googleAPILocalization == null){
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }

                    try {
                        Thread.sleep(getResources().getInteger(R.integer.time_of_sample));
                        counter++;
                        calculateSample();
                        if (counter == getResources().getInteger(R.integer.samples_per_minute)-1) {
                            double result = avgDB;
                            avgDB = 0;
                            result /= (double) getResources().getInteger(R.integer.samples_per_minute);
                            result = 10 * Math.log10(result);
                            counter = 0;
                            Log.i("Avg in db", Double.toString(result));
                            if (googleAPILocalization.canUseGPS()) {
                                Measurement oneMinuteMeasurement = null;
                              /*  try {
                                    Location l  = googleAPILocalization.getLocation();
                                    //oneMinuteMeasurement = new Measurement(result, l.getLatitude(),l.getLongitude(), 0);
                                } catch (OverrangeException e) {
                                    e.printStackTrace();
                                } catch (NullLocalizationException e) {
                                    e.printStackTrace();
                                }*/

                                if(listener != null)
                                    listener.onMeasurementChange(oneMinuteMeasurement);

                                //Log.i("Sample", Integer.toString((int) oneMinuteMeasurement.getAvgNoiseLevel()));
                                if (sender.isConnectionWithServer("soundmeterpg.cba.pl")) {
                                   /* try {
                                        //if (sender.insert(oneMinuteMeasurement))
                                        //    oneMinuteMeasurement.setState(true);
                                      //  Log.i("Store samples on server",Double.toString(oneMinuteMeasurement.getAvgNoiseLevel()));
                                    } catch (NullRecordException e) {
                                        e.printStackTrace();
                                    }*/
                                }
                               /* try {
                                   // dataBaseHandler.insert(oneMinuteMeasurement);
                                   // Log.i("Store samples in Database",Double.toString(oneMinuteMeasurement.getLatitude())+ Double.toString(oneMinuteMeasurement.getLongitude()));
                                } catch (NullRecordException e) {
                                    e.printStackTrace();
                                }*/
                            }
                            //TODO kumulowanie danych,estymacja,zapisywanie na dysk,wysylanie na sewer
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        recordThread = new Thread(runnable);
        recordThread.start();
    }

    public void stop(){
        runningThread = false;
        if(audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }
        if(recordThread != null){
            try {
                recordThread.join();
                recordThread = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(dataBaseHandler != null)
        dataBaseHandler.close();

    }

    private void bindServices(){
        intents[LOCALIZATION] = new Intent(getBaseContext(),GoogleAPILocalization.class);
        intents[SENDER] = new Intent(getBaseContext(),Sender.class);
        bindService(intents[LOCALIZATION], localizationConnection, Context.BIND_AUTO_CREATE);
        bindService(intents[SENDER], senderConnection, Context.BIND_AUTO_CREATE);
    }

    private  void unbindServices(){
        unbindService(localizationConnection);
        unbindService(senderConnection);
    }

    private double getNoiseLevel() {
        double amplitude = 1.0;
        return 20 * Math.log10(getAmplitude() / amplitude);
    }

    private int getAmplitude() {
        if (audioRecord != null) {
            audioRecord.read(buffer, 0, BUFFER_SIZE);
            int max = 0;
            for (short s : buffer) {
                if (Math.abs(s) > max) {
                    max = Math.abs(s);
                }
            }
            return max;
        } else return 0;
    }

    private void calculateSample(){
        amplitude = getNoiseLevel();
        amplitude /= 10.0;
        amplitude = Math.pow(10, amplitude);
        avgDB += amplitude;
    }

    public void setOnMeasurementChangeListener(MeasurementChangeListener onMeasurementChangeLisneter) {
        this.listener = onMeasurementChangeLisneter;
    }

    public class LocalBinder extends Binder {
        public SampleCreator getService() {
            return SampleCreator.this;
        }
    }
}
