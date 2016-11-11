package pl.gda.pg.eti.kask.soundmeterpg.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Switch;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import pl.gda.pg.eti.kask.soundmeterpg.IntentActionsAndKeys;
import pl.gda.pg.eti.kask.soundmeterpg.R;

import static android.R.attr.dialogMessage;
import static android.R.attr.subMenuArrow;

/**
 * Created by Daniel on 10.10.2016.
 */

public class CalibrateDialog extends DialogPreference implements NumberPicker.Formatter {
    private final int MIN = -50;
    private final int MAX = 50;
    private final int DEFAULT_VALUE = 0;
    private int currentValue;
    private NumberPicker picker;
    private Button cancel_btn;
    private Button calibrate_btn;

    @Override
    public View getView(View convertView, ViewGroup parent) {
        View view = super.getView(convertView, parent);
        ImageView imageView = (ImageView)view.findViewById(R.id.image_view_preference);
        imageView.setImageResource(R.mipmap.ic_calibration);
        return  view;
    }

    public CalibrateDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.calibrate_dialog);
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);
        builder.setTitle(null);
        builder.setPositiveButton(null, null);
        builder.setNegativeButton(null, null);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        initializeNumberPicker(view);
        initializeCancelBtn(view);
        initializeCalibrateBtn(view);
    }

    private void initializeNumberPicker(View view) {
        picker= (NumberPicker)view.findViewById(R.id.number_picker_calibrate_dialog);
        picker.setMinValue(DEFAULT_VALUE);
        picker.setMaxValue(MAX - MIN);
        currentValue = getPersistedInt(DEFAULT_VALUE);
        /*Number picker ma taki błąd że nie renderuje się za pierwszym razem.
        Dlatego wywołuje metodę zmiany wartości o 1, zeby widok się zrenderował.
        Dlatego jeżeli jest wartości dodatnia to odejmuję 1, ponieważ metoda wywoływana zwiększa wartość o jeden.
        Zagmatwane ale wyczytałem że to jest taki issue i ze nie został on jeszcze naprawiony
         */
        if(currentValue < 0)
            picker.setValue(currentValue - MIN);
        else
            picker.setValue(currentValue+MIN-1);
        picker.setFormatter(this);
        renderNumberPicker();
    }

    private void renderNumberPicker() {
        try {
            Method method = picker.getClass().getDeclaredMethod("changeValueByOne", boolean.class);
            method.setAccessible(true);
            method.invoke(picker, true);
        } catch (NoSuchMethodException e) {
            // e.printStackTrace();
        } catch (IllegalArgumentException e) {
            //  e.printStackTrace();
        } catch (IllegalAccessException e) {
            //  e.printStackTrace();
        } catch (InvocationTargetException e) {
            //e.printStackTrace();
        }
    }

    @Override
    public String format(int i ){
        return  Integer.toString(i + MIN);
    }
    private void initializeCancelBtn(View view) {
        cancel_btn = (Button) view.findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().cancel();
            }
        });
    }

    private void initializeCalibrateBtn(View view) {
        calibrate_btn = (Button) view.findViewById(R.id.calibrate_btn);
        calibrate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentValue = picker.getValue() - MAX;
                Log.i("DialogPreference","value is :" + Integer.toString(currentValue));
                persistInt(currentValue );
                getDialog().dismiss();
            }
        });
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInteger(index, DEFAULT_VALUE);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            currentValue = this.getPersistedInt(DEFAULT_VALUE);
        } else {
            currentValue = (Integer) defaultValue;
            persistInt(currentValue);
        }
    }

}
