package pl.gda.pg.eti.kask.soundmeterpg.Services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import pl.gda.pg.eti.kask.soundmeterpg.Database.DataBaseHandler;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.TurnOffGPSException;
import pl.gda.pg.eti.kask.soundmeterpg.MeasurementDataBaseManager;
import pl.gda.pg.eti.kask.soundmeterpg.R;
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
    private android.location.Location mainLocation;
    private Context context;
    private PreferenceParser preferenceParser;
    private MeasurementDataBaseManager measurementDataBaseManager;

    @Override
    public void onCreate() {
        context = getBaseContext();
        preferenceParser = new PreferenceParser(context);
        measurementDataBaseManager = new MeasurementDataBaseManager(context, preferenceParser);

        if (checkPlayServices()) {
            buildGoogleApiClient();
            createLocationRequest();
        }
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            stopLocationUpdates();
            googleApiClient.disconnect();
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (googleApiClient != null) {
            googleApiClient.connect();
            if (googleApiClient.isConnected()) {
                checkPlayServices();
                if (googleApiClient != null && googleApiClient.isConnected()) {
                    startLocationUpdates();
                }
            }
        }
        return localBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            stopLocationUpdates();
            googleApiClient.disconnect();
        }
        return super.onUnbind(intent);
    }

    private boolean canUseGPS() {
        return (ServiceDetector.isGPSEnabled(context));
    }

    public Location getLocation() throws TurnOffGPSException {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                !canUseGPS()) {
            throw new TurnOffGPSException("GPS is turn off.");
        } else {
            //lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (googleApiClient == null)
                return null;

           // final android.location.Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (mainLocation == null) {
                return null;
        }
            else
             lastKnownLocation = new Location(mainLocation.getLatitude(), mainLocation.getLongitude());
            //jezeli różnica ejst większa od 15 metrow to zapisuje pomiary i mainLocation sie zmienia
           /* else if (distanceBeteenLocation(mainLocation, location) > 15) {
                Handler handler = new Handler(Looper.getMainLooper());

                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "ZMIANA : Lat : "+location.getLatitude()+ ", Long: " +location.getLongitude(), Toast.LENGTH_LONG).show();

                        //Toast.makeText(TrackerService.this.getApplicationContext(),"Video is playing...",Toast.LENGTH_SHORT).show();
                    }
                });
                measurementDataBaseManager.flush();
                mainLocation = location;
            }
            */

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
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
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
        if (googleApiClient.isConnected())
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
       // Toast.makeText(getApplicationContext(), "ZMIANA : Lat : "+location.getLatitude()+ ", Long: " +location.getLongitude(), Toast.LENGTH_LONG).show();

        if (mainLocation == null) {
            mainLocation = new android.location.Location(location);
        }
        //jezeli różnica ejst większa od 15 metrow to zapisuje pomiary i mainLocation sie zmienia
        else if (distanceBeteenLocation(mainLocation, location) > 15) {
            measurementDataBaseManager.flush();
            mainLocation = location;
        }

    }

    public class LocalBinder extends Binder {
        public GoogleAPILocalization getService() {
            return GoogleAPILocalization.this;
        }
    }

    private double distanceBeteenLocation(android.location.Location oldLocation, android.location.Location newLocation) {

        double dlong = (newLocation.getLongitude() - oldLocation.getLongitude())  * (Math.PI / 180);
        double dlat = (newLocation.getLatitude() - oldLocation.getLatitude()) * (Math.PI / 180);
        double a = Math.pow(Math.sin(dlat/2.0), 2) + Math.cos(newLocation.getLatitude()* (Math.PI / 180)) * Math.cos(oldLocation.getLatitude() * (Math.PI / 180)) * Math.pow(Math.sin(dlong/2.0), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = 6367 * c;
        return d* 1000;
    }
}