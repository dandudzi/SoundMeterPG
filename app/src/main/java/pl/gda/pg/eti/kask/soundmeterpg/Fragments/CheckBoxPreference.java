package pl.gda.pg.eti.kask.soundmeterpg.Fragments;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import pl.gda.pg.eti.kask.soundmeterpg.R;

/**
 * Created by gierl on 29.10.2016.
 */

public class CheckBoxPreference extends android.preference.CheckBoxPreference{
    private int preferenceKey;
    @Override
    public View getView(View convertView, ViewGroup parent) {
        View v = super.getView(convertView, parent);
        ImageView imageView = (ImageView)v.findViewById(R.id.image_view_preference);
        switch (preferenceKey) {
            case R.string.private_data_key_preference:
                imageView.setImageResource(R.mipmap.ic_private_data);
                break;
            case R.string.working_in_background_key_preference:
                imageView.setImageResource(R.mipmap.ic_wokring_background);
                break;
            case R.string.recording_audio_key_preference:
                imageView.setImageResource(R.mipmap.ic_mic);
                break;
            case R.string.internal_storage_key_preference:
                imageView.setImageResource(R.mipmap.ic_internal_storage);
                break;
            case R.string.internet_key_preference:
                imageView.setImageResource(R.mipmap.ic_internet);
                break;
            case R.string.gps_key_preference:
                imageView.setImageResource(R.mipmap.ic_gps);
                break;
            case R.string.sending_measurement_key_preferenece :
                imageView.setImageResource(R.mipmap.ic_server);
                break;
        }
        return v;
    }

    public CheckBoxPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        preferenceKey = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "key",0);

    }
    public CheckBoxPreference(Context context) {
        super(context);
    }

    public CheckBoxPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
            preferenceKey = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "key",0);
          //  String xmlProvidedSize = context.getString(attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "key",0));
        }
}
