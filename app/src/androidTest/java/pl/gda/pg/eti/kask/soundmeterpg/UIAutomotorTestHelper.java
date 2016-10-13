package pl.gda.pg.eti.kask.soundmeterpg;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CompoundButtonCompat;

import pl.gda.pg.eti.kask.soundmeterpg.Services.ServiceDetector;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.ConnectionInternetDetector;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Daniel on 30.08.2016 at 10:51 :).
 */
public class UIAutomotorTestHelper {
    public static final String MEASURE_ITEM = "Measure";
    public static final String MEASURE_ID = "pl.gda.pg.eti.kask.soundmeterpg:id/measure";
    public static final String MEASUREMENTS_ITEM = "Measurements";
    public static final String MEASUREMENTS_ID = "pl.gda.pg.eti.kask.soundmeterpg:id/measurements_layout";

    public static final String LOGIN_ACTIVITY_ITEM = "Log in";
    public static final String LOGIN_PROGRESS_ACTIVITY_ID = "pl.gda.pg.eti.kask.soundmeterpg:id/progress_bar_view";
    public static final String LOGIN_ACTIVITY_ID = "pl.gda.pg.eti.kask.soundmeterpg:id/login_form";

    public static final String SETTINGS_ITEM = "Settings";
    public static final String SETTINGS_ID = "pl.gda.pg.eti.kask.soundmeterpg:id/settings";

    public static final String MENU_ID="More options";

    public static final String ABOUT_TEXT="About";
    public static final String ABOUT_ID="pl.gda.pg.eti.kask.soundmeterpg:id/about_view";

    public static final String FAQ_TEXT="FAQ";
    public static final String FAQ_ID="pl.gda.pg.eti.kask.soundmeterpg:id/faq_view";

    public static final String LOGIN_DIALOG_TEXT ="Login";
    public static final String LOGIN_DIALOG_ID ="pl.gda.pg.eti.kask.soundmeterpg:id/login_view";
    public static final String LOGIN_BUTTON_ID="pl.gda.pg.eti.kask.soundmeterpg:id/login_button_login_activity";

    public static final String ANDROID_POSITIVE_BUTTON_ID = "android:id/button1";

    public static final String APP_NAME = "SoundMeterPG";
    public static final int TIME_OUT = 1000;

    public static void testDrawerItem(boolean pressBackIfNotPressHome, boolean openByIconAppsIfNotByRecentApps, String itemInDrawerName,
                                       String idStartView, String idEndView, UiDevice device) throws Exception {
        openItemInDrawer(itemInDrawerName,device);
        checkViewIsDisplay(idStartView,device);

        if(pressBackIfNotPressHome)
            device.pressBack();
        else
            device.pressHome();

        if(openByIconAppsIfNotByRecentApps)
            openActivityByIconApp(device);
        else
            openActivityByRecentApps(device);

        checkViewIsDisplay(idEndView,device);
    }

    public static void openItemInDrawer(String itemText, UiDevice device) throws UiObjectNotFoundException {
        openDrawer(device);
        isItemInDrawer(itemText, device);
        clickItem(itemText,device);
    }

    private static void clickItem(String itemText, UiDevice device) throws UiObjectNotFoundException {
        UiObject item = device.findObject(new UiSelector().text(itemText));
        if(!item.exists())
            throw new UiObjectNotFoundException("Not found view");
        item.click();
    }

    public static void openDrawer(UiDevice device) throws UiObjectNotFoundException {
        UiObject drawer = device.findObject(new UiSelector().descriptionContains("drawer_open"));
        drawer.click();
    }

    public static void checkIsDrawerOpen(UiDevice device) throws UiObjectNotFoundException {
        isItemInDrawer(SETTINGS_ITEM,device);
    }

    private static void isItemInDrawer(String itemText, UiDevice device) throws UiObjectNotFoundException {
        UiObject item = device.findObject(new UiSelector().text(itemText));
        if(!item.waitForExists(TIME_OUT))
            throw new UiObjectNotFoundException("Not found view");
    }

