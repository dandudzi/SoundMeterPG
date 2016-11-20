package pl.gda.pg.eti.kask.soundmeterpg.SoundMeter;

import android.support.annotation.NonNull;
import android.util.Log;

import java.text.Format;
import java.text.SimpleDateFormat;
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
    public int weight;
    public MeasurementStatistics statistics;

    public Measurement(MeasurementStatistics statistics, Location location, boolean storedOnWebServer, Date date, int weight) throws OverRangeException {
        if(isCorrectStatistics(statistics))
            this.statistics = statistics;
        else
            throw new OverRangeException(createOverRangeExceptionMessage(statistics));
        this.location = location;
        this.date = date;
        this.storedOnWebServer = storedOnWebServer;
        this.weight = weight;
    }

    @NonNull
    private String createOverRangeExceptionMessage(MeasurementStatistics statistics) {
        return "Incorrect value. Correct value are : MinNoiseLevel : "+ Sample.MIN_NOISE_LEVEL + "\n" +
                "MaxNoiseLevel : " + Sample.MAX_NOISE_LEVEL + "\n AvgNoise : bigger than : " + Sample.MIN_NOISE_LEVEL + " and less than : " + Sample.MAX_NOISE_LEVEL + " \n" +
                "Current value : MinNoiseLevel : " + statistics.min + " MaxNoiseLevel : " + statistics.max + " AvgNoiseLevel : " + statistics.avg;
    }

    private boolean isCorrectStatistics(MeasurementStatistics statistics) {
        return  ( statistics.min >= Sample.MIN_NOISE_LEVEL && statistics.min <= Sample.MAX_NOISE_LEVEL &&
                ( statistics.max >= Sample.MIN_NOISE_LEVEL && statistics.max <= Sample.MAX_NOISE_LEVEL)&&
                ( statistics.avg >= Sample.MIN_NOISE_LEVEL && statistics.avg <= Sample.MAX_NOISE_LEVEL));}

    public Location getLocation(){
        return location;
    }
    public String getDate(){
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
       return formatter.format(date);
    }
    public int getMin(){
        return statistics.min;
    }
    public  int getMax(){
        return statistics.max;
    }
    public int getAvg(){
        return statistics.avg;
    }

    public int getWeight() { return  weight;}
    public boolean getStoredState(){
        return storedOnWebServer;
    }
    public static MeasurementStatistics calculateMeasureStatistics(List<Sample> list){
      /*  double leq = 0;
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
        */

        double sum = 0.0;
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
              sum += noiseLevel;
            }
           sum /= list.size();
        }
        MeasurementStatistics statistics =  new MeasurementStatistics();
        statistics.min =  min;
        statistics.max = max;
        statistics.avg = (int) Math.round(sum);
        return statistics;
    }


    @Override
    public String toString() {
        return statistics + location.toString() + date.toString() + "\n storeOnWebServer: "+ storedOnWebServer;
    }
}
