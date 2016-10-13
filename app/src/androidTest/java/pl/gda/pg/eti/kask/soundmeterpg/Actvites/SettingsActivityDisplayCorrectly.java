package pl.gda.pg.eti.kask.soundmeterpg.Actvites;

import android.content.Context;
import android.support.test.espresso.DataInteraction;
import android.support.v7.widget.AppCompatImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Test;

import pl.gda.pg.eti.kask.soundmeterpg.R;
import pl.gda.pg.eti.kask.soundmeterpg.TextViewTestHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.PositionAssertions.isAbove;
import static android.support.test.espresso.assertion.PositionAssertions.isRightOf;
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

    @Test
    public void isSeekBarDisplayCorrectly() {
        int title = R.string.title_seek_bar_preference;
        String key = context.getString(R.string.measurements_in_storage_key_preference);

        DataInteraction interaction = findPreferencesOnView(key, title);
        interaction.check(matches(isCompletelyDisplayed()));

        DataInteraction titleSeekBar = interaction.onChildView(allOf(withText(title)));
        TextViewTestHelper.testSinglelineTextView(titleSeekBar,context.getString(title));

        DataInteraction minValue = interaction.onChildView(allOf(withId(R.id.min_value_seek_bar)));
        DataInteraction maxValue = interaction.onChildView(allOf(withId(R.id.max_value_seek_bar)));
        DataInteraction currentValue = interaction.onChildView(allOf(withId(R.id.current_value_seek_bar)));

        TextViewTestHelper.testSinglelineTextView(minValue);
        TextViewTestHelper.testSinglelineTextView(maxValue);
        TextViewTestHelper.testSinglelineTextView(currentValue);

        DataInteraction seekBar = interaction.onChildView(allOf(withId(R.id.seek_bar_preference)));
        seekBar.check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void isCalibrateDisplayCorrectly(){
        int title = R.string.calibrate_title_preference;
        String key = context.getString(R.string.calibrate_key_preference);

        DataInteraction calibration =  findPreferencesOnView(key,title);
        calibration.check(matches(isCompletelyDisplayed()));
        DataInteraction titleSeekBar = calibration.onChildView(allOf(withText(title)));
        TextViewTestHelper.testSinglelineTextView(titleSeekBar,context.getString(title));
        titleSeekBar.perform(click());

        Matcher titleDialog = withText(R.string.calibrate_title_preference);
        Matcher negativeValueText = withText(R.string.negative_value_calibration_dialog);
        Matcher switchDialog = withText(R.string.switch_text_calibration_dialog);
        Matcher db =  withText(R.string.calibrate_msg_preference);
        Matcher numberPicker = withClassName(Matchers.equalTo(NumberPicker.class.getName()));

        onView(titleDialog).check(matches(isCompletelyDisplayed()));
        onView(negativeValueText).check(matches(isCompletelyDisplayed()));
        onView(switchDialog).check(matches(isCompletelyDisplayed()));
        onView(db).check(matches(isCompletelyDisplayed()));
        onView(numberPicker).check(matches(isCompletelyDisplayed()));

        onView(titleDialog).check(isAbove(switchDialog));
        onView(titleDialog).check(isAbove(db));
        onView(titleDialog).check(isAbove(numberPicker));

        onView(db).check(isRightOf(numberPicker));

        onView(numberPicker).check(isRightOf(negativeValueText));
        onView(numberPicker).check(isRightOf(switchDialog));

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
