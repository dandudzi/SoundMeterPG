package pl.gda.pg.eti.kask.soundmeterpg.Actvites;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.SettingsActivity;

/**
 * Created by Daniel on 19.07.2016 :) at 12:12 :).
 */
@RunWith(AndroidJUnit4.class)
public class SettingsActivityDisplayCorrectlyTest extends  SettingsActivityDisplayCorrectly{
    @Rule
    public final ActivityTestRule<SettingsActivity> mActivityRule = new ActivityTestRule<>(
            SettingsActivity.class);

    @Before
    public void initSettings() {
        super.context = mActivityRule.getActivity().getBaseContext();
    }

}
