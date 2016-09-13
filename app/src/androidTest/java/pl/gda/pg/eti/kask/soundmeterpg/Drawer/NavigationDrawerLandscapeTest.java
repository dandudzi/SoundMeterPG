package pl.gda.pg.eti.kask.soundmeterpg.Drawer;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.MainActivity;
import pl.gda.pg.eti.kask.soundmeterpg.Internet.MyAccountManager;
import pl.gda.pg.eti.kask.soundmeterpg.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static pl.gda.pg.eti.kask.soundmeterpg.OrientationChangeAction.orientationLandscape;

/**
 * Created by Daniel on 09.09.2016 at 19:55 :).
 */
@RunWith(AndroidJUnit4.class)
public class NavigationDrawerLandscapeTest extends NavigationDrawerDisplayCorrectly {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void setUp(){
        super.device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        super.context =  mActivityRule.getActivity().getBaseContext();
        super.manager =  new MyAccountManager(mActivityRule.getActivity());
        onView(isRoot()).perform(orientationLandscape());
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
    }

    @Override
    @Test
    public void isLoginOrLogoutDisplayCorrectly(){
        onView(withId(R.id.left_drawer)).perform(swipeUp());
        super.isLoginOrLogoutDisplayCorrectly();
    }

    @Override
    @Test
    public void isSettingsDisplayCorrectly(){
        onView(withId(R.id.left_drawer)).perform(swipeUp());
        super.isSettingsDisplayCorrectly();
    }

    @Override
    @Test
    public void relativePositionTest(){
        onView(withId(R.id.left_drawer)).perform(swipeUp());
        super.isSettingsDisplayCorrectly();
    }

}
