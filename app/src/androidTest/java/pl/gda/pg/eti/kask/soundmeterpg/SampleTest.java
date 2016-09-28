package pl.gda.pg.eti.kask.soundmeterpg;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.OverrangeException;

/**
 * Created by gierl on 27.07.2016.
 */
@RunWith(AndroidJUnit4.class)
public class SampleTest {
    private static Sample sample;
    private static double EXPECTED_NOISE;
    private static double EXPECTED_LONGITUDE;
    private static double EXPECTED_LATITUDE;
    private static int EXPECTED_STATE; //0 -false, 1 -true
    @BeforeClass
    public static void initialize() {
        Random rand = new Random();
        EXPECTED_NOISE = Sample.MIN_NOISE_LEVEL + (Sample.MAX_NOISE_LEVEL - Sample.MIN_NOISE_LEVEL) * rand.nextDouble();
        EXPECTED_LATITUDE = Sample.MIN_LATITUDE + (Sample.MAX_LATITUDE - Sample.MIN_LATITUDE) * rand.nextDouble();
        EXPECTED_LONGITUDE = Sample.MIN_LONGITUDE + (Sample.MAX_LONGITUDE - Sample.MIN_LONGITUDE) * rand.nextDouble();
        EXPECTED_STATE = 0;
        try {
            sample = new Sample(EXPECTED_NOISE, EXPECTED_LATITUDE, EXPECTED_LONGITUDE, EXPECTED_STATE);
        } catch (OverrangeException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void isCorrectNoise() {
        Assert.assertEquals(EXPECTED_NOISE, sample.getAvgNoiseLevel(), 0.0);
    }

    @Test
    public void isCorrectLatitude() {
        Assert.assertEquals(EXPECTED_LATITUDE, sample.getLatitude(), 0.0);
    }

    @Test
    public void isCorrectLongitude() {
        Assert.assertEquals(EXPECTED_LONGITUDE, sample.getLongitude(), 0.0);
    }
    @Test
    public void isCorrectState(){
        try {
            Sample pr = new Sample(EXPECTED_NOISE, EXPECTED_LATITUDE, EXPECTED_LONGITUDE ,1);
            Assert.assertTrue(pr.getState());
            pr.setState(false);
            Assert.assertFalse(pr.getState());
        } catch (OverrangeException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void overrangeParametrTest() {
        Throwable throwable = null;
        try {
            Sample sample = new Sample(EXPECTED_NOISE, Sample.MAX_LATITUDE + EXPECTED_LATITUDE, EXPECTED_LONGITUDE, 0);
        } catch (Throwable e) {
            throwable = e;
        }
        Assert.assertTrue(throwable instanceof OverrangeException);

    }


}
