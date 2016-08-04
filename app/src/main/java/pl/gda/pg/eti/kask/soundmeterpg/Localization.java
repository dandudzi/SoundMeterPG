package pl.gda.pg.eti.kask.soundmeterpg;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by gierl on 13.07.2016.
 */

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Localization extends Service implements LocationListener {

    private static final long MIN_DISTANCE = 10;
    private static final long MIN_TIME = 1000 * 60 * 1;
    private boolean _GPSEnabled;
    private boolean _networkEnabled;
    private boolean _canGetLocalization;
    private LocationManager _locationManager;
    private Context _context;


    public Localization(Context context) {
        try {
            _context = context;
            _locationManager = (LocationManager) _context.getSystemService(LOCATION_SERVICE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enableGPS() {
        _GPSEnabled = true;
    }

    public void enableNetwork() {
        _networkEnabled = true;
    }

    public void disableGPS() {
        _GPSEnabled = false;
    }

    public void disableNetwork() {
        _networkEnabled = false;
    }

    public Location getLocalization() {
        List<String> providers = new ArrayList<String>();
        Location bestLocation = null;
        if (_networkEnabled) providers.add(LocationManager.NETWORK_PROVIDER);
        if (_GPSEnabled) providers.add(LocationManager.GPS_PROVIDER);
        for (String provider : providers) {
            Location l = null;
            try {
                _locationManager.requestLocationUpdates(provider, MIN_TIME, MIN_DISTANCE, this);
                l = _locationManager.getLastKnownLocation(provider);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    public void stopUsingGPS() {
        _locationManager.removeUpdates(Localization.this);
    }

    public boolean canGetLocalization() {
        _canGetLocalization = ((_GPSEnabled) || (_networkEnabled) ? true : false);
        return _canGetLocalization;
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
