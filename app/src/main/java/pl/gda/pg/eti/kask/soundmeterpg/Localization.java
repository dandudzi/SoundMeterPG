package pl.gda.pg.eti.kask.soundmeterpg;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

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

import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.NullLocalizationException;
import pl.gda.pg.eti.kask.soundmeterpg.Internet.ConnectionInternetDetector;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.PreferenceParser;

public class Localization extends Service implements LocationListener {

    private static final long MIN_DISTANCE = 10;
    private static final long MIN_TIME = 1000 * 60 * 1;
    private final IBinder _mBinder = new LocalBinder();
    private LocationManager _locationManager;

    private Context _context;
    private ConnectionInternetDetector _connectionInternetDetector;
    private PreferenceParser _preferenceParser;


    public Location getLocalization() throws NullLocalizationException {
        List<String> providers = new ArrayList<String>();
        Location bestLocation = null;
        if (canUseInternetProvider()) providers.add(LocationManager.NETWORK_PROVIDER);

        if (canUseGpsProvider()) providers.add(LocationManager.GPS_PROVIDER);

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
        if (bestLocation == null)
            throw new NullLocalizationException("Cannot get Localization, check if you have privileges to use GPS or Internet provider");
        else return bestLocation;
    }

    public boolean canUseLocation() {
        return canUseGpsProvider() || canUseInternetProvider();
    }

    public void stopUsingGPS() {
        _locationManager.removeUpdates(Localization.this);
    }

    private boolean canUseInternetProvider() {
        return (_locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) &&
                _preferenceParser.hasPrivilegesToUseInternet());
    }

    private boolean canUseGpsProvider() {
        return (_locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) &&
                _preferenceParser.hasPrivilegesToUseGPS());
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
        _context = getBaseContext();
        _locationManager = (LocationManager) _context.getSystemService(LOCATION_SERVICE);
        _preferenceParser = new PreferenceParser(_context);
        _connectionInternetDetector = new ConnectionInternetDetector(_context);
        _connectionInternetDetector = new ConnectionInternetDetector(_context);
        return _mBinder;
    }

    public class LocalBinder extends Binder {
        Localization getService() {
            //zwracamy instancje serwisu, przez nią odwołamy się następnie do metod.
            return Localization.this;
        }
    }
}
