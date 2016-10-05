package pl.gda.pg.eti.kask.soundmeterpg.Internet;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.MainActivity;
import pl.gda.pg.eti.kask.soundmeterpg.InformationAboutThisApplication;

import static org.junit.Assert.*;

/**
 * Created by Daniel on 17.08.2016 at 10:52 :).
 */
@Ignore
@RunWith(AndroidJUnit4.class)
public class MyAccountManagerTest {
    private Activity activity;
    private MyAccountManager manager;
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void setUp() throws Exception {
        activity = mActivityRule.getActivity();
    }

    @Test
    public void testIsLogIn() throws Exception {
        manager =  new MyAccountManager(activity);
        manager.logIn(MyAccountManager.login,MyAccountManager.password, InformationAboutThisApplication.getMACAddress());
        while(true) {
            if (!(manager.getProgress() < MyAccountManager.MAX_DURATION_OF_LOG_IN)) break;
        }
        assertEquals("Is log in",true,manager.isLogIn());
        manager.logOut();
        assertEquals("Is log in",false,manager.isLogIn());
    }

    @Test
    public void testLogIn() throws Exception {
        manager =  new MyAccountManager(activity);
        manager.logIn(MyAccountManager.login,MyAccountManager.password, InformationAboutThisApplication.getMACAddress());
        while(true) {
            if (!(manager.getProgress() < MyAccountManager.MAX_DURATION_OF_LOG_IN)) break;
        }
        assertEquals("Is log in",true,manager.isLogIn());
    }

    @Test
    public void testLogOut() throws Exception {
        manager =  new MyAccountManager(activity);
        manager.logOut();
        assertEquals("Is log in",false,manager.isLogIn());
    }
}