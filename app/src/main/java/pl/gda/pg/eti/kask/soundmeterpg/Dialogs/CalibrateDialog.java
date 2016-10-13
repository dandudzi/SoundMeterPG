package pl.gda.pg.eti.kask.soundmeterpg.Dialogs;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Switch;

import pl.gda.pg.eti.kask.soundmeterpg.R;

import static android.R.attr.dialogMessage;

/**
 * Created by Daniel on 10.10.2016.
 */

public class CalibrateDialog extends DialogPreference {
    private final int MAX = 50;
    private final int DEFAULT_VALUE = 0;
    private int currentValue;
    private NumberPicker picker;
    private Switch isNegative;

    public CalibrateDialog(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.calibrate_dialog);
        setPositiveButtonText("Set");
        setNegativeButtonText(android.R.string.cancel);


    }

    @Override
    protected void onBindDialogView(View view) {
        picker= (NumberPicker)view.findViewById(R.id.number_picker_calibrate_dialog);
        isNegative = (Switch) view.findViewById(R.id.switch_calibration_dialog);
        picker.setMaxValue(MAX);
        currentValue = getPersistedInt(DEFAULT_VALUE);
        if(currentValue <0) {
            currentValue *= -1;
            isNegative.setChecked(true);
        }
        picker.setValue(currentValue);
        super.onBindDialogView(view);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInteger(index, DEFAULT_VALUE);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            // Restore existing state
            currentValue = this.getPersistedInt(DEFAULT_VALUE);
        } else {
            // Set default state from the XML attribute
            currentValue = (Integer) defaultValue;
            persistInt(currentValue);
        }
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if(positiveResult) {
            currentValue = picker.getValue();
            if(isNegative.isChecked())
                currentValue *= -1;
            persistInt(currentValue);
        }
    }
}
