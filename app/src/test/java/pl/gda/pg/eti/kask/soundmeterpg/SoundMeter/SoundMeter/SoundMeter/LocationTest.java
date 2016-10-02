package pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.SoundMeter.SoundMeter;

import android.os.Bundle;
import android.os.Parcel;

import org.junit.Assert;
import org.junit.Test;

import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.OverRangeException;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Location;

import static junit.framework.Assert.assertEquals;

/**
 * Created by gierl on 01.10.2016.
 */
public class LocationTest {

    private final double CORECCT_LONGITUDE = 20.0;
    private final double LESS_THAN_MIN_LONGITUDE = Location.MIN_LONGITUDE -1;
    private final double MORE_THAN_MAX_LONGITUDE = Location.MAX_LONGITUDE+1;
    private final double CORECCT_LATITUDE = 20.0;
    private final double LESS_THAN_MIN_LATITUDE = Location.MIN_LATITUDE -1;
    private final double MORE_THAN_MAX_LATITUDE= Location.MAX_LATITUDE+1;

    @Test
    public void getLatitudeTest() {
        Location location =  new Location(CORECCT_LATITUDE, CORECCT_LONGITUDE);
        assertEquals(CORECCT_LATITUDE,location.getLatitude());
    }

    @Test
    public void getLongitudeTest() {
        Location location =  new Location(CORECCT_LATITUDE, CORECCT_LONGITUDE);
        assertEquals(CORECCT_LONGITUDE,location.getLongitude());
    }

    @Test(expected = OverRangeException.class)
    public void moreThanPossibleValueOfLatitudeTest() {
        Location location = new Location(MORE_THAN_MAX_LATITUDE, CORECCT_LONGITUDE);
    }

    @Test(expected = OverRangeException.class)
    public void moreThanPossibleValueOfLongitudeTest() {
        Location location = new Location(CORECCT_LATITUDE, MORE_THAN_MAX_LONGITUDE);
    }

    @Test(expected = OverRangeException.class)
    public void lessThanPossibleValueOfLatitudeTest(){
        Location location = new Location(LESS_THAN_MIN_LATITUDE, CORECCT_LONGITUDE);
    }

    @Test(expected = OverRangeException.class)
    public void lessThanPossibleValueOfLongitudeTest(){
        Location location = new Location(CORECCT_LATITUDE, LESS_THAN_MIN_LONGITUDE);
    }


}