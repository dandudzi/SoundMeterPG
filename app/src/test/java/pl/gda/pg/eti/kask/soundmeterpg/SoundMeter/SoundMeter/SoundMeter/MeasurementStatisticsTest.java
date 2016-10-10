package pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.SoundMeter.SoundMeter;

import android.support.annotation.NonNull;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Random;

import pl.gda.pg.eti.kask.soundmeterpg.Fragments.Measure;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.MeasurementStatistics;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Sample;

/**
 * Created by gierl on 10.10.2016.
 */

public class MeasurementStatisticsTest {
    private static int MIN;
    private static int MAX;
    private static int AVG;
    @BeforeClass
    public static void setUp(){
        Random random = new Random();
        MIN = random.nextInt(Sample.MAX_NOISE_LEVEL - Sample.MIN_NOISE_LEVEL) + Sample.MIN_NOISE_LEVEL;
        MAX = random.nextInt(Sample.MAX_NOISE_LEVEL - Sample.MIN_NOISE_LEVEL) + Sample.MIN_NOISE_LEVEL;
        AVG = random.nextInt(Sample.MAX_NOISE_LEVEL - Sample.MIN_NOISE_LEVEL) + Sample.MIN_NOISE_LEVEL;}
    @Test
    public void equalsStatisticsTest(){
        MeasurementStatistics measurementStatistics = createMeasurementStatistics();
        veryfyMeasurementStatistics(measurementStatistics);
    }

    @Test
    public void copyingObjectTest() throws CloneNotSupportedException {
        MeasurementStatistics measurementStatistics = createMeasurementStatistics();
        MeasurementStatistics m2 = (MeasurementStatistics) measurementStatistics.clone();
        veryfyMeasurementStatistics(m2);
        m2.avg = 2;
        Assert.assertNotSame(m2, measurementStatistics);
    }

    public static MeasurementStatistics createMeasurementStatistics() {
        MeasurementStatistics measurementStatistics = new MeasurementStatistics();
        measurementStatistics.max = MAX;
        measurementStatistics.min = MIN;
        measurementStatistics.avg = AVG;
        return measurementStatistics;
    }

    private void veryfyMeasurementStatistics(MeasurementStatistics measurementStatistics) {
        Assert.assertEquals(MIN, measurementStatistics.min);
        Assert.assertEquals(MAX, measurementStatistics.max);
        Assert.assertEquals(AVG, measurementStatistics.avg);
    }
}
