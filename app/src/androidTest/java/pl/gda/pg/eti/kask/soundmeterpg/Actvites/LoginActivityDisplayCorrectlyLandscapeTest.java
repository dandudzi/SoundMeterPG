package pl.gda.pg.eti.kask.soundmeterpg.Actvites;

import android.os.RemoteException;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.LoginActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static pl.gda.pg.eti.kask.soundmeterpg.OrientationChangeAction.orientationLandscape;

/**
 * Created by Daniel on 06.09.2016 at 12:22 :).
 */
@RunWith(AndroidJUnit4.class)
public class LoginActivityDisplayCorrectlyLandscapeTest extends LoginActivityDisplayCorrectly {

    @Rule
    public final ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
            LoginActivity.class);

    @Before
    public void setUp() throws RemoteException {
        super.context = mActivityRule.getActivity().getBaseContext();
        onView(isRoot()).perform(orientationLandscape());
    }

}
