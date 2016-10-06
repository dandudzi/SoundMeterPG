package pl.gda.pg.eti.kask.soundmeterpg.SoundMeter;

/**
 * Created by Daniel on 06.10.2016 at 14:05 :).
 */

public class MeasurementStatistics {
    public int min;
    public int max;
    public int avg;

    @Override
    public String toString() {
        return "Min: " + min + " Max: " + max + " Avg: " + avg +"\n";
    }
}
