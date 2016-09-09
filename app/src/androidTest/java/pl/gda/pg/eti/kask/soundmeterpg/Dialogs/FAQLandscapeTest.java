package pl.gda.pg.eti.kask.soundmeterpg.Dialogs;

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
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static pl.gda.pg.eti.kask.soundmeterpg.OrientationChangeAction.orientationLandscape;

/**
 * Created by Daniel on 06.09.2016 at 17:54 :).
 */
@RunWith(AndroidJUnit4.class)
public class FAQLandscapeTest extends FAQDisplayCorrectly {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);


    @Before
    public  void setUp(){
        onView(isRoot()).perform(orientationLandscape());
        openContextualActionModeOverflowMenu();
        onView(withText(R.string.title_faq_dialog)).perform(click());
        super.context = mActivityRule.getActivity().getBaseContext();
    }

}
