package pl.gda.pg.eti.kask.soundmeterpg.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.NullRecordException;
import pl.gda.pg.eti.kask.soundmeterpg.Sample;
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
                Process mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 " + url);
                int mExitValue = mIpAddrProcess.waitFor();
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

    public boolean insert(Sample sample) throws NullRecordException {
        if (sample == null) {
            throw new NullRecordException("The probe is null!");
        }
        if (preferenceParser.hasPermissionToUseInternet() && connectionInternetDetector.isConnectingToInternet()
                && isConnectionWithServer("soundmeterpg.cba.pl")) {
            HttpURLConnection connection;
            URL url = null;
            OutputStreamWriter request = null;
            String response;
            String parameters = getResources().getString(R.string.noise) + "=" + sample.getAvgNoiseLevel() +
                    "&" + getResources().getString(R.string.latitude) + "=" + sample.getLatitude() +
                    "&" + getResources().getString(R.string.longitude) + "=" + sample.getLongitude();
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
                if (response.contains(getResources().getString(R.string.confirmation_added_record)))
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
        } else return false;

    }
}