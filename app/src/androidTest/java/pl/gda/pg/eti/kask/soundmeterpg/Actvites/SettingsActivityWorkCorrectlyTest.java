package pl.gda.pg.eti.kask.soundmeterpg.Actvites;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.NumberPicker;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.SettingsActivity;
import pl.gda.pg.eti.kask.soundmeterpg.R;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.ConnectionInternetDetector;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.PreferenceParser;
import pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNot.not;
import static pl.gda.pg.eti.kask.soundmeterpg.PreferenceTestHelper.*;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.TIME_OUT;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.turnOffGPS;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.turnOffGPSPermission;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.turnOffInternetData;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.turnOnAllPermission;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.turnOnGPS;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.turnOnGPSPermission;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.turnOnInternetData;

/**
 * Created by Daniel on 24.07.2016 :) at 12:11 :).
 */
@RunWith(AndroidJUnit4.class)
public class SettingsActivityWorkCorrectlyTest {
    private Context context;
    private SharedPreferences prefs;
    private UiDevice device;

    @Rule
    public final ActivityTestRule<SettingsActivity> mActivityRule = new ActivityTestRule<>(
            SettingsActivity.class);

    String[] permissions = new String[] {
            android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.ACCESS_NETWORK_STATE,
            android.Manifest.permission.ACCESS_FINE_LOCATION};

    @Before
    public void setUp() throws Exception {
        context = mActivityRule.getActivity().getBaseContext();
        prefs = PreferenceManager.getDefaultSharedPreferences(mActivityRule.getActivity());
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        turnOnGPS(device,context);
        turnOnAllPermission(device,context);

    }

    @Test
    public void isPrivateDataWorksCorrectly() {
//        selectPreference(R.string.recording_audio_key_preference,prefs,context);
//        uncheckPreference(R.string.private_data_key_preference,prefs,context);
//        uncheckPreference(R.string.working_in_background_key_preference,prefs,context);
//
//        isPreferenceNotEnabled(R.string.gps_key_preference,context);
//        isPreferenceNotEnabled(R.string.internet_key_preference,context);
//        isPreferenceNotEnabled(R.string.internal_storage_key_preference,context);
//
//        selectPreference(R.string.private_data_key_preference,prefs,context);
//        isPreferenceEnabled(R.string.gps_key_preference,context);
//        isPreferenceEnabled(R.string.internet_key_preference,context);
//        isPreferenceEnabled(R.string.internal_storage_key_preference,context);
    }

    @Test
    public void isWorkingInBackgroundWorksCorrectly() {
//        selectPreference(R.string.recording_audio_key_preference,prefs,context);
//        uncheckPreference(R.string.private_data_key_preference,prefs,context);
//        uncheckPreference(R.string.working_in_background_key_preference,prefs,context);
//
//        isPreferenceNotEnabled(R.string.gps_key_preference,context);
//        isPreferenceNotEnabled(R.string.internet_key_preference,context);
//        isPreferenceNotEnabled(R.string.internal_storage_key_preference,context);
//
//        selectPreference(R.string.working_in_background_key_preference,prefs,context);
//        isPreferenceNotEnabled(R.string.gps_key_preference,context);
//        isPreferenceNotEnabled(R.string.internet_key_preference,context);
//        isPreferenceEnabled(R.string.internal_storage_key_preference,context);
    }

    @Test
    public void isWorkingInBackgroundAndPrivateDataWorkCorrectly() {
//        selectPreference(R.string.recording_audio_key_preference,prefs,context);
//        selectPreference(R.string.private_data_key_preference,prefs,context);
//        selectPreference(R.string.working_in_background_key_preference,prefs,context);
//
//        isPreferenceEnabled(R.string.gps_key_preference,context);
//        isPreferenceEnabled(R.string.internet_key_preference,context);
//        isPreferenceEnabled(R.string.internal_storage_key_preference,context);
    }

    @Test
    public void isRecordingAudioWorksCorrectly() {
//        uncheckPreference(R.string.recording_audio_key_preference,prefs,context);
//
//        isPreferenceNotEnabled(R.string.private_data_key_preference,context);
//        isPreferenceNotEnabled(R.string.working_in_background_key_preference,context);
//        isPreferenceNotEnabled(R.string.gps_key_preference,context);
//        isPreferenceNotEnabled(R.string.internet_key_preference,context);
//        isPreferenceNotEnabled(R.string.internal_storage_key_preference,context);
//
//        selectPreference(R.string.recording_audio_key_preference,prefs,context);
//
//        isPreferenceEnabled(R.string.private_data_key_preference,context);
//        isPreferenceEnabled(R.string.working_in_background_key_preference,context);
    }

    @Test
    public void isGPSWorksCorrectly() throws Exception{
//        setUpGPSPreference(prefs, context);
//
//        turnOnGPS(device,context);
//        selectPreference(R.string.gps_key_preference,prefs,context);
//        isPreferenceChecked(R.string.gps_key_preference,context);
//
//        uncheckPreference(R.string.gps_key_preference,prefs,context);
//        isPreferenceNotChecked(R.string.gps_key_preference,context);
//
//        turnOffGPS(device,context);
//
//        selectPreference(R.string.gps_key_preference,prefs,context);
//        pressButtonWithTextOnDialog("android:id/button1",device);
//        device.pressBack();
//        isPreferenceNotChecked(R.string.gps_key_preference,context);
//
//        selectPreference(R.string.gps_key_preference,prefs,context);
//        pressButtonWithTextOnDialog("android:id/button2",device);
//        isPreferenceNotChecked(R.string.gps_key_preference,context);
//
//        selectPreference(R.string.gps_key_preference,prefs,context);
//        pressButtonWithTextOnDialog("android:id/button1",device);
//        switchLocation();
//        device.pressBack();
//        isPreferenceChecked(R.string.gps_key_preference,context);
    }

