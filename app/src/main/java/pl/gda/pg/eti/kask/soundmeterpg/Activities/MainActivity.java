package pl.gda.pg.eti.kask.soundmeterpg.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.LastDateException;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.VersionException;
import pl.gda.pg.eti.kask.soundmeterpg.FactorAlertDialog;
import pl.gda.pg.eti.kask.soundmeterpg.R;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_activity_toolbar);
        setSupportActionBar(myToolbar);

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
                AlertDialog faqAlert = FactorAlertDialog.createFAQDialog(this);
                faqAlert.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void showAlertDialog() {
        AlertDialog aboutAlert = null;
        try {
            aboutAlert = FactorAlertDialog.createAboutDialog(this);
        } catch (VersionException | LastDateException e) {
            e.printStackTrace();
        }
        if(aboutAlert!=null)
            aboutAlert.show();
    }

}
