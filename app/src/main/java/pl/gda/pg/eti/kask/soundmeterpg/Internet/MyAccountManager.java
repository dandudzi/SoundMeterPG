package pl.gda.pg.eti.kask.soundmeterpg.Internet;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.HttpAuthHandler;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.EndTaskException;
import pl.gda.pg.eti.kask.soundmeterpg.Interfaces.AccountManager;
import pl.gda.pg.eti.kask.soundmeterpg.R;

/**
 * Created by Daniel on 15.08.2016 at 11:10 :).
 */
public class MyAccountManager implements AccountManager  {
    public static final int MAX_DURATION_OF_LOG_IN = 30000;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private SynchronizedPreference prefs;
    private final String key = "isLogIn";
    private Activity activity;
    private int progressOfLogging =0;
    private String errorMessage;
    private volatile boolean endTask = false;
    private Thread loggingThread;
    private Thread loggedThread;
    private  String sessionId = "sec_session_id=4ec58d39958f0sde8d1a3841c0b4304660";
    CookieManager cookieManager = null;
    URLConnection urlConnection = null;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    public MyAccountManager(Activity activity){
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        editor = preferences.edit();

        this.activity = activity;
        cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        prefs = SynchronizedPreference.getInstance();
    }

    @Override
    public boolean isLogIn() {
        return prefs.getBoolean(key,false,activity);
    }

    @Override
    public void logIn(final String login, final String password, String mac) {
        progressOfLogging=0;
        //TODO usuń ta linijkę jak zrobisz panel wylogowania
        prefs.putBoolean(key,false,activity);

        endTask = false;
        createLoginThread(login, password);
        /*
        if(login.equals(MyAccountManager.login) && password.equals(MyAccountManager.password))
            loggingThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    prefs.putBoolean(key,true,activity);
                    int progressStep = 3000;
                    int forTimesLoop = 10;
                    int timeToSleepInMilliseconds = 1000;
                    try {
                        emulateLoggingInServer(progressStep, forTimesLoop, timeToSleepInMilliseconds);
                    } catch (EndTaskException e) {
                        Log.i("MyAccountManager","EndTask");
                    }
                }
            });
        else if(login.equals("")  && password.equals(""))
            loggingThread =  new Thread(new Runnable() {
                @Override
                public void run() {
                    int progressStep = 1000;
                    int forTimesLoop = 29;
                    int timeToSleepInMilliseconds = 1000;
                    try {
                        emulateLoggingInServer(progressStep, forTimesLoop, timeToSleepInMilliseconds);
                        String msg = activity.getString(R.string.timeout_error_message_login_activity);
                        setErrorMessage(msg);
                        setProgressOfLogging(MAX_DURATION_OF_LOG_IN);
                    } catch (EndTaskException e) {
                        Log.i("MyAccountManager","EndTask");
                    }

                }
            });
        else
            loggingThread =  new Thread(new Runnable() {
                @Override
                public void run() {
                    int progressStep = 3000;
                    int forTimesLoop = 5;
                    int timeToSleepInMilliseconds = 1000;
                    try {
                        emulateLoggingInServer(progressStep, forTimesLoop, timeToSleepInMilliseconds);
                        String msg = activity.getString(R.string.incorrect_credentials_login_activity);
                        setErrorMessage(msg);
                        setProgressOfLogging(MAX_DURATION_OF_LOG_IN);
                    } catch (EndTaskException e) {
                        Log.i("MyAccountManager","EndTask");
                    }

                }
            });
            */
        loggingThread.start();
    }

    private void createLoginThread(final String login, final String password) {

        loggingThread = new Thread(new Runnable() {
            @Override
            public void run() {

                HttpURLConnection conn = null;
                URL url = null;

                try {
                    url = new URL("https://soundmeterpg.pl/insert_android");

                    conn = initializeConnection(conn, url);
                    String query = initializeArguments(login, password);
                    openConnection(conn, query);
                    conn.connect();
                }
                catch (IOException e1) {
                e1.printStackTrace();
                }

                getResponse(conn);
            }
        });
    }


