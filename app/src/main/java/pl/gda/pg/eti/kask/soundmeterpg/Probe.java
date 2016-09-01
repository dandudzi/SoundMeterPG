package pl.gda.pg.eti.kask.soundmeterpg;

import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.OverrangeException;

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
    private boolean _storedOnServer = false;

    public Probe(double avgNoiseLevel, double latitude, double longitude, int storedOnServer) throws OverrangeException {
        if (avgNoiseLevel < MIN_NOISE_LEVEL || avgNoiseLevel > MAX_NOISE_LEVEL ||
                latitude < MIN_LATITUDE || latitude > MAX_LATITUDE ||
                longitude < MIN_LONGITUDE || longitude > MAX_LONGITUDE)
            throw new OverrangeException("One of argument is overrange, Latitude = " +
                    latitude + ", Longitude = " + longitude + ", AvgNoiseLevel = " + avgNoiseLevel);
        _avgNoiseLevel = avgNoiseLevel;
        _latitude = latitude;
        _longitude = longitude;
        _storedOnServer = (storedOnServer == 1) ? true : false;
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

    public boolean getState() {
        return _storedOnServer;
    }
}
