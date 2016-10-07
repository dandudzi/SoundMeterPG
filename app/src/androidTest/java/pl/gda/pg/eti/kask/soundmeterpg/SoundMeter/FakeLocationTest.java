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
 * Created by Daniel on 06.10.2016 at 13:17 :).
 */

@RunWith(AndroidJUnit4.class)
public class FakeLocationTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void parcelTest(){
        Bundle bundle =  new Bundle();
        Location location =  new FakeLocation();
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
