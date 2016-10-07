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

    private int BUFFER_SIZE;
    private short[] buffer;
    private AudioRecord recorder;
    private Context context;

    public AudioRecorder(Context context){
        this.context = context;
        recorder =  findAudioRecorder();
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

    private static final int[] SAMPLES_RATE = new int[] {  22050, 44100, 11025, 16000, 8000 };
    private static final int[] CHANNELS = new int[] { AudioFormat.CHANNEL_IN_MONO, AudioFormat.CHANNEL_IN_STEREO };
    private static final int[] AUDIOS_ENCODING = new int[] {  AudioFormat.ENCODING_PCM_16BIT, AudioFormat.ENCODING_PCM_8BIT, AudioFormat.ENCODING_PCM_FLOAT };

    private AudioRecord findAudioRecorder() {
        AudioRecord recorder;
        for (int CHANNEL:CHANNELS) {
            for (int AUDIO_ENCODING: AUDIOS_ENCODING) {
                for(int SAMPLE_RATE:SAMPLES_RATE){
                    try {
                        BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL, AUDIO_ENCODING);

                        if (BUFFER_SIZE != AudioRecord.ERROR_BAD_VALUE) {
                            recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,SAMPLE_RATE,CHANNEL,AUDIO_ENCODING,BUFFER_SIZE);

                            if (recorder.getState() == AudioRecord.STATE_INITIALIZED)
                                return recorder;
                            else
                                recorder.release();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

}
