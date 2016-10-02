package pl.gda.pg.eti.kask.soundmeterpg.SoundMeter;

import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.OverRangeException;

/**
 * Created by gierl on 19.07.2016.
 */
public class Measurement {

    private double avgNoiseLevel;
    private double latitude;
    private double longitude;
    private boolean storedOnServer = false;

    public Measurement(double avgNoiseLevel, double latitude, double longitude, int storedOnServer) throws OverRangeException {
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
