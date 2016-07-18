package pl.gda.pg.eti.kask.soundmeterpg;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Daniel on 10.07.2016.
 */
public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }


}
