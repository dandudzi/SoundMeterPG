package pl.gda.pg.eti.kask.soundmeterpg;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.util.Log;

import pl.gda.pg.eti.kask.soundmeterpg.Exception.OverrangeException;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.NullLocalizationException;

/**
 * Created by Filip Gierlowski and Daniel Dudziak
 */
public class NoiseLevel extends Service {

    private static final int MEASURMENTS_NUMBER_OF_SAMPLE_PER_MINUTES = 16;
    private static final int MILISECOND_IN_SECOND = 1000;
    private static final int ONE_PROBE = 3750;
    private static final int SAMPLE_RATE = 22050;
    private static final int CHANNEL = AudioFormat.CHANNEL_IN_MONO;
    private static final int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private static int BUFFER_SIZE;
    private AudioRecord _audioRecorder;
    private volatile double _avgDB;
    private double _amplitude;
    private short[] _buffer;
    private boolean _runningThread;
    private Thread _recordThread;
    private Thread _avgDecibels;
    private int _counter = 0;
    private DataBaseHandler _dataBaseHandler;
    private Localization _gps;

    @Override
    public void onCreate() {
        _dataBaseHandler = new DataBaseHandler(this);
        super.onCreate();
        _avgDB = 0.0;
        Context ctx2 = getBaseContext();
        Context ctx3 = getApplication().getBaseContext();
        _gps = new Localization(getBaseContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL, AUDIO_ENCODING);
        try {

            _audioRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, CHANNEL, AUDIO_ENCODING, BUFFER_SIZE);
            _buffer = new short[BUFFER_SIZE];
        } catch (Exception e) {
            android.util.Log.e("TrackingFlow", "Exception", e);
        }
        _audioRecorder.startRecording();
        getNoiseLevel();
        Runnable runnable = new Runnable() {
            public void run() {
                while (_runningThread) {
                    try {
                        Thread.sleep(ONE_PROBE);
                        _counter++;
                        _amplitude = getNoiseLevel();
                        // Log.i("Noise in db",Double.toString(_amplitude));
                        _amplitude /= (double) 10.0;
                        _amplitude = Math.pow(10, _amplitude);
                        _avgDB += _amplitude;
                        if (_counter > 2) {
                            double wynik = _avgDB;
                            _avgDB = 0;
                            wynik /= (double) 16;
                            wynik = 10 * Math.log10(wynik);
                            _counter = 0;
                            Log.i("Avg in db", Double.toString(wynik));
                            if (_gps.canUseLocation()) {
                                Probe pr = new Probe(wynik, _gps.getLocalization().getLatitude(), _gps.getLocalization().getLongitude());
                                //  new Insert().execute(pr);
                            }
                        }


                        //
                        //TODO kumulowanie danych,estymacja,zapisywanie na dysk,wysylanie na sewer
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (OverrangeException e) {
                        e.printStackTrace();
                    } catch (NullLocalizationException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        _runningThread = true;
        _recordThread = new Thread(runnable);
        _recordThread.start();
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        stopRecording();
        super.onDestroy();
    }

    private void stopRecording() {
        if (_audioRecorder != null) {
            _runningThread = false;
            _audioRecorder.stop();
            _audioRecorder.release();
            _audioRecorder = null;
            _recordThread = null;
        }
    }

    private double getNoiseLevel() {
        double amplitude = 1.0;
        return 20 * Math.log10(getAmplitude() / amplitude);
    }

    private int getAmplitude() {
        if (_audioRecorder != null) {
            _audioRecorder.read(_buffer, 0, BUFFER_SIZE);
            int max = 0;
            for (short s : _buffer) {
                if (Math.abs(s) > max) {
                    max = Math.abs(s);
                }
            }
            return max;
        } else return 0;
    }

}
