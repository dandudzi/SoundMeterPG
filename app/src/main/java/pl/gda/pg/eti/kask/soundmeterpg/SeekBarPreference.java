package pl.gda.pg.eti.kask.soundmeterpg;
/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2011 - Lovro Pandžić
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * link - http://stackoverflow.com/questions/5050272/android-seekbarpreference
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

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
        layout = inflater.inflate(R.layout.seek_bar_preference, null);
        TextView textView =  (TextView)layout.findViewById(R.id.max_value_seek_bar);
        textView.setText(Integer.toString(maximum));
        Indicator = (TextView)layout.findViewById(R.id.current_value_seek_bar);
        SeekBar bar= (SeekBar)layout.findViewById(R.id.seek_bar_preference);
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