package pl.gda.pg.eti.kask.soundmeterpg;

import java.util.Date;

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
    private double variance;
    private String date;
    private String userID;
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
    public Sample(double avgNoiseLevel, double variance, double latitude, double longitude, String date, String userID, int storedOnServer) throws OverrangeException {
        if (avgNoiseLevel < MIN_NOISE_LEVEL || avgNoiseLevel > MAX_NOISE_LEVEL ||
                latitude < MIN_LATITUDE || latitude > MAX_LATITUDE ||
                longitude < MIN_LONGITUDE || longitude > MAX_LONGITUDE)
            throw new OverrangeException("One of argument is overrange, Latitude = " +
                    latitude + ", Longitude = " + longitude + ", AvgNoiseLevel = " + avgNoiseLevel);
        this.avgNoiseLevel = avgNoiseLevel;
        this.variance = variance;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.userID = userID;
        this.storedOnServer = (storedOnServer == 1) ? true : false;
    }

    public double getAvgNoiseLevel() {
        return avgNoiseLevel;
    }

    public double getVariance() { return variance; }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getDate() {return  date; }

    public String getUserID() { return userID;  }
    public boolean getState() {
        return storedOnServer;
    }

    public void setState(boolean stored){ storedOnServer = stored;}
}
