package pl.gda.pg.eti.kask.soundmeterpg.SoundMeter;


import android.os.Parcel;
import android.os.Parcelable;

import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.OverRangeException;

/**
 * Created by gierl on 01.10.2016.
 */
public class Sample implements Parcelable{
    public static final int MIN_NOISE_LEVEL = 0;
    public static final int MAX_NOISE_LEVEL = 140;


    private int noiseLevel;
    private Location location;


    public static int calculateRMSForPeriodOfTime(short[] amplitudesTable, final int BUFFER_SIZE){
        double RMS = 0;
        int counter = 0;
        for (short s : amplitudesTable) {
            if(s!=0)
                counter++;
            RMS += (s*s);
        }
        RMS /= BUFFER_SIZE;
        if(RMS != 0)
            RMS = Math.sqrt(RMS);
        return (int)RMS;
    }

    public static int getNoiseLevelFromAmplitude(int amplitude){
        return Math.abs((int)Math.round(20 * Math.log10(amplitude / 1.0)));
    }

    public Sample(int noiseLevel, Location location) {
        if(location == null)
            throw new NullPointerException("Location is null object");
        this.location = location;
        if(isNoiseLevelCorrect(noiseLevel))
            this.noiseLevel = noiseLevel;
        else
            throw new OverRangeException(createOverRangeExceptionMessage(noiseLevel));
    }

    private boolean isNoiseLevelCorrect(int noiseLevel) {
        return noiseLevel >= MIN_NOISE_LEVEL && noiseLevel <= MAX_NOISE_LEVEL;
    }

    private String createOverRangeExceptionMessage(double noiseLevel) {
        return "Incorrect noiseLevel parameter. Correct value is "+MIN_NOISE_LEVEL + " to "+
        MAX_NOISE_LEVEL + ". Current value is: " + noiseLevel;
    }

    public int getNoiseLevel() {
        return noiseLevel;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(!(obj instanceof Sample))
            return false;
        Sample object = (Sample) obj;
        if(object.getLocation().equals(location) && object.getNoiseLevel() == noiseLevel)
            return true;

        return false;
    }

    //Parcelable

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(location,0);
        parcel.writeInt(noiseLevel);
    }

    private Sample(Parcel in){
        this.location = (Location)in.readParcelable(Location.class.getClassLoader());
        this.noiseLevel = in.readInt();
    }

    public static final Parcelable.Creator<Sample> CREATOR = new Parcelable.Creator<Sample>() {

        @Override
        public Sample createFromParcel(Parcel source) {
            return new Sample(source);
        }

        @Override
        public Sample[] newArray(int size) {
            return new Sample[size];
        }
    };

    @Override
    public String toString() {
        return "Noise level: "+ noiseLevel;
    }
}
