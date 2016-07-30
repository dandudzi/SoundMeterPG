package pl.gda.pg.eti.kask.soundmeterpg.Actvites;


import android.content.Context;
import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.AppCompatImageView;
import android.widget.RelativeLayout;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.SettingsActivity;
import pl.gda.pg.eti.kask.soundmeterpg.R;
import pl.gda.pg.eti.kask.soundmeterpg.TesterHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by Daniel on 19.07.2016 :).
 */
@RunWith(AndroidJUnit4.class)
public class SettingsActivityCorrectDisplayTest {
    private Context context;
    @Rule
    public ActivityTestRule<SettingsActivity> mActivityRule = new ActivityTestRule<>(
            SettingsActivity.class);

    @Before
    public void initSettings() {
        context = mActivityRule.getActivity().getBaseContext();
    }

    @Test
    public void isApplicationSettingsDisplayCorrectly() {
        onView(ViewMatchers.withText(R.string.application_settings)).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void isPrivateDataDisplayCorrectly() {
        int titleId = R.string.title_private_data_preference;
        int summaryId = R.string.summary_private_data_preference;
        String key = context.getString(R.string.key_private_data_preference);

        isPreferenceDisplayCorrectly(key,titleId,summaryId);
    }

    @Test
    public void isWorkingInBackgroundDisplayCorrectly() {
        int titleId = R.string.title_working_in_background_preference;
        int summaryId = R.string.summary_working_in_background_preference;
        String key = context.getString(R.string.key_working_in_background_preference);
        isPreferenceDisplayCorrectly(key,titleId,summaryId);
    }


    @Test
    public void isDeviceSettingsDisplayCorrectly() {
        onView(withText(R.string.device_settings)).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void isRecordingAudioDisplayCorrectly() {
        int titleId = R.string.title_recording_audio_preference;
        int summaryId = R.string.summary_recording_audio_preference;
        String key = context.getString(R.string.key_recording_audio_preference);

        isPreferenceDisplayCorrectly(key,titleId,summaryId);
    }

    @Test
    public void isGPSDisplayCorrectly() {
        int titleId = R.string.title_gps_preference;
        int summaryId = R.string.summary_gps_preference;
        String key = context.getString(R.string.key_gps_preference);

        isPreferenceDisplayCorrectly(key,titleId,summaryId);
    }

    @Test
    public void isInternetDisplayCorrectly() {
        int titleId = R.string.title_internet_preference;
        int summaryId = R.string.summary_internet_preference;
        String key = context.getString(R.string.key_internet_preference);

        isPreferenceDisplayCorrectly(key,titleId,summaryId);
    }

    @Test
    public void isAccessToInternalStorageDisplayCorrectly() {
        int titleId = R.string.title_internal_storage_preference;
        int summaryId = R.string.summary_internal_storage_preference;
        String key = context.getString(R.string.key_internal_storage_preference);

        isPreferenceDisplayCorrectly(key,titleId,summaryId);
    }

    private void isPreferenceDisplayCorrectly(String key, int titleId, int summaryId){

        String title = context.getString(titleId);
        String summary = context.getString(summaryId);
        String iconName = AppCompatImageView.class.getName();
        String relativeLayoutName = RelativeLayout.class.getName();

        DataInteraction interaction = TesterHelper.getDataInteractionPreferences(key, titleId, summaryId);

        interaction.check(matches(isCompletelyDisplayed()));

        DataInteraction icon = interaction.onChildView(allOf(withClassName(is(iconName))));
        icon.check(matches(isCompletelyDisplayed()));

        interaction.onChildView(withClassName(is(relativeLayoutName)))
                .check(matches(isCompletelyDisplayed()));

        DataInteraction interactionTitle = interaction.onChildView(withText(title));
        TesterHelper.testSinglelineTextView(interactionTitle, title);

        DataInteraction interactionSummary = interaction.onChildView(withText(summary));
        TesterHelper.testMultilineTextView(interactionSummary, summary);

        interaction.onChildView(withId(android.R.id.widget_frame)).check(matches(isCompletelyDisplayed()));

        DataInteraction checkBox  = TesterHelper.getCheckboxPreferences(interaction);
        checkBox.check(matches(isCompletelyDisplayed()));
    }


}
