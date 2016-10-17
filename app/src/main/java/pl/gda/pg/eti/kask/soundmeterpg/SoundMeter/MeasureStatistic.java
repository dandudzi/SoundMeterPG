package pl.gda.pg.eti.kask.soundmeterpg.SoundMeter;

import pl.gda.pg.eti.kask.soundmeterpg.MutableInteger;

/**
 * Created by Daniel on 14.10.2016.
 */

public class MeasureStatistic {
    final static String DEGREE  = "\u00b0";


    public static String getLongitude(SexigesimalLocation location) {
        return String.valueOf(location.degreesLongitude)+DEGREE+" "+
                String.valueOf(location.minutesLongitude)+"' "+
                String.valueOf(location.secondsLongitude)+"''";
    }

    public static String getLatitude(SexigesimalLocation location) {
        return String.valueOf(location.degreesLatitude)+DEGREE+" "+
                String.valueOf(location.minutesLatitude)+"' "+
                String.valueOf(location.secondsLatitude)+"''";
    }

    public static int setUpStatistic(int noiseLevel, MeasurementStatistics statistic, MutableInteger counterSampleAvg) {
        if(statistic.min == 0)
            statistic.min = noiseLevel;
        if(noiseLevel > 0 && statistic.min > noiseLevel)
            statistic.min = noiseLevel;
        if(noiseLevel > statistic.max)
            statistic.max = noiseLevel;
        if((statistic.avg + noiseLevel) < 0){
            statistic.avg /= counterSampleAvg.value;
            counterSampleAvg.value = 1;
        }
        if(noiseLevel!=0)
            ++counterSampleAvg.value;

        statistic.avg += noiseLevel;
        if(counterSampleAvg.value == 0)
            return  0;
        return statistic.avg / counterSampleAvg.value;
    }
}
