package pl.gda.pg.eti.kask.soundmeterpg.SoundMeter;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import pl.gda.pg.eti.kask.soundmeterpg.R;

/**
 * Created by Daniel on 05.10.2016.
 */

public class AudioRecorder {
    private  final int CHANNEL = AudioFormat.CHANNEL_IN_MONO;
    private  final int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private final int BUFFER_SIZE;
    private short[] buffer;
    private AudioRecord recorder;
    private Context context;

    public AudioRecorder(Context context){
        this.context = context;
        int sampleRateInHz = context.getResources().getInteger(R.integer.sample_rate);
        BUFFER_SIZE = AudioRecord.getMinBufferSize(context.getResources().getInteger(R.integer.sample_rate), CHANNEL, AUDIO_ENCODING);
        recorder =  new AudioRecord(MediaRecorder.AudioSource.MIC,sampleRateInHz,CHANNEL,AUDIO_ENCODING,BUFFER_SIZE );
        buffer =  new short[BUFFER_SIZE];
        recorder.startRecording();
       getAmplitude();
    }

    public int getNoiseLevel() {
        double amplitude = 1.0;
        int amp2 = getAmplitude();
        int wynik = (int)(20 * Math.log10(getAmplitude() / amplitude));
        return (int)(20 * Math.log10(getAmplitude() / amplitude));
    }

    private int getAmplitude() {
        if (recorder != null) {
            recorder.read(buffer, 0, BUFFER_SIZE);
            int max = 0;
            for (short s : buffer) {
                if (Math.abs(s) > max) {
                    max = Math.abs(s);
                }
            }
            return max;
        } else return 0;
    }

    public void onDestroy(){
        if(recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
        }
    }
}
