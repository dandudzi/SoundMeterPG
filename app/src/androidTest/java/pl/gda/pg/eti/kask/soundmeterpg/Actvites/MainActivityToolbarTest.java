package pl.gda.pg.eti.kask.soundmeterpg.Actvites;

/**
 * Created by Daniel on 09.07.2016 at 12:12 :).
 */
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.MainActivity;


@RunWith(AndroidJUnit4.class)
public class MainActivityToolbarTest extends MainActivityToolbar{

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void setUp(){
        super.device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        super.context = mActivityRule.getActivity().getBaseContext();
    }

}

