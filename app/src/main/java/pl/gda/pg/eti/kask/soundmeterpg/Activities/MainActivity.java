package pl.gda.pg.eti.kask.soundmeterpg.Activities;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenu;
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
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import pl.gda.pg.eti.kask.soundmeterpg.Dialogs.About;
import pl.gda.pg.eti.kask.soundmeterpg.Dialogs.FAQ;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.LastDateException;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.VersionException;
import pl.gda.pg.eti.kask.soundmeterpg.Fragments.Measure;
import pl.gda.pg.eti.kask.soundmeterpg.Fragments.Measurements;
import pl.gda.pg.eti.kask.soundmeterpg.Internet.MyAccountManager;
import pl.gda.pg.eti.kask.soundmeterpg.R;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Measurement;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.PreferenceParser;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView drawerList;
    private PreferenceParser preference;
    private boolean isMeasure = false;
    private MyAccountManager accountManager;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        setFragmentContent(new Measure());
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //Menu menu = (Menu) drawerLayout.findViewById(R.id.log_out_drawer);
        //menu = drawerLayout.findViewById(R.id.log_in_drawer);

        setUpToolbar();
        setUpDrawer();
        preference = new PreferenceParser(getBaseContext());

        /*once onFirst start app*/
        PreferenceManager.setDefaultValues(this,R.xml.preferences,false);
        preference.askUserForPermission(this);
        accountManager = new MyAccountManager(getBaseContext());

        saveDeviceName();



    }



    private void setFragmentContent(Fragment newFragment){
        if(newFragment instanceof Measure)
            isMeasure =true;
        else
            isMeasure=false;

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.content_frame, newFragment);

        transaction.commit();
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
        ){
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                NavigationView drawerList = (NavigationView) findViewById(R.id.left_drawer);
                NavigationMenu menu = (NavigationMenu) drawerList.getMenu();
                //get Header to Drawer
                View headerLayout = drawerList.getHeaderView(0);
                TextView header = (TextView) headerLayout.findViewById(R.id.login_info);

                //get Item from Menu
                MenuItem loginActivity = menu.findItem(R.id.log_in_drawer);


                SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
                long timeInMillis = prefs.getLong(getBaseContext().getResources().getString(R.string.cookie_expired_time), (long)0);
                if(timeInMillis != 0) {
                    Date resultdate = new Date(timeInMillis);
                    if (resultdate.after(new Date())) {
                        String login = prefs.getString(getBaseContext().getResources().getString(R.string.login_key), "logout");
                        header.setText("Hello " + login + " !\n" + "Your account log out automatically on : " + formatter.format(resultdate));
                        loginActivity.setTitle("Log out");
                    }
                    else{
                        header.setText("You are logout");
                        loginActivity.setTitle("Sign in");
                    }
                }
               else{
                    header.setText("You are logout");
                    loginActivity.setTitle("Sign in");
                }
                invalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);
    }

    private void setUpDrawer() {
        drawerList = (NavigationView) findViewById(R.id.left_drawer);
        drawerList.setNavigationItemSelectedListener(this);

    }

    private void saveDeviceName() {
        SharedPreferences preferences;
        preferences =  PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(getResources().getString(R.string.deviceID),accountManager.getDeviceName());
        editor.commit();
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
    public boolean onNavigationItemSelected(MenuItem item) {
        Log.i("Navigation Drawer","User chose position number "+item.getTitle());
        drawerLayout.closeDrawer(GravityCompat.START);
        FragmentManager fragmentManager = this.getFragmentManager();
        Fragment frag = fragmentManager.findFragmentById(R.id.content_frame);

        switch(item.getItemId()){
            case R.id.settings_drawer:
                startActivity(SettingsActivity.class);
                break;
            case R.id.measure_drawer:
                if(frag != null && frag instanceof Measure)
                    return false;
                setFragmentContent(new Measure());
                break;
            case R.id.measurements_drawer:
                if(frag != null && frag instanceof Measurements)
                    return false;
                setFragmentContent(new Measurements());
                break;
            case R.id.log_in_drawer:
                NavigationMenu menu = (NavigationMenu) drawerList.getMenu();
                MenuItem loginActivity = menu.findItem(R.id.log_in_drawer);
                if(loginActivity.getTitle() == "Sign in")
                startActivity(LoginActivity.class);
                else
                    accountManager.logOut();
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if(isMeasure) {
                FragmentManager fragmentManager = this.getFragmentManager();
                Measure frag = (Measure) fragmentManager.findFragmentById(R.id.content_frame);
                if (preference.hasPermissionToWorkInBackground()) {
                    frag.startBackgroundService();
                }
            }
            super.onBackPressed();
        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        preference.setAllPreferenceLikePermission();
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
