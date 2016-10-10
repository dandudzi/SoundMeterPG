package pl.gda.pg.eti.kask.soundmeterpg.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import pl.gda.pg.eti.kask.soundmeterpg.Database.DataBaseHandler;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.NullRecordException;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Measurement;
import pl.gda.pg.eti.kask.soundmeterpg.R;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.ConnectionInternetDetector;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.PreferenceParser;

public class Sender extends Service {

    private final IBinder localBinder = new LocalBinder();
    private ConnectionInternetDetector connectionInternetDetector;
    private PreferenceParser preferenceParser;

    public class LocalBinder extends Binder {
        public Sender getService() {
            return Sender.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        connectionInternetDetector = new ConnectionInternetDetector(getBaseContext());
        preferenceParser = new PreferenceParser(getBaseContext());
        return localBinder;
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

    public boolean insert(Measurement measurement){
        if (measurement == null) {
           return  false;
        }
        if (isConnectionWithServer("soundmeterpg.cba.pl")) {
            try {
                HttpURLConnection connection;
                URL url = new URL(getResources().getString(R.string.site));
                OutputStreamWriter request = null;
                String response;
                String parametr = createStringParametr(measurement);
                connection = setConnectionArguments(url);
                setRequestArgument(connection, parametr);
                response = getResponse(connection);
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

    private String createStringParametr(Measurement measurement) {
        String parametrs = DataBaseHandler.MIN + "=" + measurement.getMin() + "&" +
                DataBaseHandler.MAX + "=" + measurement.getMax() + "&" +
                DataBaseHandler.AVG + "=" + measurement.getAvg() + "&" +
                DataBaseHandler.LATITUDE + "=" + measurement.getLocation().getLatitude() + "&" +
                DataBaseHandler.LONGITUDE + "=" + measurement.getLocation().getLongitude() + "&" +
                DataBaseHandler.DATE + "='" + measurement.getDate() + "'";
        return  parametrs;
    }
}