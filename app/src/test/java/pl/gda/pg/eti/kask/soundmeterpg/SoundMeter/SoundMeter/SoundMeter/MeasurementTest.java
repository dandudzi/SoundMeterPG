package pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.SoundMeter.SoundMeter;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.OverRangeException;
import pl.gda.pg.eti.kask.soundmeterpg.Fragments.Measure;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Location;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Measurement;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.MeasurementStatistics;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Sample;

/**
 * Created by gierl on 10.10.2016.
 */

public class MeasurementTest {

    private static Measurement measurement;
    private static MeasurementStatistics measurementStatistics = MeasurementStatisticsTest.createMeasurementStatistics();
    private static Date date = new Date();
    private Location location;
    private static final Location CORRECT_LOCATION = new Location(Location.MAX_LATITUDE,Location.MAX_LONGITUDE);

    @BeforeClass
    public static void setUp(){
        measurement = new Measurement(measurementStatistics, CORRECT_LOCATION, true, date,0);
    }
    @Test
public void measurementStatisticsTest(){
    Measurement measurement = new Measurement(measurementStatistics, CORRECT_LOCATION, true, date,0);
    veryfyMeasureStatistics(measurement);
}
    @Test
    public void locationTest(){

        Assert.assertEquals(CORRECT_LOCATION.getLatitude(), measurement.getLocation().getLatitude(),0);
        Assert.assertEquals(CORRECT_LOCATION.getLongitude(), measurement.getLocation().getLongitude(),0);
    }
    @Test
    public void storedOnWebServerTest(){
        Assert.assertTrue(measurement.getStoredState());
    }

    @Test
    public void dateTest(){
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Assert.assertEquals(formatter.format(date), measurement.getDate());
    }

    @Test(expected = OverRangeException.class)
    public void measurementStatisticsLessThanMinValueTest(){
        MeasurementStatistics measurementStatistics = new MeasurementStatistics();
        measurementStatistics.min = -1;
        Measurement measurement = new Measurement(measurementStatistics, location, true, date,0);
    }

    @Test(expected = OverRangeException.class)
    public void measurementStatisticsLessThanMaxValueTest(){
        MeasurementStatistics measurementStatistics = new MeasurementStatistics();
        measurementStatistics.max = Sample.MAX_NOISE_LEVEL * 2;
        Measurement measurement = new Measurement(measurementStatistics, location, true, date,0);
    }

    @Test(expected = OverRangeException.class)
    public void measurementStatisticsLessThaAvgValueTest(){
        MeasurementStatistics measurementStatistics = new MeasurementStatistics();
        measurementStatistics.avg = -2;
        Measurement measurement = new Measurement(measurementStatistics, location, true, date,0);
    }

    private void veryfyMeasureStatistics(Measurement measurement) {
        Assert.assertEquals(measurementStatistics.min, measurement.getMin());
        Assert.assertEquals(measurementStatistics.max, measurement.getMax());
        Assert.assertEquals(measurementStatistics.avg, measurement.getAvg());
    }
}
