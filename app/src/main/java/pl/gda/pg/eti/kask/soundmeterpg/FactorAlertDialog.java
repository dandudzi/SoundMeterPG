package pl.gda.pg.eti.kask.soundmeterpg;

import android.annotation.SuppressLint;
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
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by Daniel on 03.07.2016 :).
 */
class FactorAlertDialog {
    public static final int REQUEST_CODE_INTERNET =4991 ;
    public static final int REQUEST_CODE_GPS =1994 ;

    public static AlertDialog createNoGPSDialog(final Activity ownerDialog, final Fragment ownerFragment){
        Intent intent =new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        InformationToCreateDialog info = new InformationToCreateDialog(intent,ownerDialog,ownerFragment,REQUEST_CODE_GPS,R.layout.no_gps_dialog);
        return createCustomDialog(info);
    }


    public static AlertDialog createNoInternetDialog(final Activity ownerDialog, final Fragment ownerFragment){
        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        InformationToCreateDialog info = new InformationToCreateDialog(intent,ownerDialog,ownerFragment,REQUEST_CODE_INTERNET,R.layout.no_internet_dialog);
        return createCustomDialog(info);
    }

    private static AlertDialog createCustomDialog(final InformationToCreateDialog info){
        AlertDialog.Builder dialog = new AlertDialog.Builder(info.ownerDialog);
        LayoutInflater inflater = info.ownerDialog.getLayoutInflater();

        View dialogView = inflater.inflate(info.layout, null);

        dialog.setCancelable(false);
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick( final DialogInterface dialog,  final int id) {
                info.ownerFragment.startActivityForResult(info.intent,info.requestCode);
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog,  final int id) {
                dialog.cancel();
            }
        });
        dialog.setView(dialogView);
        return dialog.create();
    }

    public static AlertDialog createAboutDialog(Activity ownerDialog) {

        AlertDialog.Builder aboutDialog = new AlertDialog.Builder(ownerDialog);
        Context ownerContext = ownerDialog.getBaseContext();

        LayoutInflater inflater = ownerDialog.getLayoutInflater();

        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.about_dialog, null);

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

    public static String getLastDateBuildApplication(Context ownerContext) {
        String lastBuild = "null";
        ApplicationInfo appInfo ;
        try {
            appInfo = ownerContext.getPackageManager().getApplicationInfo(ownerContext.getPackageName(), 0);
            ZipFile file = new ZipFile(appInfo.sourceDir);
            ZipEntry entry = file.getEntry("classes.dex");
            long time = entry.getTime();
            lastBuild = SimpleDateFormat.getInstance().format(new Date(time));
        } catch (PackageManager.NameNotFoundException | IOException e) {
            Log.e("Last build", e.getLocalizedMessage() + '\n' + e.getMessage());
        }

        return lastBuild;
    }

    public static String getVersionOfApplication(Context ownerContext) {
        String version = "null";
        try {
             version =  ownerContext.getPackageManager().getPackageInfo(ownerContext.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("Package version", e.getLocalizedMessage() + '\n' + e.getMessage());
        }
        return version;
    }


}


class InformationToCreateDialog{
    int layout;
    int requestCode;
    final Intent intent;
    final Activity ownerDialog;
    final Fragment ownerFragment;

    public InformationToCreateDialog(Intent intent, Activity ownerDialog, Fragment ownerFragment, int requestCode, int layout) {
        this.intent = intent;
        this.ownerDialog = ownerDialog;
        this.ownerFragment = ownerFragment;
        this.requestCode = requestCode;
        this.layout = layout;
    }


}