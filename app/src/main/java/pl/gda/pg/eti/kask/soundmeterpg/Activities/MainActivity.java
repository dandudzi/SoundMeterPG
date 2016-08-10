package pl.gda.pg.eti.kask.soundmeterpg.Activities;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.AdapterView;
import android.widget.ListView;

import pl.gda.pg.eti.kask.soundmeterpg.Dialogs.About;
import pl.gda.pg.eti.kask.soundmeterpg.Dialogs.FAQ;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.LastDateException;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.VersionException;
import pl.gda.pg.eti.kask.soundmeterpg.Fragments.LogIn;
import pl.gda.pg.eti.kask.soundmeterpg.Fragments.Measure;
import pl.gda.pg.eti.kask.soundmeterpg.Fragments.Measurements;
import pl.gda.pg.eti.kask.soundmeterpg.R;
import pl.gda.pg.eti.kask.soundmeterpg.Drawer.RowsDrawer;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView drawerList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        setFragmentContent(new Measure());

        Toolbar myToolbar = setUpToolbar();

        setUpDrawer(myToolbar);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }

    private void setUpDrawer(final Toolbar myToolbar) {
        drawerList = (ListView) findViewById(R.id.left_drawer);

        drawerList.setAdapter(RowsDrawer.createRows(this,R.layout.row_item_drawer));

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                myToolbar,
                R.string.drawer_open,
                R.string.drawer_close
        );

        drawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    private Toolbar setUpToolbar() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_activity_toolbar);
        setSupportActionBar(myToolbar);

        //noinspection ConstantConditions
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        String description = getString(R.string.main_icon_description);
        getSupportActionBar().setHomeActionContentDescription(description);
        return myToolbar;
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
            case R.id.settings_action:
                Log.i("Toolbar","Opening settings");
                startSettingsActivity();
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

    public void startSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
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

    private void setFragmentContent(Fragment newFragment){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.content_frame, newFragment);

        transaction.commit();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        Log.i("Navigation Drawer","User chose position number "+Integer.toString(position));
        String[] drawerRows = getResources().getStringArray(R.array.rows_list_drawer);
        String choseOption =  drawerRows[position];
        actionSelectItem(choseOption);
        drawerList.setItemChecked(position, false);
        drawerLayout.closeDrawer(drawerList);
    }

    private void actionSelectItem(String choseOption) {
        switch(choseOption){
            case "Settings":
                startSettingsActivity();
                break;
            case "Measure":
                setFragmentContent(new Measure());
                break;
            case "Measurements":
                setFragmentContent(new Measurements());
                break;
            case "Log in":
                setFragmentContent(new LogIn());
                break;
        }
    }


}