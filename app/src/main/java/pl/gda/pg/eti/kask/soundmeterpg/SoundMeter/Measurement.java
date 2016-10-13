package pl.gda.pg.eti.kask.soundmeterpg.SoundMeter;

import java.util.Date;
import java.util.List;

import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.OverRangeException;

/**
 * Created by gierl on 19.07.2016.
 */
public class Measurement {


    public Location location;
    public Date date;
    public boolean storedOnWebServer = false;
    public MeasurementStatistics statistics;

    public Measurement(MeasurementStatistics statistics, Location location, boolean storedOnWebServer, Date date) throws OverRangeException {
        this.statistics = statistics;
        this.location = location;
        this.date = date;
        this.storedOnWebServer = storedOnWebServer;
    }

    public static MeasurementStatistics calculateMeasureStatistics(List<Sample> list){
        double leq = 0;
        int min = 140;
        int max = 0;
            if(list.size() > 0 ){
            double noiseLevel;
            for (Sample sample: list) {
                noiseLevel = sample.getNoiseLevel();
                if(noiseLevel > max)
                    max = (int)noiseLevel;
                if(noiseLevel > 0 && noiseLevel < min)
                    min = (int) noiseLevel;
                noiseLevel /= 10;
                noiseLevel = Math.pow(10, noiseLevel);
                leq += noiseLevel;
            }
            leq/=list.size();
            leq = 10 * Math.log10(leq);
        }
        MeasurementStatistics statistics =  new MeasurementStatistics();
        statistics.min =  min;
        statistics.max = max;
        statistics.avg = (int)Math.round(leq);
        return statistics;
    }


    @Override
    public String toString() {
        return statistics + location.toString() + date.toString() + "\n storeOnWebServer: "+ storedOnWebServer;
    }
}