    public static void checkViewIsDisplay(String idView, UiDevice device) throws UiObjectNotFoundException {
        UiObject view = device.findObject(new UiSelector().resourceId(idView));
        if(!view.waitForExists(TIME_OUT))
            throw new UiObjectNotFoundException("Not found view");
    }



    public static void openActivityByRecentApps( UiDevice device) throws RemoteException, UiObjectNotFoundException {
        UiObject panel = getHomeScreen(device);
        if (!panel.exists())
            device.pressHome();
        device.pressRecentApps();

        UiObject myApp = device.findObject(new UiSelector().description(APP_NAME));

        if(Build.VERSION.SDK_INT > 19)
            while(true){
                if(myApp.clickAndWaitForNewWindow(TIME_OUT))
                    break;
            }
        else
            myApp.clickAndWaitForNewWindow(TIME_OUT);
    }


    public static void openActivityByIconApp( UiDevice device) throws UiObjectNotFoundException {
        UiObject appsButton = device.findObject(new UiSelector().description("Apps"));
        UiObject panel  = getHomeScreen(device);

        device.pressHome();
        appsButton.click();


        UiObject myApp = device.findObject(new UiSelector().text(APP_NAME));
        UiObject tabContentView = getAppTab(device);
        while(!myApp.exists()) {
            tabContentView.swipeLeft(10);
            tabContentView.swipeDown(10);
        }

        myApp.clickAndWaitForNewWindow(TIME_OUT);
    }

    private static UiObject getAppTab(UiDevice device) {
        if(Build.VERSION.SDK_INT > 19)
            return device.findObject(new UiSelector().resourceId("android:id/content"));
        else
            return device.findObject(new UiSelector().resourceId("android:id/tabcontent"));
    }

    private static UiObject getHomeScreen(UiDevice device) {
        return device.findObject(new UiSelector().descriptionContains("Home screen"));
    }

    public static void pressButtonAndWaitForNewWindow(String id, UiDevice device) throws UiObjectNotFoundException {
        UiObject button =  device.findObject(new UiSelector().resourceId(id));
        if(!button.exists())
            throw  new UiObjectNotFoundException("Not found button");

        button.clickAndWaitForNewWindow(TIME_OUT);
    }

    public static void turnOnGPS(UiDevice device, Context context) throws UiObjectNotFoundException {
        if(ServiceDetector.isGPSEnabled(context))
            return;
        turnGPS(device,context);
    }

    public static void turnOffGPS(UiDevice device, Context context) throws UiObjectNotFoundException {
        if(ServiceDetector.isGPSEnabled(context))
            turnGPS(device,context);
    }

    public static void turnGPS(UiDevice device, Context context) throws UiObjectNotFoundException {
        UiObject object = null;
       switch(Build.VERSION.SDK_INT ){
           case 19 :  case 22 :case 23 :
               Intent settingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
               settingIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
               context.startActivity(settingIntent);
               object = device.findObject(new UiSelector().className("android.widget.Switch"));
               break;
           case 21:
               device.openNotification();
               object = device.findObject(new UiSelector().resourceId("com.android.systemui:id/multi_user_avatar"));
               object.click();
               object = device.findObject(new UiSelector().description("Location reporting on."));
               break;
           case 24 :
               device.openQuickSettings();
               object = device.findObject(new UiSelector().resourceId("com.android.systemui:id/tile_label").text("Location"));
               break;
       }
        if(!object.waitForExists(TIME_OUT))
            throw new UiObjectNotFoundException("Switch not found");
        object.click();
        if(Build.VERSION.SDK_INT == 24 || Build.VERSION.SDK_INT == 21)
            device.pressBack();
        device.pressBack();
    }

    public static void turnOnInternetData(UiDevice device, Context context) throws UiObjectNotFoundException {
        ConnectionInternetDetector detector =  new ConnectionInternetDetector(context);
        if(detector.isConnectingToInternet())
            return;
        turnInternetData(device,context);
    }

    public static void turnOffInternetData(UiDevice device, Context context) throws UiObjectNotFoundException {
        ConnectionInternetDetector detector =  new ConnectionInternetDetector(context);
        if(detector.isConnectingToInternet())
            turnInternetData(device,context);
    }

