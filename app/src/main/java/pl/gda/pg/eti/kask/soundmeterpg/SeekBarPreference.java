package pl.gda.pg.eti.kask.soundmeterpg;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by Daniel on 29.09.2016.
 */
public class SeekBarPreference extends Preference implements
        SeekBar.OnSeekBarChangeListener {

    public static int maximum = 100;
    public static int interval = 5;
    private float oldValue = 25;
    private TextView Indicator;

    public SeekBarPreference(Context context) {
        super(context);
    }

    public SeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SeekBarPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        View layout;
        LayoutInflater inflater = (LayoutInflater)super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = inflater.inflate(R.layout.seek_bar_pref, null);
        Indicator = (TextView)layout.findViewById(R.id.textView3);
        SeekBar bar= (SeekBar)layout.findViewById(R.id.seekBar);
        bar.setOnSeekBarChangeListener(this);
        bar.setMax(maximum);
        bar.setProgress((int)oldValue);
        return layout;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        progress = Math.round(((float) progress) / interval) * interval;

        if (!callChangeListener(progress)) {
            seekBar.setProgress((int) this.oldValue);
            return;
        }

        seekBar.setProgress(progress);
        this.oldValue = progress;
        this.Indicator.setText(""+progress);
        updatePreference(progress);

        if(fromUser)
        notifyChanged();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    protected Object onGetDefaultValue(TypedArray ta, int index) {
        int dValue = ta.getInt(index, 25);

        return validateValue(dValue);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue,
                                     Object defaultValue) {
        int temp = restoreValue ? getPersistedInt(25) : (Integer) defaultValue;
        if (!restoreValue)
            persistInt(temp);

        this.oldValue = temp;
    }

    private int validateValue(int value) {
        if (value > maximum)
            value = maximum;
        else if (value < 0)
            value = 0;
        else if (value % interval != 0)
            value = Math.round(((float) value) / interval) * interval;

        return value;
    }

    private void updatePreference(int newValue) {
        SharedPreferences.Editor editor = getEditor();
        editor.putInt(getKey(), newValue);
        editor.commit();
    }
}