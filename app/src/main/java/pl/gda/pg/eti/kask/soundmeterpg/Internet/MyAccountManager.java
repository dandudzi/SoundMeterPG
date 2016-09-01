package pl.gda.pg.eti.kask.soundmeterpg.Internet;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import pl.gda.pg.eti.kask.soundmeterpg.Interfaces.AccountManager;
import pl.gda.pg.eti.kask.soundmeterpg.R;

/**
 * Created by Daniel on 15.08.2016 at 11:10 :).
 */
public class MyAccountManager implements AccountManager {
    public static final int MAX_DURATION_OF_LOG_IN = 30000;
    public static final String login = "filgirel";
    public static final String password = "Nadal123Podresla!@$$";

    private SynchronizedPreference prefs;
    private final String key = "isLogIn";
    private Activity activity;
    private int progressOfLogging =0;
    private String errorMessage;

    public MyAccountManager(Activity activity){
        this.activity = activity;
        prefs = SynchronizedPreference.getInstance();
    }

    @Override
    public boolean isLogIn() {
        return prefs.getBoolean(key,false,activity);
    }

    @Override
    public void logIn(String login, String password, String mac) {
        progressOfLogging=0;
        //TODO usuń ta linijkę jak zrobisz panel wylogowania
        prefs.putBoolean(key,false,activity);

        Thread loggingThread;
        if(login.equals(MyAccountManager.login) && password.equals(MyAccountManager.password))
            loggingThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    prefs.putBoolean(key,true,activity);
                    for (int i = 0; i < 10; i++) {
                        addToProgress(3000);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        else if(login.equals("")  && password.equals(""))
            loggingThread =  new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 29; i++) {
                        addToProgress(1000);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    String msg = activity.getString(R.string.timeout_error_message_login_activity);
                    setErrorMessage(msg);
                    setProgressOfLogging(MAX_DURATION_OF_LOG_IN);
                }
            });
        else
            loggingThread =  new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 5; i++) {
                        addToProgress(3000);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    String msg = activity.getString(R.string.incorrect_credentials_login_activity);
                    setErrorMessage(msg);
                    setProgressOfLogging(MAX_DURATION_OF_LOG_IN);
                }
            });
        loggingThread.start();
    }

    @Override
    public void logOut() {
        prefs.putBoolean(key,false,activity);
    }

    @Override
    public synchronized int getProgress() {
        return progressOfLogging;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    private synchronized void addToProgress(int value){
        progressOfLogging += value;
    }

    private synchronized void setProgressOfLogging(int value){progressOfLogging = value;}

    private synchronized void setErrorMessage(String msg){errorMessage = msg;}
}

class SynchronizedPreference{

    private static class SingletonHolder {
        public static final SynchronizedPreference instance = new SynchronizedPreference();
    }

    private SynchronizedPreference(){
    }

    public static SynchronizedPreference getInstance(){
        return SingletonHolder.instance;
    }

    public synchronized boolean getBoolean(String key, boolean defValue, Activity activity){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getBoolean(key, defValue);
    }


    public synchronized void putBoolean(String key, boolean value, Activity activity){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key,value);
        editor.commit();
    }

}
