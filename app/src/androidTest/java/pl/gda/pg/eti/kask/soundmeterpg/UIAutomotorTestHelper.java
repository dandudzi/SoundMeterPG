package pl.gda.pg.eti.kask.soundmeterpg;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import pl.gda.pg.eti.kask.soundmeterpg.Internet.ConnectionInternetDetector;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Daniel on 30.08.2016 at 10:51 :).
 */
public class UIAutomotorTestHelper {
    public static final String MEASURE_ITEM = "Measure";
    public static final String MEASURE_ID = "pl.gda.pg.eti.kask.soundmeterpg:id/measure";
    public static final String MEASUREMENTS_ITEM = "Measurements";
    public static final String MEASUREMENTS_ID = "pl.gda.pg.eti.kask.soundmeterpg:id/measurements";

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

    private static final String APP_NAME = "SoundMeterPG";
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
    public static void turnOnGps(UiDevice device, Context context) throws UiObjectNotFoundException {
        UiObject object = null;
       switch(Build.VERSION.SDK_INT ){
           case 19 :
               Intent settingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
               settingIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
               context.startActivity(settingIntent);
               object = new UiObject(new UiSelector().className("android.widget.Switch"));
               break;
           case 20 :
               //Lel nie ma api 20 w ogole xD
               break;
           case 21 :
               device.openNotification();
               object = new UiObject(new UiSelector().resourceId("com.android.systemui:id/multi_user_avatar"));
               object.click();
               object = new UiObject(new UiSelector().resourceId("android:id/title").text("Location"));
               break;
           case 22:
               device.openQuickSettings();
               object = new UiObject(new UiSelector().resourceId("android:id/title").text("Location"));
               break;
           case 23 :
               device.openQuickSettings();
               object = new UiObject(new UiSelector().index(6));
               break;
           case 24 :
               device.openQuickSettings();
               object = new UiObject(new UiSelector().resourceId("com.android.systemui:id/tile_label").text("Location"));
               break;
       }
        object.click();
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




  private static void turnInternetData(UiDevice device, Context context) throws UiObjectNotFoundException {
        UiObject object = null;
        UiObject switchObject = new UiObject(new UiSelector().className("android.widget.Switch"));
        UiObject buttonObject = new UiObject(new UiSelector().resourceId("android:id/button1"));
        switch (Build.VERSION.SDK_INT) {
            case 19:
                Intent settingIntent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                settingIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(settingIntent);
                UiObject mobileNetwork = new UiObject(new UiSelector().resourceId("android:id/title").text("Mobile networks"));
                mobileNetwork.click();
                mobileNetwork = new UiObject(new UiSelector().resourceId("android:id/checkbox"));
                mobileNetwork.click();
                break;
            case 21:
                device.openNotification();
                object = new UiObject(new UiSelector().resourceId("com.android.systemui:id/multi_user_avatar"));
                object.click();
                UiObject androidObject = new UiObject(new UiSelector().resourceId("android:id/title").index(4));
                androidObject.click();
                switchObject.click();
                buttonObject.click();
                break;
            case 22:
                device.openQuickSettings();
                androidObject = new UiObject(new UiSelector().resourceId("android:id/title").index(4));
                androidObject.click();
                switchObject.click();
                buttonObject.click();
                break;
            case 23:
                device.openQuickSettings();
                androidObject = new UiObject(new UiSelector().className("android.view.ViewGroup").index(3));
                androidObject.click();
                switchObject.click();
                buttonObject.click();
                break;
            case 24:
                device.openQuickSettings();
                androidObject = new UiObject(new UiSelector().resourceId("com.android.systemui:id/tile_label").index(0));
                androidObject.click();
                switchObject.click();
                buttonObject.click();
                break;
        }
        device.pressBack();
        device.pressBack();
    }




}
