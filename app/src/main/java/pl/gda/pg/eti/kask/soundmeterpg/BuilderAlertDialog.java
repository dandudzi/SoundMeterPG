package pl.gda.pg.eti.kask.soundmeterpg;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by Daniel on 03.07.2016 :).
 */
class BuilderAlertDialog {
    public static AlertDialog createNoGPSDialog(final Activity ownerDialog, final Fragment ownerFragment){
        AlertDialog.Builder noGPSDialog = new AlertDialog.Builder(ownerDialog);
        LayoutInflater inflater = ownerDialog.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.no_gps_dialog, null);

        noGPSDialog.setCancelable(false);
        noGPSDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {

                final Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                ownerFragment.startActivityForResult(intent,SettingsFragment.REQUEST_CODE_GPS);
            }
        });
        noGPSDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                dialog.cancel();
            }
        });
        noGPSDialog.setView(dialogView);
        return noGPSDialog.create();
    }


    public static AlertDialog createNoInternetDialog(final Activity ownerDialog, final Fragment ownerFragment){
        AlertDialog.Builder noInternetDialog = new AlertDialog.Builder(ownerDialog);
        LayoutInflater inflater = ownerDialog.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.no_internet_dialog, null);

        noInternetDialog.setCancelable(false);
        noInternetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {

                ownerFragment.startActivityForResult(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS),SettingsFragment.REQUEST_CODE_INTERNET);
            }
        });
        noInternetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                dialog.cancel();
            }
        });
        noInternetDialog.setView(dialogView);
        return noInternetDialog.create();
    }

    public static AlertDialog createAboutDialog(Activity ownerDialog) {

        AlertDialog.Builder aboutDialog = new AlertDialog.Builder(ownerDialog);
        Context ownerContext = ownerDialog.getBaseContext();

        LayoutInflater inflater = ownerDialog.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.about_dialog, null);

        String version = getVersionOfApplication(ownerContext);
        setTextView(dialogView,R.id.version_about_dialog,version );

        String lastBuild =  getLastDateBuildApplication(ownerContext);
        setTextView(dialogView,R.id.last_build_about_dialog,lastBuild );

        aboutDialog.setView(dialogView);
        aboutDialog.setPositiveButton("OK",null);
        return aboutDialog.create();
    }

    private static void setTextView(View view, int dialogID, String text){
        TextView tmp = (TextView) view.findViewById(dialogID);
        tmp.setText(text);
    }

    private static String getLastDateBuildApplication(Context ownerContext) {
        String lastBuild = "null";
        ApplicationInfo appInfo ;
        try {
            appInfo = ownerContext.getPackageManager().getApplicationInfo(ownerContext.getPackageName(), 0);
            ZipFile file = new ZipFile(appInfo.sourceDir);
            ZipEntry entry = file.getEntry("classes.dex");
            long time = entry.getTime();
            lastBuild = SimpleDateFormat.getInstance().format(new java.util.Date(time));
        } catch (PackageManager.NameNotFoundException | IOException e) {
            Log.e("Last build", e.getLocalizedMessage() + "\n" + e.getMessage());
        }

        return lastBuild;
    }

    private static String getVersionOfApplication(Context ownerContext) {
        String version = "null";
        try {
             version =  ownerContext.getPackageManager().getPackageInfo(ownerContext.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("Package version", e.getLocalizedMessage() + "\n" + e.getMessage());
        }
        return version;
    }


}
