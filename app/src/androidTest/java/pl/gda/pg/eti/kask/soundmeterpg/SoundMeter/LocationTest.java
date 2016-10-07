package pl.gda.pg.eti.kask.soundmeterpg.SoundMeter;

import android.os.Bundle;
import android.os.Parcel;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.MainActivity;

import static junit.framework.Assert.assertEquals;

/**
 * Created by gierl on 01.10.2016.
 */
@RunWith(AndroidJUnit4.class)
public class LocationTest {

    private final double CORECCT_LONGITUDE = 20.0;
    private final double CORECCT_LATITUDE = 20.0;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void parcelTest(){
        Bundle bundle =  new Bundle();
        Location location =  new Location(CORECCT_LATITUDE,CORECCT_LONGITUDE);
        Parcel parcel = Parcel.obtain();
        String tag = "locationTag";

        bundle.putParcelable(tag,location);
        bundle.writeToParcel(parcel,0);

        parcel.setDataPosition(0);
        bundle = parcel.readBundle();
        bundle.setClassLoader(Location.class.getClassLoader());
        Location newLocation = bundle.getParcelable(tag);
        assertEquals(location,newLocation);
    }
}
