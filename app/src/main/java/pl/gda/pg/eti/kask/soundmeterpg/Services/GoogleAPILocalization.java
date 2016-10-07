package pl.gda.pg.eti.kask.soundmeterpg.Services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.TurnOffGPSException;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Location;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.PreferenceParser;

/**
 * Created by Filip Gierłowski.
 */
public class  GoogleAPILocalization extends Service implements ConnectionCallbacks,
        LocationListener, GoogleApiClient.OnConnectionFailedListener {

    private static GoogleApiClient googleApiClient;
    private static LocationRequest locationRequest;
    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 3000; // 3,75 sec
    private static int FATEST_INTERVAL = 1000; // 1 sec
    private static int DISPLACEMENT = 10; // 10 meters
    private final IBinder localBinder = new LocalBinder();
    private Location lastKnownLocation;
    private Context context;
    private PreferenceParser preferenceParser;

    @Override
    public void onCreate() {
        context = getBaseContext();
        preferenceParser = new PreferenceParser(context);
        if (checkPlayServices()) {
            buildGoogleApiClient();
            createLocationRequest();
        }
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        if (googleApiClient.isConnected()) {
            stopLocationUpdates();
            googleApiClient.disconnect();
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (googleApiClient != null)
            googleApiClient.connect();
        checkPlayServices();
        if (googleApiClient.isConnected()) {
            startLocationUpdates();
        }

        return localBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (googleApiClient.isConnected()) {
            stopLocationUpdates();
            googleApiClient.disconnect();
        }
        return super.onUnbind(intent);
    }

    public boolean canUseGPS() {
        return (ServiceDetector.isGPSEnabled(context));
    }

    public Location getLocation() throws TurnOffGPSException {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                !canUseGPS()) {
            throw new TurnOffGPSException("GPS is turn off.");
        } else {
            //lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            android.location.Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if(location == null)
                return null;
            else
            lastKnownLocation = new Location(location.getLatitude(), location.getLongitude());
        }
        return lastKnownLocation;
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        return resultCode == ConnectionResult.SUCCESS;
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FATEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    protected void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                googleApiClient, this);
    }

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("", "Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        lastKnownLocation = new Location(location.getLatitude(), location.getLongitude());
    }

    public class LocalBinder extends Binder {
        public GoogleAPILocalization getService() {
            return GoogleAPILocalization.this;
        }
    }

}