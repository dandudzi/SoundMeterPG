package pl.gda.pg.eti.kask.soundmeterpg;

import pl.gda.pg.eti.kask.soundmeterpg.Exception.OverrangeException;

/**
 * Created by gierl on 19.07.2016.
 */
public class Probe {
    public static final double MIN_LATITUDE = -85.00;
    public static final double MAX_LATITUDE = 85.00;
    public static final double MIN_LONGITUDE = -180.00;
    public static final double MAX_LONGITUDE = 180.00;
    public static final double MIN_NOISE_LEVEL = 0.00;
    public static final double MAX_NOISE_LEVEL = 140.00;
    private double _avgNoiseLevel;
    private double _latitude;
    private double _longitude;

    public Probe(double avgNoiseLevel, double latitude, double longitude) throws OverrangeException {
        if (avgNoiseLevel < MIN_NOISE_LEVEL || avgNoiseLevel > MAX_NOISE_LEVEL ||
                latitude < MIN_LATITUDE || latitude > MAX_LATITUDE ||
                longitude < MIN_LONGITUDE || longitude > MAX_LATITUDE)
            throw new OverrangeException("One of argument is overrange, Latitude = " +
                    latitude + ", Longitude = " + longitude + ", AvgNoiseLevel = " + avgNoiseLevel);
        _avgNoiseLevel = avgNoiseLevel;
        _latitude = latitude;
        _longitude = longitude;
    }

    public double getAvgNoiseLevel() {
        return _avgNoiseLevel;
    }

    public double getLatitude() {
        return _latitude;
    }

    public double getLongitude() {
        return _longitude;
    }
}
