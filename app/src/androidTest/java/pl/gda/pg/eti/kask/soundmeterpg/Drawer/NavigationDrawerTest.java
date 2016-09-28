package pl.gda.pg.eti.kask.soundmeterpg.Drawer;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.MainActivity;
import pl.gda.pg.eti.kask.soundmeterpg.Internet.MyAccountManager;
import pl.gda.pg.eti.kask.soundmeterpg.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


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
        super.manager =  new MyAccountManager(mActivityRule.getActivity());
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
    }

}
