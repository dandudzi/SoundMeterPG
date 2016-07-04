package pl.gda.pg.eti.kask.soundmeterpg;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
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
 * Created by Daniel on 03.07.2016.
 */
public class BuilderAlertDialog {
    public static AlertDialog getAboutDialog(Activity ownerDialog) {

        AlertDialog.Builder aboutDialog = new AlertDialog.Builder(ownerDialog);
        Context ownerContext = ownerDialog.getBaseContext();

        LayoutInflater inflater = ownerDialog.getLayoutInflater();


        View dialogView = inflater.inflate(R.layout.about_dialog, null);

        String version = getVersionOfApplication(ownerContext);
        setTextView(dialogView,R.id.version_abouta_dialog,version );

        String lastBuild =  getLastDateBuildApplication(ownerContext);
        setTextView(dialogView,R.id.last_build_abouta_dialog,lastBuild );

        String email = ownerContext.getString(R.string.email_contact);
        setTextView(dialogView,R.id.contact_abouta_dialog,email );

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
        ApplicationInfo appInfo = null;
        try {
            appInfo = ownerContext.getPackageManager().getApplicationInfo(ownerContext.getPackageName(), 0);
            ZipFile file = new ZipFile(appInfo.sourceDir);
            ZipEntry entry = file.getEntry("classes.dex");
            long time = entry.getTime();
            lastBuild = SimpleDateFormat.getInstance().format(new java.util.Date(time));
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("Package version", e.getLocalizedMessage() + "\n" + e.getMessage());
        } catch (IOException e) {
            Log.e("Package version", e.getLocalizedMessage() + "\n" + e.getMessage());
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
