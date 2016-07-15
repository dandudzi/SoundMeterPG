package pl.gda.pg.eti.kask.soundmeterpg;


import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private TmpRecorder tmpRecorder; //this class must be reworked so its temporary solution

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_activity_toolbar);
        setSupportActionBar(myToolbar);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        //tmpRecorder = new TmpRecorder();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    public void onResume()
    {
        super.onResume();
        //tmpRecorder.startRecorder();
    }

    public void onPause()
    {
        super.onPause();
        //tmpRecorder.stopRecorder();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_action:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.about_action:
                AlertDialog aboutAlert = BuilderAlertDialog.getAboutDialog(this);
                aboutAlert.show();
                return true;

            default:

                return super.onOptionsItemSelected(item);

        }
    }

    public void insertData(View btn) throws IOException {
        new Insert(getBaseContext()).execute(tmpRecorder.soundDb(1.0));
    }
}
