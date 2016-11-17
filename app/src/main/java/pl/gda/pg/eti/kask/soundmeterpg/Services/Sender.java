package pl.gda.pg.eti.kask.soundmeterpg.Services;

import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import pl.gda.pg.eti.kask.soundmeterpg.Database.DataBaseHandler;
import pl.gda.pg.eti.kask.soundmeterpg.Database.MeasurementDataBaseObject;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.NullRecordException;
import pl.gda.pg.eti.kask.soundmeterpg.IntentActionsAndKeys;
import pl.gda.pg.eti.kask.soundmeterpg.Internet.MyAccountManager;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Measurement;
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

        int counterToRefreshSession =0;
        final int REFRESH_SESSION = 5; //po 5* czas sleepa = 50sekund
        MyAccountManager accountManager = new MyAccountManager(getBaseContext());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        MeasurementDataBaseObject measurementDataBaseObject;

        while (!endMeasure) {
            try {
                Thread.sleep(10000);
                counterToRefreshSession++;
                if (counterToRefreshSession == REFRESH_SESSION) {
                    counterToRefreshSession = 0;
                    if (!accountManager.checkIfUserIsLogged()) {
                        //TODO NOTYFIKACJA
                    }
                }
                    long timeInMillis = prefs.getLong(getBaseContext().getResources().getString(R.string.cookie_expired_time), (long) 0);
                    if (timeInMillis != 0) {
                        Date resultdate = new Date(timeInMillis);
                        if (resultdate.after(new Date())) {//nie musze sprawdzac przez neta czy zalogowany
                            if (!endMeasure && isConnectionWithServer(SITE)) {
                                measurementDataBaseObject = dataBaseHandler.getTheOldestRowToSendToServer();
                                if (measurementDataBaseObject != null && insert(measurementDataBaseObject)) {
                                    dataBaseHandler.erease(measurementDataBaseObject.getID());
                                }
                            }
                        }
                    }
            }
            catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        Log.i("Sender class", "STOPING SERVICE");

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
        if (isConnectionWithServer("soundmeterpg.pl")) {
            try {
                HttpURLConnection connection;
                URL url = new URL("https://soundmeterpg.pl/insert");
                OutputStreamWriter request = null;
                String response;
                String parametr = createStringParametr(measurement);
                connection = setConnectionArguments(url);
                setRequestArgument(connection, parametr);
                response = getResponse(connection);
                Log.i("RESPONSE FROM SEND ", response.toString());
                if (response.contains(getResources().getString(R.string.confirmation_added_record)))
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
                DataBaseHandler.DATE + "='" + measurement.getDate() + "'&"+
                DataBaseHandler.WEIGHT + "=" + measurement.getWeight() + "&" +
                DataBaseHandler.USER_ID+ "='"+ measurement.getUserID()+ "'";
        return  parametrs;
    }
}