package pl.gda.pg.eti.kask.soundmeterpg.Activities;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import pl.gda.pg.eti.kask.soundmeterpg.Dialogs.About;
import pl.gda.pg.eti.kask.soundmeterpg.Dialogs.FAQ;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.LastDateException;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.VersionException;
import pl.gda.pg.eti.kask.soundmeterpg.Fragments.Measure;
import pl.gda.pg.eti.kask.soundmeterpg.Fragments.Measurements;
import pl.gda.pg.eti.kask.soundmeterpg.R;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView drawerList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        setFragmentContent(new Measure());
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setUpToolbar();
        setUpDrawer();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;

            case R.id.settings_action:
                Log.i("Toolbar","Opening settings");
                startActivity(SettingsActivity.class);
                return true;

            case R.id.about_action:
                Log.i("Toolbar","Opening about dialog");
                showAlertDialog();
                return true;

            case R.id.faq_action:
                Log.i("Toolbar","Opening FAQ");
                AlertDialog faqAlert = FAQ.create(this);
                faqAlert.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
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
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Log.i("Navigation Drawer","User chose position number "+item.getTitle());
        drawerLayout.closeDrawer(GravityCompat.START);

        switch(item.getItemId()){
            case R.id.settings_drawer:
                startActivity(SettingsActivity.class);
                break;
            case R.id.measure_drawer:
                setFragmentContent(new Measure());
                break;
            case R.id.measurements_drawer:
                setFragmentContent(new Measurements());
                break;
            case R.id.log_in_drawer:
                startActivity(LoginActivity.class);
                break;
        }
        return false;
    }

    private void setUpDrawer() {
        drawerList = (NavigationView) findViewById(R.id.left_drawer);
        drawerList.setNavigationItemSelectedListener(this);
    }

    private void setUpToolbar() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_activity_toolbar);
        setSupportActionBar(myToolbar);

        //noinspection ConstantConditions
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        String description = getString(R.string.main_icon_description);
        getSupportActionBar().setHomeActionContentDescription(description);

        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                myToolbar,
                R.string.drawer_open,
                R.string.drawer_close
        );
    }

    private void startActivity(Class _class) {
        Intent intent = new Intent(this, _class);
        startActivity(intent);
    }

    private void showAlertDialog() {
        AlertDialog aboutAlert = null;
        try {
            aboutAlert = About.create(this);
        } catch (VersionException | LastDateException e) {
            e.printStackTrace();
        }
        if(aboutAlert!=null)
            aboutAlert.show();
    }

    private void setFragmentContent(Fragment newFragment){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.content_frame, newFragment);

        transaction.commit();
    }

    public void startMyService(View v) {
        Intent serviceIntent = new Intent(this, SampleCreator.class);
        serviceIntent.addCategory("SampleCreator");
        startService(serviceIntent);
    }

    public void stopMyService(View v) {
        Intent serviceIntent = new Intent(this, SampleCreator.class);
        serviceIntent.addCategory("SampleCreator");
        stopService(serviceIntent);
    }


    public void insertData(View btn) throws IOException {
        //TODO to tylko bylo do prototypu
        //new Sender(getBaseContext()).execute(tmpRecorder.soundDb(1.0));
    }

    public void showGPS(View w) {
        DataBaseHandler dataBaseHandler = new DataBaseHandler(getBaseContext(), getResources().getString(R.string.database_name));
        try {
            dataBaseHandler.insert(new Sample(32.3, 322.23, 12.11, 0));
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
