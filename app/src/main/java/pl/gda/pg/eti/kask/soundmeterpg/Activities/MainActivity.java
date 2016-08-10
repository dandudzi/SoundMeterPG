package pl.gda.pg.eti.kask.soundmeterpg;


import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import pl.gda.pg.eti.kask.soundmeterpg.Exception.NullRecordException;
import pl.gda.pg.eti.kask.soundmeterpg.Exception.OverrangeException;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_activity_toolbar);
        setSupportActionBar(myToolbar);

        //noinspection ConstantConditions
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //TODO ikona
        getSupportActionBar().setIcon(R.mipmap.ic_politechnika);
        //TODO by nacisnąć ikonę
        //getSupportActionBar().setHomeActionContentDescription(R.string.main_icon_description);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_action:
                Log.i("Toolbar","Opening settings");
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.about_action:
                Log.i("Toolbar","Opening about dialog");
                showAlertDialog();
                return true;

            case R.id.faq_action:
                Log.i("Toolbar","Opening FAQ");
                AlertDialog faqAlert = FAQDialog.create(this);
                faqAlert.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void showAlertDialog() {
        AlertDialog aboutAlert = null;
        try {
            aboutAlert = AboutDialog.create(this);
        } catch (VersionException | LastDateException e) {
            e.printStackTrace();
        }
        if(aboutAlert!=null)
            aboutAlert.show();
    }

    public void startMyService(View v) {
        Intent serviceIntent = new Intent(this, NoiseLevel.class);
        serviceIntent.addCategory("NoiseLevel");
        startService(serviceIntent);
    }

    public void stopMyService(View v) {
        Intent serviceIntent = new Intent(this, NoiseLevel.class);
        serviceIntent.addCategory("NoiseLevel");
        stopService(serviceIntent);
    }


    public void insertData(View btn) throws IOException {
        //TODO to tylko bylo do prototypu
        //new Insert(getBaseContext()).execute(tmpRecorder.soundDb(1.0));
    }

    public void showGPS(View w) {
        DataBaseHandler dataBaseHandler = new DataBaseHandler(getBaseContext());
        try {
            dataBaseHandler.insert(new Probe(32.3, 322.23, 12.11));
        } catch (NullRecordException e) {
            e.printStackTrace();
        } catch (OverrangeException e) {
            e.printStackTrace();
        }
          /*  _gps = new Localization(MainActivity.this);

            if(_gps.canGetLocation()) {
                double latitude = _gps.getLatitude();
                double longitude = _gps.getLongitude();

                Toast.makeText(
                        getApplicationContext(),
                        "Your Location is -\nLat: " + latitude + "\nLong: "
                                + longitude, Toast.LENGTH_LONG).show();
            } else {
                _gps.showSettingsAlert();
            }
*/
    }
}
