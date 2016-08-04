package pl.gda.pg.eti.kask.soundmeterpg;

/**
 * Created by gierl on 19.07.2016.
 */
public class Probe {
    private double _avgNoiseLevel;
    private double _latitude;
    private double _longitude;

    public Probe(double avgNoiseLevel, double latitude, double longitude) {
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
