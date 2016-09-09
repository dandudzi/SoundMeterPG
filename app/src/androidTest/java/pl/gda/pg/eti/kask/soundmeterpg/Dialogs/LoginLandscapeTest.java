package pl.gda.pg.eti.kask.soundmeterpg.Dialogs;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.LoginActivity;
import pl.gda.pg.eti.kask.soundmeterpg.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static pl.gda.pg.eti.kask.soundmeterpg.OrientationChangeAction.orientationLandscape;

/**
 * Created by Daniel on 06.09.2016 at 18:44 :).
 */
@RunWith(AndroidJUnit4.class)
public class LoginLandscapeTest extends LoginDisplayCorrectly {

    @Rule
    public final ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
            LoginActivity.class);

    @Before
    public  void setUp(){
        super.context = mActivityRule.getActivity().getBaseContext();
        onView(isRoot()).perform(orientationLandscape());
        onView(withId(R.id.login_button_login_activity)).perform(click());
    }

}
