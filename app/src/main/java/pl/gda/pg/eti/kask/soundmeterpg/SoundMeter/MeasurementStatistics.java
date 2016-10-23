package pl.gda.pg.eti.kask.soundmeterpg.SoundMeter;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Daniel on 06.10.2016 at 14:05 :).
 */

public class MeasurementStatistics implements Parcelable {
    public int min;
    public int max;
    public int avg;

    public MeasurementStatistics(){

    }
    @Override
    public String toString() {
        return "Min: " + min + " Max: " + max + " Avg: " + avg +"\n";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(min);
        parcel.writeInt(max);
        parcel.writeInt(avg);
    }

    private MeasurementStatistics(Parcel in){
        this.min = in.readInt();
        this.max = in.readInt();
        this.avg = in.readInt();
    }

    public static final Parcelable.Creator<MeasurementStatistics> CREATOR = new Parcelable.Creator<MeasurementStatistics>() {

        @Override
        public MeasurementStatistics createFromParcel(Parcel source) {
            return new MeasurementStatistics(source);
        }

        @Override
        public MeasurementStatistics[] newArray(int size) {
            return new MeasurementStatistics[size];
        }
    };

}
