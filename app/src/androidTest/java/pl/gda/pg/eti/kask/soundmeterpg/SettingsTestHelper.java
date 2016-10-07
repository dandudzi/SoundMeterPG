package pl.gda.pg.eti.kask.soundmeterpg;

import android.support.test.uiautomator.UiDevice;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Daniel on 10.08.2016 at 17:37 :).
 */
public class SettingsTestHelper {
    public static void backFromSettings(UiDevice device) {
        device.pressBack();
        onView(withText(R.string.recording_audio_title_preference)).check(doesNotExist());
    }

    public static void isSettingDisplay() {
        onView(withId(R.id.settings)).check(matches(isCompletelyDisplayed()));
    }
}
