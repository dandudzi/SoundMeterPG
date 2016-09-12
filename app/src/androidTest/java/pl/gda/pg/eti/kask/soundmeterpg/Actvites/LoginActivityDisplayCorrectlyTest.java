package pl.gda.pg.eti.kask.soundmeterpg.Actvites;

import android.os.RemoteException;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.LoginActivity;

/**
 * Created by Daniel on 21.08.2016 at 15:36 :).
 */
@RunWith(AndroidJUnit4.class)
public class LoginActivityDisplayCorrectlyTest extends LoginActivityDisplayCorrectly{


    @Rule
    public final ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
            LoginActivity.class);

    @Before
    public void setUp() throws RemoteException {
        super.context = mActivityRule.getActivity().getBaseContext();
    }

}
