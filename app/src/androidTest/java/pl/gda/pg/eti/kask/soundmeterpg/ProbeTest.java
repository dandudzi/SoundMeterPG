package pl.gda.pg.eti.kask.soundmeterpg;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by gierl on 27.07.2016.
 */
@RunWith(AndroidJUnit4.class)
public class ProbeTest {
    private Probe _probe;
    private double EXPECTED_NOISE = 55.066;
    private double EXPECTED_LONGITUDE = 25.346;
    private double EXPECTED_LATITUDE = 34.666;

    @Before
    public void initialize() {
        _probe = new Probe(EXPECTED_NOISE, EXPECTED_LATITUDE, EXPECTED_LONGITUDE);
    }

    @Test
    public void isCorrectNoise() {
        Assert.assertEquals(EXPECTED_NOISE, _probe.getAvgNoiseLevel(), 0.0);
    }

    @Test
    public void isCorrectLatitude() {
        Assert.assertEquals(EXPECTED_LATITUDE, _probe.getLatitude(), 0.0);
    }

    @Test
    public void isCorrectLongitude() {
        Assert.assertEquals(EXPECTED_LONGITUDE, _probe.getLongitude(), 0.0);
    }


}
