package pl.gda.pg.eti.kask.soundmeterpg.Services;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * Created by gierl on 01.10.2016.
 */
public class SenderServiceConnection implements ServiceConnection {

    private Measure parentService;

    public SenderServiceConnection(Measure service){
        parentService = service;
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        Sender.LocalBinder binder = (Sender.LocalBinder) iBinder;
        parentService.setSender(binder.getService());
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        parentService.setSender(null);
    }
}
