package pl.gda.pg.eti.kask.soundmeterpg.Services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.MainActivity;
import pl.gda.pg.eti.kask.soundmeterpg.Fragments.*;
import pl.gda.pg.eti.kask.soundmeterpg.IntentActionsAndKeys;
import pl.gda.pg.eti.kask.soundmeterpg.MeasurementDataBaseManager;
import pl.gda.pg.eti.kask.soundmeterpg.MutableInteger;
import pl.gda.pg.eti.kask.soundmeterpg.R;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.FakeLocation;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.MeasureStatistic;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.MeasurementStatistics;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.PreferenceParser;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Sample;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.SexigesimalLocation;

/**
 * Created by Daniel on 14.10.2016.
 */

public class BackgroundWork extends IntentService {

    private volatile boolean endWork =false;
    private MutableInteger counterSampleAvg = new MutableInteger();
    private MeasurementStatistics statistic;
    private NotificationCompat.Builder notification;
    private NotificationManager manager;
    private int notifyId = 1;

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            IntentActionsAndKeys action =  IntentActionsAndKeys.valueOf(intent.getAction());
            if(!endWork)
            switch(action){
                case END_ACTION_WORKING_BACKGROUND:
                        endWork =  true;
                    break;
                case SAMPLE_RECEIVE_ACTION:
                    Sample sample = intent.getExtras().getParcelable(IntentActionsAndKeys.SAMPLE_KEY.toString());
                    updateNotification(sample);
                    break;
                case ERROR_MEASURE_ACTION:
                        endWork =  true;
                    break;
            }
        }
    };
    public BackgroundWork(String name) {
        super(name);
    }
    public BackgroundWork() {
        super("Bacground Work");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        createNotification();
        while(!endWork){
            try{
                Thread.sleep(230);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        manager.cancelAll();
        Log.i("Backgroundwork","end");
    }

    private void updateNotification(Sample sample){
        int noiseLevel =sample.getNoiseLevel();
        int avg;
        avg = MeasureStatistic.setUpStatistic(noiseLevel,statistic,counterSampleAvg);
        String msg = String.valueOf(noiseLevel)+" db";

        if(!(sample.getLocation() instanceof FakeLocation)) {
            SexigesimalLocation location = sample.getLocation().convertLocation();
            String lat = sample.getLocation().getLatitude()>0 ? "N" : "S";
            String lon = sample.getLocation().getLongitude()>0 ?"E" : "W";
            msg = msg + "\n" + MeasureStatistic.getLatitude(location) +lat;
            msg = msg + "\n" + MeasureStatistic.getLongitude(location)+lon;
        }
        notification.setContentText(msg);
        manager.notify(notifyId, notification.build());
    }

    private void createNotification() {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        PendingIntent contentIntent = PendingIntent.getActivity(getBaseContext(),0,intent,0);
        notification = new NotificationCompat.Builder(this)
                                                .setSmallIcon(R.mipmap.ic_politechnika)
                                                .setContentTitle("Actual measure")
                                                .setContentText("0db")
                                                .setPriority(2)
                                                .setContentIntent(contentIntent)
                                                .setOngoing(true);


        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(notifyId, notification.build());
    }


    @Override
    public void onCreate() {
        super.onCreate();
        statistic =  new MeasurementStatistics();
        IntentFilter filter =  new IntentFilter();
        filter.addAction(IntentActionsAndKeys.ERROR_MEASURE_ACTION.toString());
        filter.addAction(IntentActionsAndKeys.SAMPLE_RECEIVE_ACTION.toString());
        filter.addAction(IntentActionsAndKeys.END_ACTION_WORKING_BACKGROUND.toString());
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);

    }
}
