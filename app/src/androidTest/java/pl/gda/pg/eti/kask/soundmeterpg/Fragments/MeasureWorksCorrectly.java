package pl.gda.pg.eti.kask.soundmeterpg.Fragments;

import android.content.Context;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.telephony.TelephonyManager;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.MainActivity;
import pl.gda.pg.eti.kask.soundmeterpg.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assume.assumeTrue;

/**
 * Created by Daniel on 06.10.2016 at 11:39 :).
 */
@RunWith(AndroidJUnit4.class)
public class MeasureWorksCorrectly {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void setUp() {

    }

    @Test
    public void startMeasureTest(){
        ifIsEmulatorIgnoreTest();
        onView(withId(R.id.measure_button_fragment)).perform(click());
    }

    @Ignore
    public void ifIsEmulatorIgnoreTest() {
        TelephonyManager tm = (TelephonyManager)mActivityRule.getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        String networkOperator = tm.getNetworkOperatorName();
        assumeTrue(!("Android".equals(networkOperator)));
    }

}
