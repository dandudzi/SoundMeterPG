package pl.gda.pg.eti.kask.soundmeterpg.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObjectNotFoundException;

import pl.gda.pg.eti.kask.soundmeterpg.R;

import static pl.gda.pg.eti.kask.soundmeterpg.PreferenceTestHelper.selectPreference;
import static pl.gda.pg.eti.kask.soundmeterpg.PreferenceTestHelper.uncheckPreference;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.SETTINGS_ITEM;
import static pl.gda.pg.eti.kask.soundmeterpg.UIAutomotorTestHelper.openItemInDrawer;

/**
 * Created by Daniel on 14.10.2016.
 */

public class MeasureTestHelper {
    public static void turnAllSettingsPermission(UiDevice device, SharedPreferences prefs, Context context) throws UiObjectNotFoundException {
        openItemInDrawer(SETTINGS_ITEM,device);
        selectPreference(R.string.recording_audio_key_preference,prefs,context);
        selectPreference(R.string.private_data_key_preference,prefs,context);
        uncheckPreference(R.string.working_in_background_key_preference,prefs,context);
        selectPreference(R.string.gps_key_preference,prefs,context);
        selectPreference(R.string.internet_key_preference,prefs,context);
        selectPreference(R.string.internal_storage_key_preference,prefs,context);
        device.pressBack();
    }

    public static void turnOffMicrophonePermission(UiDevice device, SharedPreferences prefs, Context context) throws UiObjectNotFoundException {
        openItemInDrawer(SETTINGS_ITEM,device);
        uncheckPreference(R.string.recording_audio_key_preference,prefs,context);
        device.pressBack();
    }
}
