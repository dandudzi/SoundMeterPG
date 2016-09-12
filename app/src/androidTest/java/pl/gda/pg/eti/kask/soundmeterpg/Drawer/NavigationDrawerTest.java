package pl.gda.pg.eti.kask.soundmeterpg.Drawer;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.MainActivity;
import pl.gda.pg.eti.kask.soundmeterpg.R;


/**
 * Created by Daniel on 09.08.2016 at 17:02 :).
 */
@RunWith(AndroidJUnit4.class)
public class NavigationDrawerTest extends NavigationDrawerDisplayCorrectly {


    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void setUp(){
        super.device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        super.context =  mActivityRule.getActivity().getBaseContext();
        super.rows = super.context.getResources().getStringArray(R.array.rows_list_drawer);
    }

}
