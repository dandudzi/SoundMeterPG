package pl.gda.pg.eti.kask.soundmeterpg.SoundMeter;

import android.os.Parcelable;

/**
 * Created by Daniel on 06.10.2016 at 13:10 :).
 */

public class FakeLocation extends Location implements Parcelable {
    //Punkt na Oceanie Arktycznym :)
    public static final double FAKE_LATITUDE = 80.23;
    public static final double FAKE_LONGITUDE = 5.39;

    public FakeLocation() {
        super(FAKE_LATITUDE, FAKE_LONGITUDE);
    }
}
