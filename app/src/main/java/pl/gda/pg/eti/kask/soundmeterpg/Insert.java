package pl.gda.pg.eti.kask.soundmeterpg;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import pl.gda.pg.eti.kask.soundmeterpg.Exception.NullRecordException;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.ConnectionInternetDetector;

public class Insert extends Service {

    private final IBinder mBinder = new LocalBinder();
    private ConnectionInternetDetector _connectionInternetDetector;
    public class LocalBinder extends Binder {
        Insert getService() {
            //zwracamy instancje serwisu, przez nią odwołamy się następnie do metod.
            return Insert.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        _connectionInternetDetector = new ConnectionInternetDetector(getBaseContext());
        return mBinder;
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
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return false;
        }
    }

    public boolean insert(Probe probe) throws NullRecordException {
        if (probe == null) {
            throw new NullRecordException("The probe is null!");
        }
        HttpURLConnection connection;
        URL url = null;
        OutputStreamWriter request = null;
        String response;
        String parameters = getResources().getString(R.string.noiseDB_attribute) + probe.getAvgNoiseLevel() +
                getResources().getString(R.string.latitude_attribute) + probe.getLatitude() +
                getResources().getString(R.string.longitude_attribute) + probe.getLongitude();
        try {
            url = new URL(getResources().getString(R.string.site));
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
            if (response.equals(getResources().getString(R.string.confirmation_added_record)))
            return true;
            else
                return false;

        } catch (MalformedURLException e1) {
            e1.printStackTrace();
            return false;
        } catch (IOException e1) {
            e1.printStackTrace();
            return false;
        }

    }
}