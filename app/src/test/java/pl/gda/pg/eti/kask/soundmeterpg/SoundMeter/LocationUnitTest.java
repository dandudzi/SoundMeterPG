package pl.gda.pg.eti.kask.soundmeterpg.SoundMeter;

import org.junit.Test;

import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.OverRangeException;

import static junit.framework.Assert.assertEquals;

/**
 *
 * Created by gierl on 01.10.2016.
 */

public class LocationUnitTest {

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


    @Test
    public void convertLocationSimpleTest(){
        Location location = new Location(45.17823401,45.17823401);
        SexigesimalLocation output = location.convertLocation();

        assertEquals(45,output.degreesLatitude);
        assertEquals(10,output.minutesLatitude);
        assertEquals(41,output.secondsLatitude);

        assertEquals(45,output.degreesLongitude);
        assertEquals(10,output.minutesLongitude);
        assertEquals(41,output.secondsLongitude);
    }

    @Test
    public void convertLocationNegativeValueTest(){
        Location location = new Location(-65.3062656,-95.3062656);
        SexigesimalLocation output = location.convertLocation();

        assertEquals(-65,output.degreesLatitude);
        assertEquals(18,output.minutesLatitude);
        assertEquals(22,output.secondsLatitude);

        assertEquals(-95,output.degreesLongitude);
        assertEquals(18,output.minutesLongitude);
        assertEquals(22,output.secondsLongitude);
    }

    @Test
    public void convertLocationZeroTest(){
        Location location = new Location(0.0,0.45676);
        SexigesimalLocation output = location.convertLocation();

        assertEquals(0,output.degreesLatitude);
        assertEquals(0,output.minutesLatitude);
        assertEquals(0,output.secondsLatitude);

        assertEquals(0,output.degreesLongitude);
        assertEquals(27,output.minutesLongitude);
        assertEquals(24,output.secondsLongitude);

        location = new Location(0.0012,12.00345);
        output = location.convertLocation();

        assertEquals(0,output.degreesLatitude);
        assertEquals(0,output.minutesLatitude);
        assertEquals(4,output.secondsLatitude);

        assertEquals(12,output.degreesLongitude);
        assertEquals(0,output.minutesLongitude);
        assertEquals(12,output.secondsLongitude);

        location = new Location(85.0,74.45);
        output = location.convertLocation();

        assertEquals(85,output.degreesLatitude);
        assertEquals(0,output.minutesLatitude);
        assertEquals(0,output.secondsLatitude);

        assertEquals(74,output.degreesLongitude);
        assertEquals(27,output.minutesLongitude);
        assertEquals(0,output.secondsLongitude);
    }

}