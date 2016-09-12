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
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Daniel on 24.07.2016 :) at 12:13 :).
 */
@RunWith(AndroidJUnit4.class)
public class FAQTest extends FAQDisplayCorrectly{


    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);


    @Before
    public  void setUp(){
        openContextualActionModeOverflowMenu();
        onView(withText(R.string.title_faq_dialog)).perform(click());
        super.context = mActivityRule.getActivity().getBaseContext();
    }



}
