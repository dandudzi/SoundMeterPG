package pl.gda.pg.eti.kask.soundmeterpg;

import android.*;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.LastDateException;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.VersionException;

/**
 * Created by Daniel on 04.08.2016 at 11:43 :) at 12:13 :).
 */
public class InformationAboutThisApplication {

    public static String getLastDateBuildApplication(Context ownerContext) throws LastDateException {
        String lastBuild = null;
        ApplicationInfo appInfo;
        try {
            appInfo = ownerContext.getPackageManager().getApplicationInfo(ownerContext.getPackageName(), 0);
            String appPath = appInfo.sourceDir;
            long time = new File(appPath).lastModified();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            lastBuild = formatter.format(time);
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        return lastBuild;
    }

    public static String getVersionOfApplication(Context ownerContext) throws VersionException {
        String version;
        try {
            version =  ownerContext.getPackageManager().getPackageInfo(ownerContext.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("Package version", e.getLocalizedMessage() + '\n' + e.getMessage());
            throw new VersionException(e.getMessage());
        }
        return version;
    }

    public static String getMACAddress(){
        return "02:00:00:00:00:00";
    }

}
