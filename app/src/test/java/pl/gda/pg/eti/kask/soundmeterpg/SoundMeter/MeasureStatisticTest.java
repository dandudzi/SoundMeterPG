package pl.gda.pg.eti.kask.soundmeterpg.SoundMeter;

import org.junit.Test;

import pl.gda.pg.eti.kask.soundmeterpg.MutableInteger;

import static org.junit.Assert.*;

/**
 * Created by Daniel on 14.10.2016.
 */
public class MeasureStatisticTest {
    @Test
    public void statisticTest() throws Exception {
        MeasurementStatistics statistics =  new MeasurementStatistics();
        int noiseLevel = 20;
        MutableInteger counterSampleAvg = new MutableInteger();
        int expected = 2;

        assertEquals(noiseLevel, MeasureStatistic.setUpStatistic(noiseLevel,statistics,counterSampleAvg));
        assertEquals(noiseLevel, statistics.min);
        assertEquals(noiseLevel, statistics.max);
        assertEquals(noiseLevel, statistics.avg);
        assertEquals(expected, counterSampleAvg.value);

        noiseLevel = 16;
        expected++;

        assertEquals(18, MeasureStatistic.setUpStatistic(noiseLevel,statistics,counterSampleAvg));
        assertEquals(16, statistics.min);
        assertEquals(20, statistics.max);
        assertEquals(36, statistics.avg);
        assertEquals(expected, counterSampleAvg.value);

        noiseLevel = 0;

        assertEquals(18, MeasureStatistic.setUpStatistic(noiseLevel,statistics,counterSampleAvg));
        assertEquals(16, statistics.min);
        assertEquals(20, statistics.max);
        assertEquals(36, statistics.avg);
        assertEquals(expected, counterSampleAvg.value);

        noiseLevel = 123;
        statistics.avg = 2147483646;
        counterSampleAvg.value = 53687091;
        expected = 3;

        assertEquals((39+noiseLevel)/2, MeasureStatistic.setUpStatistic(noiseLevel,statistics,counterSampleAvg));
        assertEquals(16, statistics.min);
        assertEquals(noiseLevel, statistics.max);
        assertEquals(39 + noiseLevel, statistics.avg);
        assertEquals(expected, counterSampleAvg.value);
    }

    @Test
    public void statisticZeroTest() throws Exception {
        MeasurementStatistics statistics = new MeasurementStatistics();
        int noiseLevel = 0;
        MutableInteger counterSampleAvg = new MutableInteger();
        int expected = 1;

        assertEquals(noiseLevel, MeasureStatistic.setUpStatistic(noiseLevel, statistics, counterSampleAvg));
        assertEquals(noiseLevel, statistics.min);
        assertEquals(noiseLevel, statistics.max);
        assertEquals(noiseLevel, statistics.avg);
        assertEquals(expected, counterSampleAvg.value);
    }
}