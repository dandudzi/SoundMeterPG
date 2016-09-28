package pl.gda.pg.eti.kask.soundmeterpg;

import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.OverrangeException;

/**
 * Created by gierl on 19.07.2016.
 */
public class Sample {
    public static final double MIN_LATITUDE = -85.00;
    public static final double MAX_LATITUDE = 85.00;
    public static final double MIN_LONGITUDE = -180.00;
    public static final double MAX_LONGITUDE = 180.00;
    public static final double MIN_NOISE_LEVEL = 0.00;
    public static final double MAX_NOISE_LEVEL = 140.00;
    private double avgNoiseLevel;
    private double latitude;
    private double longitude;
    private boolean storedOnServer = false;

    public Sample(double avgNoiseLevel, double latitude, double longitude, int storedOnServer) throws OverrangeException {
        if (avgNoiseLevel < MIN_NOISE_LEVEL || avgNoiseLevel > MAX_NOISE_LEVEL ||
                latitude < MIN_LATITUDE || latitude > MAX_LATITUDE ||
                longitude < MIN_LONGITUDE || longitude > MAX_LONGITUDE)
            throw new OverrangeException("One of argument is overrange, Latitude = " +
                    latitude + ", Longitude = " + longitude + ", AvgNoiseLevel = " + avgNoiseLevel);
        this.avgNoiseLevel = avgNoiseLevel;
        this.latitude = latitude;
        this.longitude = longitude;
        this.storedOnServer = (storedOnServer == 1) ? true : false;
    }

    public double getAvgNoiseLevel() {
        return avgNoiseLevel;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public boolean getState() {
        return storedOnServer;
    }

    public void setState(boolean stored){ storedOnServer = stored;}
}
