package pl.gda.pg.eti.kask.soundmeterpg.Services;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * Created by gierl on 01.10.2016.
 */

public class Measure extends IntentService {
    public static  final String END_ACTION = "pl.gda.pg.eti.kask.STOP_MEASURE_SERVICE";

    private GoogleAPILocalization googleAPILocalization;
    private Sender sender;

    private ServiceConnection localizationConnection = new GoogleApiServiceConnection(this);
    private ServiceConnection senderConnection= new SenderServiceConnection(this);
    private Thread binderThread;
    private volatile boolean endMeasure = false;

    private BroadcastReceiver endTaskReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            synchronized (Measure.this){
                endMeasure = true;
            }
        }
    };

    public Measure() {
        super("Measure");
    }

    @Override
    protected void onHandleIntent(Intent _intent) {
       // mainThread.start();
        try {
            binderThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent("custom-event-name");
        // You can also include some extra data.
        intent.putExtra("message", "This is my message!");
        LocalBroadcastManager.getInstance(Measure.this).sendBroadcast(intent);
        while(!endMeasure){
            Log.i("Daj","SampleCreator");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LocalBroadcastManager.getInstance(this).registerReceiver(endTaskReceiver, new IntentFilter(END_ACTION));
        bindServices();
        setUpThreads();

        binderThread.start();

    }

    private void bindServices() {
        Intent googleIntent = new Intent(getBaseContext(),GoogleAPILocalization.class);
        Intent senderIntent = new Intent(getBaseContext(),Sender.class);
        getBaseContext().bindService(googleIntent, localizationConnection, Context.BIND_AUTO_CREATE);
        getBaseContext().bindService(senderIntent, senderConnection, Context.BIND_AUTO_CREATE);
    }

    private void setUpThreads() {
        //Thread for binding
        binderThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (sender != null && googleAPILocalization != null)
                        break;
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        synchronized (this) {
            if (googleAPILocalization != null)
                getBaseContext().unbindService(localizationConnection);

            if(sender !=  null)
                getBaseContext().unbindService(senderConnection);
        }
    }

    synchronized public void setSender(Sender sender){
        this.sender = sender;
    }

    synchronized public void  setGoogleAPILocalization(GoogleAPILocalization googleAPILocalization){
        this.googleAPILocalization = googleAPILocalization;
    }
}