    public static void setUpGPSPreference(SharedPreferences prefs, Context context) {
//        selectPreference(R.string.recording_audio_key_preference,prefs,context);
//        selectPreference(R.string.private_data_key_preference,prefs,context);
//        selectPreference(R.string.working_in_background_key_preference,prefs,context);
//
//        isPreferenceEnabled(R.string.gps_key_preference,context);
//
//        uncheckPreference(R.string.gps_key_preference,prefs,context);
//        isPreferenceNotChecked(R.string.gps_key_preference,context);
    }

    private void switchLocation() throws UiObjectNotFoundException {
        if(Build.VERSION.SDK_INT > 19) {
            UiObject switchLocation = device.findObject(new UiSelector().resourceId("com.android.settings:id/switch_widget"));
            switchLocation.click();
        }else{
            UiObject switchLocation = device.findObject(new UiSelector().textStartsWith("OFF"));
            switchLocation.click();
        }
    }

    public static void pressButtonWithTextOnDialog(String id, UiDevice device) throws UiObjectNotFoundException {
        UiObject button = device.findObject(new UiSelector().resourceId(id));
        button.waitForExists(TIME_OUT);
        button.click();
        button.waitUntilGone(UIAutomotorTestHelper.TIME_OUT);
    }

    @Test
    public void isInternetWorksCorrectly() throws UiObjectNotFoundException {
//        setUpInternetPreference(prefs,context);
//
//        turnOnInternetData(device,context);
//        selectPreference(R.string.internet_key_preference,prefs,context);
//        isPreferenceChecked(R.string.internet_key_preference,context);
//
//        uncheckPreference(R.string.internet_key_preference,prefs,context);
//        isPreferenceNotChecked(R.string.internet_key_preference,context);
//
//        turnOffInternetData(device,context);
//
//        selectPreference(R.string.internet_key_preference,prefs,context);
//        pressButtonWithTextOnDialog("android:id/button1",device);
//        device.pressBack();
//        isPreferenceNotChecked(R.string.internet_key_preference,context);
//
//        selectPreference(R.string.internet_key_preference,prefs,context);
//        pressButtonWithTextOnDialog("android:id/button2",device);
//        isPreferenceNotChecked(R.string.internet_key_preference,context);
//
//        selectPreference(R.string.internet_key_preference,prefs,context);
//        pressButtonWithTextOnDialog("android:id/button1",device);
//        turnOnInternetData(device,context);
//        device.pressBack();
//        isPreferenceChecked(R.string.internet_key_preference,context);
    }

    public static void setUpInternetPreference(SharedPreferences prefs, Context context) {
        selectPreference(R.string.recording_audio_key_preference,prefs,context);
        selectPreference(R.string.private_data_key_preference,prefs,context);
        selectPreference(R.string.working_in_background_key_preference,prefs,context);

        isPreferenceEnabled(R.string.internet_key_preference,context);

        uncheckPreference(R.string.internet_key_preference,prefs,context);
        isPreferenceNotChecked(R.string.internet_key_preference,context);
    }

    @Test
    public void isInternalStorageWorksCorrectly(){
//        selectPreference(R.string.recording_audio_key_preference,prefs,context);
//        selectPreference(R.string.private_data_key_preference,prefs,context);
//        selectPreference(R.string.working_in_background_key_preference,prefs,context);
//
//        isPreferenceEnabled(R.string.internal_storage_key_preference,context);
//
//        uncheckPreference(R.string.internal_storage_key_preference,prefs,context);
//        isPreferenceNotChecked(R.string.internal_storage_key_preference,context);
//
//        selectPreference(R.string.internal_storage_key_preference,prefs,context);
//        isPreferenceChecked(R.string.internal_storage_key_preference,context);
   }

    @Test
    public void isSeekBarWorksCorrectly(){
//        selectPreference(R.string.recording_audio_key_preference,prefs,context);
//        selectPreference(R.string.private_data_key_preference,prefs,context);
//        selectPreference(R.string.working_in_background_key_preference,prefs,context);
//        uncheckPreference(R.string.internal_storage_key_preference,prefs,context);
//
//        int title = R.string.title_seek_bar_preference;
//        String key = context.getString(R.string.measurements_in_storage_key_preference);
//
//        DataInteraction interaction = findPreferencesOnView(key, title);
//        interaction.check(matches(not(isEnabled())));
//
//        selectPreference(R.string.internal_storage_key_preference,prefs,context);
//        interaction.check(matches(isEnabled()));
//
//        DataInteraction seekBar = interaction.onChildView(allOf(withId(R.id.seek_bar_preference)));
//        seekBar.perform(swipeRight());
//        seekBar.perform(swipeLeft());
    }


}
