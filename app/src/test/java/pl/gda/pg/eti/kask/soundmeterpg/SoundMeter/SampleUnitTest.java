package pl.gda.pg.eti.kask.soundmeterpg.SoundMeter;


import org.junit.Test;

import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.OverRangeException;

import static org.junit.Assert.assertEquals;

/**
 * Created by gierl on 01.10.2016.
 */

public class SampleUnitTest {
    private final  int CORRECT_NOISE_LEVEL = 24;
    private final int LESS_THAN_MIN_NOISE_LEVEL = Sample.MIN_NOISE_LEVEL - 1;
    private final int MORE_THAN_MAX_NOISE_LEVEL = Sample.MAX_NOISE_LEVEL + 1;
    private final Location CORRECT_LOCATION = new Location(Location.MAX_LATITUDE,Location.MAX_LONGITUDE);
    private final Location NULL_LOCATION = null;
    @Test
    public void noiseLevelTest(){
        Sample sample = new Sample(CORRECT_NOISE_LEVEL, CORRECT_LOCATION);
        assertEquals(CORRECT_NOISE_LEVEL, sample.getNoiseLevel());
    }

    @Test(expected = OverRangeException.class)
    public void lessThanMinNoiseLevelTest(){
        Sample sample = new Sample(LESS_THAN_MIN_NOISE_LEVEL, CORRECT_LOCATION);
    }

    @Test(expected = OverRangeException.class)
    public void moreThanMaxNoiseLevelTest(){
        Sample sample = new Sample(MORE_THAN_MAX_NOISE_LEVEL, CORRECT_LOCATION);
    }

    @Test
    public void correctLocationTest(){
        Sample sample = new Sample(CORRECT_NOISE_LEVEL, CORRECT_LOCATION);
        assertEquals(CORRECT_LOCATION, sample.getLocation());
    }

    @Test(expected = NullPointerException.class)
    public void nullLocationTest(){
        Sample sample = new Sample(CORRECT_NOISE_LEVEL, NULL_LOCATION);
    }

    @Test
    public void getNoiseLevelFromAmplitudeTest(){
        int zeroTest = 0;
        int zeroExpected = 0;
        int negativeTest = -1;
        int negativeExpected = 0;
        int correctTest = 50;
        int correctExpected = 34;

        assertEquals( zeroExpected, Sample.getNoiseLevelFromAmplitude(zeroTest));
        assertEquals(negativeExpected, Sample.getNoiseLevelFromAmplitude(negativeTest));
        assertEquals(correctExpected,Sample.getNoiseLevelFromAmplitude(correctTest));
    }

}