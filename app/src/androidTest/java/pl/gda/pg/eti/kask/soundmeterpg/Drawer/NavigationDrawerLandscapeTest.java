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

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
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
        super.rows = super.context.getResources().getStringArray(R.array.rows_list_drawer);
        onView(isRoot()).perform(orientationLandscape());
    }

}
