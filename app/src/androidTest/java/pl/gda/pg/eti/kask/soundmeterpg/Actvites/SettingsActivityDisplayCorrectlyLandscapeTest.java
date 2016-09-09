package pl.gda.pg.eti.kask.soundmeterpg.Actvites;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.SettingsActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static pl.gda.pg.eti.kask.soundmeterpg.OrientationChangeAction.orientationLandscape;
import static pl.gda.pg.eti.kask.soundmeterpg.OrientationChangeAction.orientationPortrait;

/**
 * Created by Daniel on 06.09.2016 at 12:41 :).
 */
@RunWith(AndroidJUnit4.class)
public class SettingsActivityDisplayCorrectlyLandscapeTest extends SettingsActivityDisplayCorrectly {

    @Rule
    public final ActivityTestRule<SettingsActivity> mActivityRule = new ActivityTestRule<>(
            SettingsActivity.class);

    @Before
    public void initSettings() {
        super.context = mActivityRule.getActivity().getBaseContext();
        onView(isRoot()).perform(orientationLandscape());
    }

    @After
    public void after(){
        onView(isRoot()).perform(orientationPortrait());
    }
}
