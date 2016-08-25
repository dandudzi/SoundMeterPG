package pl.gda.pg.eti.kask.soundmeterpg;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import pl.gda.pg.eti.kask.soundmeterpg.Exception.NullRecordException;

public class Insert extends Service {

    public static final String SITE = "http://soundmeterpg.cba.pl/insert.php/";
    public static final String NOISE = "noiseDB=";
    public static final String LATITUDE = "&Latitude=";
    public static final String LONGITUDE = "&Longitude=";
    private final IBinder mBinder = new LocalBinder();
    private Context _context;
    private boolean _networkEnabled;
    private static boolean _runningService = false;
    //public Insert(){};
    // public Insert(Context context){
    //     _context = context;
    //  }

    public class LocalBinder extends Binder {
        Insert getService() {
            //zwracamy instancje serwisu, przez nią odwołamy się następnie do metod.
            return Insert.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


/*
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!_runningService)
        return START_STICKY;
        return  super.onStartCommand(intent,flags,startId);
    }
    @Override
    public void onDestroy() {
        _runningService = false;
        super.onDestroy();
    }
    */

    public void enableNetwork() {
        _networkEnabled = true;
    }

    ;

    public void disableNetwork() {
        _networkEnabled = false;
    }

    public boolean getNetworkConnection() {
        return _networkEnabled;
    }

    public boolean isConnectionWithServer(String url) {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 " + url);
            int mExitValue = mIpAddrProcess.waitFor();
            if (mExitValue == 0) {
                return true;
            } else {
                return false;
            }
        } catch (InterruptedException ignore) {
            ignore.printStackTrace();
            System.out.println(" Exception:" + ignore);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(" Exception:" + e);
        }
        return false;
    }

    public boolean insert(Probe probe) throws NullRecordException {
        if (probe == null) {
            throw new NullRecordException("The probe is null!");
        }
        HttpURLConnection connection;
        URL url = null;
        OutputStreamWriter request = null;
        String response;
        String parameters = NOISE + probe.getAvgNoiseLevel() + LATITUDE + probe.getLatitude() + LONGITUDE + probe.getLongitude();
        try {
            url = new URL(SITE);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("POST");
            request = new OutputStreamWriter(connection.getOutputStream());
            request.write(parameters);
            request.flush();
            request.close();
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
            return true;

        } catch (MalformedURLException e1) {
            e1.printStackTrace();
            return false;
        } catch (IOException e1) {
            e1.printStackTrace();
            return false;
        }
    }
}