    public static void turnInternetData(UiDevice device, Context context) throws UiObjectNotFoundException {
        UiObject object = null;
        UiObject switchObject = device.findObject(new UiSelector().className("android.widget.Switch"));
        UiObject buttonObject = device.findObject(new UiSelector().resourceId("android:id/button1"));
        switch (Build.VERSION.SDK_INT) {
            case 19:
                Intent settingIntent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                settingIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(settingIntent);
                UiObject mobileNetwork = new UiObject(new UiSelector().resourceId("android:id/title").text("Mobile networks"));
                mobileNetwork.click();
                mobileNetwork = device.findObject(new UiSelector().resourceId("android:id/checkbox"));
                mobileNetwork.click();
                break;
            case 21:
                device.openNotification();
                object = device.findObject(new UiSelector().resourceId("com.android.systemui:id/multi_user_avatar"));
                object.click();
                UiObject androidObject = device.findObject(new UiSelector().resourceId("android:id/title").index(4));
                androidObject.click();
                switchObject.click();
                buttonObject.click();
                break;
            case 22:
                device.openQuickSettings();
                androidObject = device.findObject(new UiSelector().description("Mobile Phone two bars.. 4G. Android."));
                androidObject.click();
                switchObject.click();
                buttonObject.click();
                break;
            case 23:
                device.openQuickSettings();
                androidObject = device.findObject(new UiSelector().className("android.view.ViewGroup").index(3));
                androidObject.click();
                switchObject.click();
                buttonObject.click();
                break;
            case 24:
                device.openQuickSettings();
                androidObject = device.findObject(new UiSelector().resourceId("com.android.systemui:id/tile_label").index(0));
                androidObject.click();
                switchObject.click();
                buttonObject.click();
                break;
        }
        device.pressBack();
        device.pressBack();
    }

    public static void turnOnAllPermission(UiDevice device, Context context) throws UiObjectNotFoundException, InterruptedException {
        turnOnGPSPermission(device,context);
        turnOnMicrophonePermission(device,context);
    }

    public static void turnOnGPSPermission(UiDevice device, Context context) throws UiObjectNotFoundException, InterruptedException {
        if(Build.VERSION.SDK_INT > 22){
                UiObject gps = openGPSPermission(device, context);
                if (gps.getText().equals("OFF")) {
                    gps.click();
                }
                device.pressBack();
        }
    }

    public static void turnOnMicrophonePermission(UiDevice device, Context context) throws UiObjectNotFoundException, InterruptedException {
        if(Build.VERSION.SDK_INT > 22){
            UiObject mic = openMicrophonePermission(device, context);
            if (mic.getText().equals("OFF")) {
                mic.click();
            }
            device.pressBack();
        }
    }

    public static void turnOffGPSPermission(UiDevice device, Context context) throws UiObjectNotFoundException, InterruptedException {
        if(Build.VERSION.SDK_INT > 22){
                UiObject gps = openGPSPermission(device, context);
                if (gps.getText().equals("ON")) {
                    gps.click();
                }
                device.pressBack();
        }
    }


    private static UiObject openGPSPermission(UiDevice device, Context context) throws UiObjectNotFoundException {
        startInstalledAppDetailsActivity(context);
        UiObject permission = device.findObject( new UiSelector().textStartsWith("Permission"));
        permission.clickAndWaitForNewWindow();
        UiObject gps = device.findObject(new UiSelector().className("android.widget.Switch").instance(0));
        return gps;
    }

    private static UiObject openMicrophonePermission(UiDevice device, Context context) throws UiObjectNotFoundException {
        startInstalledAppDetailsActivity(context);
        UiObject permission = device.findObject( new UiSelector().textStartsWith("Permission"));
        permission.clickAndWaitForNewWindow();
        UiObject mic = device.findObject(new UiSelector().className("android.widget.Switch").instance(1));
        return mic;
    }

    public static void startInstalledAppDetailsActivity( Context context) {
        Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }

}
