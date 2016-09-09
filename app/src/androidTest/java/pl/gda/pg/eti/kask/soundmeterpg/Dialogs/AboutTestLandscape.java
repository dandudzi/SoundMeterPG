package pl.gda.pg.eti.kask.soundmeterpg.Dialogs;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.MainActivity;
import pl.gda.pg.eti.kask.soundmeterpg.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openContextualActionModeOverflowMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static pl.gda.pg.eti.kask.soundmeterpg.OrientationChangeAction.orientationLandscape;

/**
 * Created by Daniel on 06.09.2016 at 13:08 :).
 */
@RunWith(AndroidJUnit4.class)
public class AboutTestLandscape extends  AboutDisplayCorrectly{

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public  void setUp() throws InterruptedException {
        super.context = mActivityRule.getActivity().getBaseContext();
        super.activity = mActivityRule.getActivity();

        onView(isRoot()).perform(orientationLandscape());
        openContextualActionModeOverflowMenu();
        onView(ViewMatchers.withText(R.string.title_about_dialog)).perform(click());
    }
//TODO jeżeli nie będzie błędów z landscape usunąć
  /*  @After
    public void after(){
        UiObject view = device.findObject(new UiSelector().resourceId(UIAutomotorTestHelper.MEASURE_ID));
        if(!view.exists())
            device.pressBack();
        view.waitForExists(UIAutomotorTestHelper.TIME_OUT);
        onView(isRoot()).perform(orientationPortrait());
    }*/
}
