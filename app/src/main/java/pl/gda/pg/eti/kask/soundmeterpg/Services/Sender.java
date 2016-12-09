package pl.gda.pg.eti.kask.soundmeterpg.Services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.LoginActivity;
import pl.gda.pg.eti.kask.soundmeterpg.Activities.MainActivity;
import pl.gda.pg.eti.kask.soundmeterpg.Database.DataBaseHandler;
import pl.gda.pg.eti.kask.soundmeterpg.Database.MeasurementDataBaseObject;
import pl.gda.pg.eti.kask.soundmeterpg.IntentActionsAndKeys;
import pl.gda.pg.eti.kask.soundmeterpg.Internet.MyAccountManager;
import pl.gda.pg.eti.kask.soundmeterpg.Internet.SingletonCookieManager;
import pl.gda.pg.eti.kask.soundmeterpg.R;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.ConnectionInternetDetector;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.PreferenceParser;

public class Sender extends IntentService {

    private ConnectionInternetDetector connectionInternetDetector;
    private PreferenceParser preferenceParser;
    private DataBaseHandler dataBaseHandler;
    private SharedPreferences sharedPreferences;
    private String SITE ;
    private volatile boolean endMeasure = false;
    private final static String DEVICE = "DeviceID";
    private  CookieManager cookieManager;
    private MyAccountManager accountManager;


    private BroadcastReceiver endTaskReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            IntentActionsAndKeys action =  IntentActionsAndKeys.valueOf(intent.getAction());
            switch(action){
                case END_ACTION:
                    synchronized (Sender.this) {
                        endMeasure =  true;
                    }
                    break;
            }
        }
    };

    public Sender(){ super("Sender"); }

    @Override
    protected void onHandleIntent(Intent intent) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        MeasurementDataBaseObject measurementDataBaseObject;
        cookieManager = SingletonCookieManager.getInstance();
        accountManager = MyAccountManager.getInstance(getBaseContext());
        String login  = sharedPreferences.getString(getResources().getString(R.string.login_key),"");
        String pass = sharedPreferences.getString(getResources().getString(R.string.password),"");
        //sprawdzenie czy zalogowany

        if(!accountManager.checkIfUserIsLogged()){
            if(login.isEmpty() || pass.isEmpty())
                createNotification();
            else
                accountManager.logIn(login,pass);
        }

        while (!endMeasure) {
            try {
                Thread.sleep(10000);
                long timeInMillis = sharedPreferences.getLong(getBaseContext().getResources().getString(R.string.cookie_expired_time), (long) 0);
                if (timeInMillis > 0) {
                    Date resultdate = new Date(timeInMillis);
                    if (resultdate.after(new Date())) {//nie musze sprawdzac przez neta czy zalogowany
                        if (!endMeasure && isConnectionWithServer(SITE)) {
                            measurementDataBaseObject = dataBaseHandler.getTheOldestRowToSendToServer();
                            if (measurementDataBaseObject != null && insert(measurementDataBaseObject)) {
                                dataBaseHandler.erease(measurementDataBaseObject.getID());
                            }
                        }
                    }
                    //Uplynal czas logowania
                    else
                        accountManager.logIn(login,pass);
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.i("Sender class", "STOPING SERVICE");

    }

    private void createNotification() {
        Intent intent2 = new Intent(getBaseContext(), LoginActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getBaseContext(),0,intent2,0);
        android.support.v4.app.NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_politechnika)
                .setContentTitle("You are not sign in !")
                .setContentText("If you want to send data to server you have to sign in.")
                .setPriority(1)
                .setContentIntent(contentIntent)
                .setOngoing(false);


        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(600, notification.build());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SITE = getBaseContext().getResources().getString(R.string.ping_site);
        connectionInternetDetector = new ConnectionInternetDetector(getBaseContext());
        preferenceParser = new PreferenceParser(getBaseContext());
        dataBaseHandler = new DataBaseHandler(getBaseContext(), getBaseContext().getResources().getString(R.string.database_name));
        LocalBroadcastManager.getInstance(this).registerReceiver(endTaskReceiver, new IntentFilter(IntentActionsAndKeys.END_ACTION.toString()));
    }

    public boolean isConnectionWithServer(String url) {
        if (preferenceParser.hasPermissionToUseInternet() && connectionInternetDetector.isConnectingToInternet()) {
            Runtime runtime = Runtime.getRuntime();
            try {
                Process connectionProcess;
                if(checkIfEmulatorDevice())
                    connectionProcess = runtime.exec("nc -v " + url + " 80");
                else
                    connectionProcess = runtime.exec("/system/bin/ping -c 1 " + url);
                int mExitValue = connectionProcess.waitFor();
                if (mExitValue == 0) {
                    return true;
                } else {
                    return false;
                }
            } catch (InterruptedException ignore) {
                ignore.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else return false;
    }

    private boolean checkIfEmulatorDevice() {
        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String networkOperator = tm.getNetworkOperatorName();
        return networkOperator.contains("Android");
    }

    public boolean insert(MeasurementDataBaseObject measurement){
        if (measurement == null) {
            return  false;
        }
        if (isConnectionWithServer(SITE)) {
            try {
                HttpURLConnection connection;
                URL url = new URL("https://soundmeterpg.pl/insert");
                String parametr = createStringParametr(measurement);
                connection = setConnectionArguments(url);
                setRequestArgument(connection, parametr);
                String response = getResponse(connection);
                Log.i("RESPONSE FROM SEND ", response.toString());
                accountManager.storeCookies();
                if (response.contains(""))
                    return true;
                else
                    return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        else return  false;
    }

    private void setRequestArgument(HttpURLConnection connection, String parametr) throws IOException {
        OutputStreamWriter request;
        request = new OutputStreamWriter(connection.getOutputStream());
        request.write(parametr);
        request.flush();
        request.close();
    }

    @NonNull
    private String getResponse(HttpURLConnection connection) throws IOException {
        String response;
        String line = "";
        InputStreamReader isr = new InputStreamReader(connection.getInputStream());
        BufferedReader reader = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        response = sb.toString();
        isr.close();
        reader.close();
        return response;
    }

    @NonNull
    private HttpURLConnection setConnectionArguments(URL url) throws IOException {
        HttpURLConnection connection;
        connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestMethod("POST");
        return connection;
    }

    private String createStringParametr(MeasurementDataBaseObject measurement) {
        String parametrs = DataBaseHandler.MIN + "=" + measurement.getMin() + "&" +
                DataBaseHandler.MAX + "=" + measurement.getMax() + "&" +
                DataBaseHandler.AVG + "=" + measurement.getAvg() + "&" +
                DataBaseHandler.LATITUDE + "=" + measurement.getLocation().getLatitude() + "&" +
                DataBaseHandler.LONGITUDE + "=" + measurement.getLocation().getLongitude() + "&" +
                DataBaseHandler.DATE + "=" + measurement.getDate() + "&"+
                DataBaseHandler.WEIGHT + "=" + measurement.getWeight() + "&" +
                DataBaseHandler.USER_ID+ "="+ measurement.getUserID() + "&" +
                DEVICE + "=" + sharedPreferences.getString(getResources().getString(R.string.deviceID),"");
        return  parametrs;
    }
}