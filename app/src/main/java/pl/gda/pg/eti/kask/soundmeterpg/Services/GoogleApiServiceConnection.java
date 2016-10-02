package pl.gda.pg.eti.kask.soundmeterpg.Services;

import android.app.Service;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * Created by gierl on 01.10.2016.
 */
public class GoogleApiServiceConnection implements ServiceConnection {

    private Measure parentService;

    public GoogleApiServiceConnection(Measure service){
        parentService = service;
    }
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        GoogleAPILocalization.LocalBinder binder = (GoogleAPILocalization.LocalBinder) iBinder;
        parentService.setGoogleAPILocalization(binder.getService());
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        parentService.setGoogleAPILocalization(null);
    }
}
