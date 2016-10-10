package pl.gda.pg.eti.kask.soundmeterpg;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.support.test.espresso.DataInteraction;
import android.support.v7.widget.AppCompatCheckBox;

import org.hamcrest.Matcher;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.PreferenceMatchers.withKey;
import static android.support.test.espresso.matcher.PreferenceMatchers.withSummary;
import static android.support.test.espresso.matcher.PreferenceMatchers.withTitle;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Created by Daniel on 03.08.2016 :) at 12:12 :).
 */
public class PreferenceTestHelper {

    public static void isPreferenceChecked(int keyId, Context context) {
        DataInteraction gps = getDataInteraction(keyId, context);
        DataInteraction checkBox = getCheckbox(gps);
        checkBox.check(matches(isChecked()));
    }

    public static void isPreferenceNotChecked(int keyId, Context context) {
        DataInteraction gps = getDataInteraction(keyId, context);
        DataInteraction checkBox = getCheckbox(gps);
        checkBox.check(matches(not(isChecked())));
    }

    public static void isPreferenceEnabled(int keyId, Context context) {
        DataInteraction gps = getDataInteraction(keyId, context);
        DataInteraction checkBox = getCheckbox(gps);
        checkBox.check(matches(isEnabled()));
    }

    public static void isPreferenceNotEnabled(int keyId, Context context) {
        DataInteraction gps = getDataInteraction(keyId, context);
        DataInteraction checkBox = getCheckbox(gps);
        checkBox.check(matches(not(isEnabled())));
    }

    public static void selectPreference(int keyId, SharedPreferences prefs, Context context) {
        DataInteraction interaction = getDataInteraction(keyId,context);
        DataInteraction checkBox = getCheckbox(interaction);
        Boolean isChecked  = prefs.getBoolean(context.getString(keyId),true);
        if(!isChecked)
            checkBox.check(matches(not(isChecked()))).perform(click());
    }

    public static void uncheckPreference(int keyId, SharedPreferences prefs, Context context) {
        DataInteraction interaction = getDataInteraction(keyId,context);
        DataInteraction checkBox = getCheckbox(interaction);
        Boolean isChecked  = prefs.getBoolean(context.getString(keyId),true);
        if(isChecked)
            checkBox.check(matches(isChecked())).perform(click());
    }
    public static void setPrivilages(String keyId, SharedPreferences prefs, boolean enabled){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(keyId, enabled);
        editor.commit();
    }

    private static DataInteraction getDataInteraction(int keyId, Context context){
        int titleId;
        int summaryId;
        String key = context.getString(keyId);
        switch(keyId){
            case R.string.private_data_key_preference:
                titleId = R.string.private_data_title_preference;
                summaryId = R.string.private_data_summary_preference;
                break;
            case R.string.working_in_background_key_preference:
                titleId = R.string.working_in_background_title_preference;
                summaryId = R.string.working_in_background_summary_preference;
                break;
            case R.string.recording_audio_key_preference:
                titleId = R.string.recording_audio_title_preference;
                summaryId = R.string.recording_audio_summary_preference;
                break;
            case R.string.gps_key_preference:
                titleId = R.string.gps_title_preference;
                summaryId = R.string.gps_summary_preference;
                break;
            case R.string.internet_key_preference:
                titleId = R.string.internet_title_preference;
                summaryId = R.string.internet_summary_preference;
                break;
            case R.string.internal_storage_key_preference:
                titleId = R.string.internal_storage_title_preference;
                summaryId = R.string.internal_storage_summary_preference;
                break;
            default:
                titleId = 0;
                summaryId = 0;
                break;
        }

        return findPreferencesOnView(key,titleId,summaryId);
    }

    public static DataInteraction findPreferencesOnView(String key, int titleId, int summaryId) {
        return onData(getDataMatcher(key, titleId, summaryId));
    }

    public static DataInteraction findPreferencesOnView(String key, int titleId) {
        return onData(getDataMatcher(key, titleId));
    }

    private static Matcher getDataMatcher(String key, int titleId, int summaryId) {
        return allOf(
                is(instanceOf(Preference.class)),
                withKey(key),
                withTitle(titleId),
                withSummary(summaryId));
    }

    private static Matcher getDataMatcher(String key, int titleId) {
        return allOf(
                is(instanceOf(Preference.class)),
                withKey(key),
                withTitle(titleId)
                );
    }

    public static DataInteraction getCheckbox(DataInteraction interaction){
        return interaction.onChildView(withId(android.R.id.widget_frame))
                .onChildView(withClassName(is(AppCompatCheckBox.class.getName())));
    }
}
