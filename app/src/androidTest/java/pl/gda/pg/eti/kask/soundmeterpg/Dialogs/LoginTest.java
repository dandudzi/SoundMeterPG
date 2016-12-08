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
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Daniel on 24.07.2016 :) at 12:13 :).
 */
@RunWith(AndroidJUnit4.class)
public class LoginTest extends LoginDisplayCorrectly{

    @Rule
    public final ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
            LoginActivity.class);

    @Before
    public  void setUp(){
        super.context = mActivityRule.getActivity().getBaseContext();
        onView(withId(R.id.email_button_login_activity)).perform(click());
    }

}
