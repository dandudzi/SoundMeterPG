package pl.gda.pg.eti.kask.soundmeterpg.Internet;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pl.gda.pg.eti.kask.soundmeterpg.Interfaces.AccountManager;
import pl.gda.pg.eti.kask.soundmeterpg.R;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.ConnectionInternetDetector;

/**
 * Created by Daniel on 15.08.2016 at 11:10 :).
 */
public class MyAccountManager implements AccountManager  {
    public static final int MAX_DURATION_OF_LOG_IN = 30000;
    private SynchronizedPreference prefs;
    private Context context;
    private int progressOfLogging =0;
    private String errorMessage;
    private Thread loggingThread;
    private Thread loggedThread;
    private Thread logOutThread;
    CookieManager cookieManager = null;
    ConnectionInternetDetector connectionInternetDetector ;

    public MyAccountManager(Context context){
        this.context = context;
        cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        prefs = SynchronizedPreference.getInstance();
        connectionInternetDetector = new ConnectionInternetDetector(context);
    }

    @Override
    public boolean isLogIn() {
        return prefs.getBoolean(context.getResources().getString(R.string.logged_key),false,context);
    }

    @Override
    public void logIn(final String login, final String password, String mac) {
        progressOfLogging=0;
        prefs.putBoolean(context.getResources().getString(R.string.logged_key),false,context);
        createLoginThread(login, password);
        loggingThread.start();
    }