    public void checkIfUserIsLogged(){
        createLoggedThread();
        loggedThread.start();
    }
    private void createLoggedThread() {

        loggedThread = new Thread(new Runnable() {
            @Override
            public void run() {
                URL uUrl = null;
                try {
                    /*if(cookieManager != null && cookieManager.getCookieStore().getCookies().size()>0) {
                        List coockies = cookieManager.getCookieStore().getCookies();
                        String daj = coockies.get(0).toString();
                    }
                    */

                    uUrl = new URL("https://soundmeterpg.pl/checkSessionAndroid");

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                HttpURLConnection huc = null;
                try {
                     huc = (HttpURLConnection) uUrl.openConnection();

                } catch (IOException e) {
                    e.printStackTrace();
                }
              /* if (cookieManager.getCookieStore().getCookies().size() > 0) {
                    // While joining the Cookies, use ',' or ';' as needed. Most of the servers are using ';'
                    huc.setRequestProperty("Cookie",
                          TextUtils.join(";",  cookieManager.getCookieStore().getCookies()));
             }
*/
           // sessionId = preferences.getString("session_id","");
              //  huc.setRequestProperty("Cookie", sessionId);
             //   sessionId = preferences.getString("session_id","");
              // huc.setRequestProperty("Cookie", sessionId);    //Why is "Cookie", open Chrome press F12 and check!
              //  if(cookieManager != null)
              //  {

                //        String expiers = TextUtils.join(";",  cookieManager.getCookieStore().getCookies());
               //     huc.setRequestProperty("Cookie",
                            //TextUtils.join(";",  cookieManager.getCookieStore().getCookies()));
//
             //   }
              ///  huc.setRequestProperty("Cookie", sessionId);

                try {
                    huc.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new InputStreamReader(huc.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String data = "";
                    String line = "";
                try {
                   // String[] aaa = huc.getHeaderField("Set-Cookie").split(";");

                 //   sessionId = aaa[0];
                    //editor.putString("session_id", String.valueOf(aaa));
                   // editor.apply();
                    while ((line = br.readLine()) != null) {
                        data = data + line;
                    }
                    Map<String, List<String>> headerFields = huc.getHeaderFields();
                    List<String> cookiesHeader = headerFields.get("Set-Cookie");

                   // if (cookiesHeader != null) {
                       // for (String cookie : cookiesHeader) {
                      //      cookieManager.getCookieStore().add(null,HttpCookie.parse(cookie).get(0));
                      //  }
                  //  }
                    huc.disconnect();
                    Log.i("LoginCheck", data);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });
    }



    private void getResponse(HttpURLConnection conn) {
        try {
            int response_code = conn.getResponseCode();
            if (response_code == HttpURLConnection.HTTP_OK) {

                StringBuilder result = getStringResponse(conn);

               //


                if (result.toString().contains("success")) {
                 /*   String[] aaa = conn.getHeaderField("Set-Cookie").split(";");
                    // sessionId = aaa[0];
                   ;
                    //editor.apply();
                   // editor.putString("session_id", String.valueOf(aaa))
                    Map<String, List<String>> headerFields = conn.getHeaderFields();
                    List<String> cookiesHeader = headerFields.get("Set-Cookie");

                    if (cookiesHeader != null) {
                        for (String cookie : cookiesHeader) {
                            cookieManager.getCookieStore().add(null,HttpCookie.parse(cookie).get(0));
                        }
                    }

                  //  urlConnection.getContent();
                   // CookieStore cookieStore = cookieManager.getCookieStore();
                   // List cookieList = cookieStore.getCookies();
*/
                    prefs.putBoolean(key, true, activity);
                }
                else{
                    prefs.putBoolean(key, false, activity);
                    String msg = activity.getString(R.string.incorrect_credentials_login_activity);
                    setErrorMessage(msg);
                    setProgressOfLogging(MAX_DURATION_OF_LOG_IN);
                }
                conn.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    private StringBuilder getStringResponse(HttpURLConnection conn) throws IOException {
        InputStream input = conn.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuilder result = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        return result;
    }

    private void openConnection(HttpURLConnection conn, String query) {
        OutputStream os = null;
        try {
            os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String initializeArguments(String login, String password) {
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("email", login)
                .appendQueryParameter("password", password);
        return builder.build().getEncodedQuery();
    }

    @NonNull
    private HttpURLConnection initializeConnection(HttpURLConnection conn, URL url){
        try {
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
          //  conn.addRequestProperty("Coockie", sessionId);
             urlConnection = url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        conn.setDoInput(true);
        conn.setDoOutput(true);
        return conn;
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

    @Override
    public synchronized void stopLogInProcess() {
        if(loggingThread !=  null && loggingThread.isAlive())
            endTask = true;
    }

    private void emulateLoggingInServer(int progressStep, int forTimesLoop, int timeToSleepInMilliseconds) throws EndTaskException {
        for(int i=0;i<forTimesLoop;i++){
            addToProgress(progressStep);
            endTaskIfNecessary();
            try {
                Thread.sleep(timeToSleepInMilliseconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void endTaskIfNecessary() throws EndTaskException {
        if(endTask){
            prefs.putBoolean(key,false,activity);
            String msg = activity.getString(R.string.end_task_login_Activity);
            setErrorMessage(msg);
            addToProgress(MAX_DURATION_OF_LOG_IN);
            throw new EndTaskException();
        }
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
