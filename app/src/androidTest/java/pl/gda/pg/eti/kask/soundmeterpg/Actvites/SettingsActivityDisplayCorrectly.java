package pl.gda.pg.eti.kask.soundmeterpg.Actvites;

import android.content.Context;
import android.support.test.espresso.DataInteraction;
import android.support.v7.widget.AppCompatImageView;
import android.widget.RelativeLayout;

import org.junit.Ignore;
import org.junit.Test;

import pl.gda.pg.eti.kask.soundmeterpg.R;
import pl.gda.pg.eti.kask.soundmeterpg.TextViewTestHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static pl.gda.pg.eti.kask.soundmeterpg.PreferenceTestHelper.findPreferencesOnView;
import static pl.gda.pg.eti.kask.soundmeterpg.PreferenceTestHelper.getCheckbox;

/**
 * Created by Daniel on 06.09.2016 at 12:35 :).
 */
@Ignore
public class SettingsActivityDisplayCorrectly {

    Context context;

    @Test
    public void isApplicationSettingsDisplayCorrectly() {
        onView(withText(R.string.application_settings)).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void isPrivateDataDisplayCorrectly() {
        int titleId = R.string.private_data_title_preference;
        int summaryId = R.string.private_data_summary_preference;
        String key = context.getString(R.string.private_data_key_preference);

        isPreferenceDisplayCorrectly(key,titleId,summaryId);
    }

    @Test
    public void isWorkingInBackgroundDisplayCorrectly() {
        int titleId = R.string.working_in_background_title_preference;
        int summaryId = R.string.working_in_background_summary_preference;
        String key = context.getString(R.string.working_in_background_key_preference);
        isPreferenceDisplayCorrectly(key,titleId,summaryId);
    }


    @Test
    public void isDeviceSettingsDisplayCorrectly() {
        onView(withText(R.string.device_settings)).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void isRecordingAudioDisplayCorrectly() {
        int titleId = R.string.recording_audio_title_preference;
        int summaryId = R.string.recording_audio_summary_preference;
        String key = context.getString(R.string.recording_audio_key_preference);

        isPreferenceDisplayCorrectly(key,titleId,summaryId);
    }

    @Test
    public void isGPSDisplayCorrectly() {
        int titleId = R.string.gps_title_preference;
        int summaryId = R.string.gps_summary_preference;
        String key = context.getString(R.string.gps_key_preference);

        isPreferenceDisplayCorrectly(key,titleId,summaryId);
    }

    @Test
    public void isInternetDisplayCorrectly() {
        int titleId = R.string.internet_title_preference;
        int summaryId = R.string.internet_summary_preference;
        String key = context.getString(R.string.internet_key_preference);

        isPreferenceDisplayCorrectly(key,titleId,summaryId);
    }

    @Test
    public void isAccessToInternalStorageDisplayCorrectly() {
        int titleId = R.string.internal_storage_title_preference;
        int summaryId = R.string.internal_storage_summary_preference;
        String key = context.getString(R.string.internal_storage_key_preference);

        isPreferenceDisplayCorrectly(key,titleId,summaryId);
    }


    private void isPreferenceDisplayCorrectly(String key, int titleId, int summaryId){

        String title = context.getString(titleId);
        String summary = context.getString(summaryId);
        String iconName = AppCompatImageView.class.getName();
        String relativeLayoutName = RelativeLayout.class.getName();

        DataInteraction interaction = findPreferencesOnView(key, titleId, summaryId);

        interaction.check(matches(isCompletelyDisplayed()));

        DataInteraction icon = interaction.onChildView(allOf(withClassName(is(iconName))));
        icon.check(matches(isCompletelyDisplayed()));

        interaction.onChildView(withClassName(is(relativeLayoutName)))
                .check(matches(isCompletelyDisplayed()));

        DataInteraction interactionTitle = interaction.onChildView(withText(title));
        TextViewTestHelper.testSinglelineTextView(interactionTitle, title);

        DataInteraction interactionSummary = interaction.onChildView(withText(summary));
        TextViewTestHelper.testMultilineTextView(interactionSummary, summary);

        interaction.onChildView(withId(android.R.id.widget_frame)).check(matches(isCompletelyDisplayed()));

        DataInteraction checkBox  = getCheckbox(interaction);
        checkBox.check(matches(isCompletelyDisplayed()));
    }
}