    public boolean checkIfUserIsLogged(){
        createIfUserIsLoggedThread();
        loggedThread.start();
        try {
            loggedThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return isLogIn();

    }

    private void createIfUserIsLoggedThread() {

        loggedThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (connectionInternetDetector.isConnectingToInternet()) {
                    URL url = null;
                    try {
                        url = new URL("https://soundmeterpg.pl/checkSessionAndroid");
                        HttpURLConnection conn = null;
                        conn = (HttpURLConnection) url.openConnection();

                        // Prawdopodobnie zamknięto aplikacje //
                        if (cookieManager.getCookieStore().getCookies().size() == 0) {
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                            long timeInMillis = preferences.getLong(context.getResources().getString(R.string.cookie_expired_time), 0);
                            Date currentTime = new Date();
                            Date resultdate = new Date(timeInMillis);

                            if (resultdate.after(currentTime)) {
                                long maxAge = resultdate.getTime() - currentTime.getTime();
                                maxAge /= 1000;
                                String name = preferences.getString(context.getResources().getString(R.string.cookie_name), "");
                                String value = preferences.getString(context.getResources().getString(R.string.cookie_value), "");
                                String path = preferences.getString(context.getResources().getString(R.string.cookie_path), "");
                                String domain = preferences.getString(context.getResources().getString(R.string.cookie_domain), "");

                                boolean secure = preferences.getBoolean(context.getResources().getString(R.string.cookie_secure), false);
                                HttpCookie cookie = new HttpCookie(name, value);
                                cookie.setPath(path);
                                cookie.setDomain(domain);
                                cookie.setMaxAge(maxAge);
                                cookie.setSecure(secure);
                                String cookiesss = name + "=" + value + ";" + "Path=" + path + ";" + "Domain=" + domain + ";maxAge=" + maxAge;
                                // String[] cookieString  = cookie.toString().split(";");
                                // Cookie", "cookieName=cookieValue; domain=www.test.com"
                                // String connections = preferences.getString(activity.getBaseContext().getString(R.string.cookie_name) ) + "=" +
                                //      name + ";" +  preferences.getString(activity.getBaseContext().getResources().getString(R.string.cookie_value) + "=" +
                                //      value);
                                //    conn.setRequestProperty("Cookie", preferences.getString(activity.getBaseContext().getString(R.string.cookie_name)) + "=" +
                                //   name + ";" +  preferences.getString(activity.getBaseContext().getResources().getString(R.string.cookie_value)) + "="+
                                //  value);;
                                conn.setRequestProperty("Cookie", cookiesss);
                            }
                        }

                        // HttpURLConnection conn = null;
                        //conn = (HttpURLConnection) url.openConnection();
                        conn.connect();
                        BufferedReader br = null;
                        br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String data = "";
                        String line = "";
                        while ((line = br.readLine()) != null) {
                            data = data + line;
                        }
                        if (data.contains("you are still logged")) {
                            prefs.putBoolean(context.getResources().getString(R.string.logged_key), true, context);
                            List<HttpCookie> cookies = cookieManager.getCookieStore().getCookies();
                            if (cookies != null && cookies.size() >0) {
                                //  String as = cookies
                                HttpCookie cookie = cookies.get(0);
                                storeCookie(cookie);
                            }
                        } else
                            prefs.putBoolean(context.getResources().getString(R.string.logged_key), false, context);
                        conn.disconnect();
                        Log.i("LoginCheck", data);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    prefs.putBoolean(context.getResources().getString(R.string.logged_key), false, context);
                }
            }
        });
    }

    private void createLoginThread(final String login, final String password) {
        loggingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if(connectionInternetDetector.isConnectingToInternet()) {
                    HttpURLConnection conn = null;
                    URL url = null;
                    try {
                        url = new URL("https://soundmeterpg.pl/insert_android");
                        conn = initializeConnection(conn, url);
                        String query = initializeArguments(login, password);
                        openConnection(conn, query);
                        conn.connect();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    getResponse(conn, login);
                }
                else
                    prefs.putBoolean(context.getResources().getString(R.string.logged_key),false,context);
            }
        });
    }
    private HttpURLConnection initializeConnection(HttpURLConnection conn, URL url){
        try {
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
        } catch (IOException e) {
            e.printStackTrace();
        }
        conn.setDoInput(true);
        conn.setDoOutput(true);
        return conn;
    }

    private String initializeArguments(String login, String password) {
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("email", login)
                .appendQueryParameter("password", password);
        return builder.build().getEncodedQuery();
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

    private void getResponse(HttpURLConnection conn, String login) {
        try {
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                StringBuilder result = getStringResponse(conn);
                if (result.toString().contains("success")) {
                    int index = result.indexOf(":");
                    //String login = result.substring(result.indexOf(":")+2);

                    prefs.putString(context.getResources().getString(R.string.login_key),login,context);
                    List<HttpCookie> cookies = cookieManager.getCookieStore().getCookies();
                    if(cookies != null && cookies.size() >0) {
                        HttpCookie cookie = cookies.get(0);
                       String coossss = cookie.toString();
                        storeCookie(cookie);
                    }

                    prefs.putBoolean(context.getResources().getString(R.string.logged_key), true, context);
                }
                else{
                    prefs.putBoolean(context.getResources().getString(R.string.logged_key), false, context);
                    String msg = context.getString(R.string.incorrect_credentials_login_activity);
                    setErrorMessage(msg);
                    setProgressOfLogging(MAX_DURATION_OF_LOG_IN);
                }
                conn.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void storeCookie(HttpCookie cookie) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        //calendar.add(Calendar.DATE, (int) cookie.getMaxAge());
        long timeInMillis = calendar.getTimeInMillis() + 1000 * cookie.getMaxAge();
        //Date resultdate = new Date(calendar.getTimeInMillis() + 1000* cookie.getMaxAge());
        // long resultDates = resultdate.getTime();

        //  String dateString = formatter.format(new Date(seconds * 1000L));


        prefs.putString(context.getResources().getString(R.string.cookie_name), cookie.getName(), context);
        prefs.putString(context.getResources().getString(R.string.cookie_value), cookie.getValue(), context);
        prefs.putString(context.getResources().getString(R.string.cookie_path), cookie.getPath(), context);
        prefs.putString(context.getResources().getString(R.string.cookie_domain), cookie.getDomain(), context);
        prefs.putLong(context.getResources().getString(R.string.cookie_expired_time), timeInMillis, context);
        prefs.putBoolean(context.getResources().getString(R.string.cookie_secure), cookie.getSecure(), context);
    }

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





    @Override
    public void logOut() {

      //  prefs.putBoolean(context.getResources().getString(R.string.logged_key),false,context);
        createLogOutThread();;
        logOutThread.start();
       // try {
         //   loggedThread.join();
     //   } catch (InterruptedException e) {
     //       e.printStackTrace();
      //  }
    }


    private void createLogOutThread() {

        logOutThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (connectionInternetDetector.isConnectingToInternet()) {
                    URL url = null;
                    try {
                        url = new URL("https://soundmeterpg.pl/logout_android");
                        HttpURLConnection conn = null;
                        conn = (HttpURLConnection) url.openConnection();

                        // Prawdopodobnie zamknięto aplikacje //
                        if (cookieManager.getCookieStore().getCookies().size() == 0) {
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                            long timeInMillis = preferences.getLong(context.getResources().getString(R.string.cookie_expired_time), 0);
                            Date currentTime = new Date();
                            Date resultdate = new Date(timeInMillis);

                            if (resultdate.after(currentTime)) {
                                long maxAge = resultdate.getTime() - currentTime.getTime();
                                maxAge /= 1000;
                                String name = preferences.getString(context.getResources().getString(R.string.cookie_name), "");
                                String value = preferences.getString(context.getResources().getString(R.string.cookie_value), "");
                                String path = preferences.getString(context.getResources().getString(R.string.cookie_path), "");
                                String domain = preferences.getString(context.getResources().getString(R.string.cookie_domain), "");

                                boolean secure = preferences.getBoolean(context.getResources().getString(R.string.cookie_secure), false);
                                HttpCookie cookie = new HttpCookie(name, value);
                                cookie.setPath(path);
                                cookie.setDomain(domain);
                                cookie.setMaxAge(maxAge);
                                cookie.setSecure(secure);
                                String cookiesss = name + "=" + value + ";" + "Path=" + path + ";" + "Domain=" + domain + ";maxAge=" + maxAge;
                                conn.setRequestProperty("Cookie", cookiesss);
                            }
                        }
                        conn.connect();
                        prefs.putLong(context.getResources().getString(R.string.cookie_expired_time), 0, context);
                        conn.disconnect();
                        } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }



    @Override
    public synchronized int getProgress() {
        return progressOfLogging;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
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

    public synchronized boolean getBoolean(String key, boolean defValue, Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(key, defValue);
    }


    public synchronized void putBoolean(String key, boolean value,  Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key,value);
        editor.commit();
    }


    public synchronized void putLong(String key, long value,  Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(key,value);
        editor.commit();
    }

    public synchronized void putString(String key, String value,  Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key,value);
        editor.commit();
    }


}
