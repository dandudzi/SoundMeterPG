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
public class SampleTest {
    private final double CORECCT_LONGITUDE = 20.0;
    private final double CORECCT_LATITUDE = 20.0;
    private final  int CORRECT_NOISE_LEVEL = 24;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void parcelTest(){
        Bundle bundle =  new Bundle();
        Location location =  new Location(CORECCT_LATITUDE,CORECCT_LONGITUDE);
        Sample sample =  new Sample(CORRECT_NOISE_LEVEL,location);
        Parcel parcel = Parcel.obtain();
        String tag = "sampleTag";

        bundle.putParcelable(tag,sample);
        bundle.writeToParcel(parcel,0);

        parcel.setDataPosition(0);
        bundle = parcel.readBundle();
        bundle.setClassLoader(Sample.class.getClassLoader());
        Sample newSample = bundle.getParcelable(tag);
        assertEquals(sample,newSample);
    }


    @Test
    public void parcelFakeLocationTest(){
        Bundle bundle =  new Bundle();
        Location location =  new FakeLocation();
        Sample sample =  new Sample(CORRECT_NOISE_LEVEL,location);
        Parcel parcel = Parcel.obtain();
        String tag = "sampleTag";

        bundle.putParcelable(tag,sample);
        bundle.writeToParcel(parcel,0);

        parcel.setDataPosition(0);
        bundle = parcel.readBundle();
        bundle.setClassLoader(Sample.class.getClassLoader());
        Sample newSample = bundle.getParcelable(tag);
        assertEquals(sample,newSample);
    }
}
