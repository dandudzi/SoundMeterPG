package pl.gda.pg.eti.kask.soundmeterpg;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;

/**
 * Created by gierl on 24.06.2016.
 */
//TODO ta klasa jest cała do zmiany ze względu na to, że została stworzona w prototypie
public class Insert extends AsyncTask<Probe, Void, Void> {
    String link = "http://localhost/WebMeterPG/insert.php?noiseDB=";
    String latitude = "&Latitude=";
    String longitude = "&Longitude=";

    public Insert() {
    }

    @Override
    protected Void doInBackground(Probe... probes) {


        URL url = null;
        try {
            String s = link + probes[0].getAvgNoiseLevel() + latitude + probes[0].getLatitude() + longitude + probes[0].getLongitude();
            url = new URL(link + probes[0].getAvgNoiseLevel() + latitude + probes[0].getLatitude() + longitude + probes[0].getLongitude());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        try {
            conn.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        conn.setDoInput(true);
        // Starts the query
        try {
            conn.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int response = 0;
        try {
            response = conn.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("The response is: ", String.valueOf(response));
        InputStream is = null;
        try {
            is = conn.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Convert the InputStream into a string
        Reader reader = null;
        try {
            reader = new InputStreamReader(is, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        char[] buffer = new char[100];
        try {
            reader.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String _response = new String(buffer);


        return null;
    }
}
