package pl.gda.pg.eti.kask.soundmeterpg.SoundMeter;

import android.os.Parcel;
import android.os.Parcelable;

import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.OverRangeException;

/**
 * Created by gierl on 01.10.2016.
 */
public class Location implements Parcelable{
    public static final double MIN_LATITUDE = -85.00;
    public static final double MAX_LATITUDE = 85.00;
    public static final double MIN_LONGITUDE = -180.00;
    public static final double MAX_LONGITUDE = 180.00;

    private double latitude;
    private double longitude;

    public Location(double latitude, double longitude){
        if(isLocationCorrect(latitude, longitude)) {
            this.latitude = latitude;
            this.longitude = longitude;
        } else
            throw new OverRangeException(createLocationOverRangeException(latitude,longitude));
    }

    private boolean isLocationCorrect(double latitude, double longitude) {
        return latitude >= MIN_LATITUDE && latitude <= MAX_LATITUDE &&
                longitude >= MIN_LONGITUDE && longitude <= MAX_LONGITUDE;
    }

    private String createLocationOverRangeException(double latitude, double longitude) {
        return "Incorrect location correct values are latitude: " + MIN_LATITUDE + " to " +MAX_LATITUDE +"\nand longitude: " +
                + MIN_LONGITUDE + " to " + MAX_LONGITUDE + ". Current value latitude: "+ latitude + " longitude: " + longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(!(obj instanceof Location))
            return false;
        Location object = (Location)obj;
        if(object.getLatitude() == latitude && object.getLongitude() == longitude)
            return true;
        return false;
    }

    @Override
    public String toString() {
        return "Latitude: "+ latitude + " Longitude: "+ longitude + "\n";
    }

    //Parcelable

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
    }

    private Location(Parcel in){
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
    }

    public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {

        @Override
        public Location createFromParcel(Parcel source) {
            return new Location(source);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
}
