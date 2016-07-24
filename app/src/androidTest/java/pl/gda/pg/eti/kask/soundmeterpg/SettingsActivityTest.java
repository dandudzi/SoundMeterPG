package pl.gda.pg.eti.kask.soundmeterpg;


import android.content.Context;
import android.preference.Preference;
import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.PositionAssertions.isLeftOf;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.PreferenceMatchers.withKey;
import static android.support.test.espresso.matcher.PreferenceMatchers.withSummaryText;
import static android.support.test.espresso.matcher.PreferenceMatchers.withTitle;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static android.support.test.espresso.matcher.PreferenceMatchers.withSummary;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Created by Daniel on 19.07.2016 :).
 */
@RunWith(AndroidJUnit4.class)
public class SettingsActivityTest {
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
        onView(withText(R.string.application_settings)).check(matches(isCompletelyDisplayed()));
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

        DataInteraction interaction = getDataInteraction(key, titleId, summaryId);

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

        DataInteraction checkBox  = getCheckbox(interaction);
        checkBox.check(matches(isCompletelyDisplayed()));
    }

    private DataInteraction getDataInteraction(String key, int titleId, int summaryId) {
        return onData(allOf(
                is(instanceOf(Preference.class)),
                withKey(key),
                withTitle(titleId),
                withSummary(summaryId)));
    }

    private DataInteraction getCheckbox(DataInteraction interaction){
        return interaction.onChildView(withId(android.R.id.widget_frame))
                .onChildView(withClassName(is(AppCompatCheckBox.class.getName())));
    }

}
