package pl.gda.pg.eti.kask.soundmeterpg.Internet;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
    private CookieManager cookieManager = null;
    private ConnectionInternetDetector connectionInternetDetector ;
    private static  MyAccountManager instance = null;
    private MyAccountManager(Context context){
        this.context = context;
        cookieManager = SingletonCookieManager.getInstance();
        prefs = SynchronizedPreference.getInstance();
        connectionInternetDetector = new ConnectionInternetDetector(context);
    }

    public static MyAccountManager getInstance(Context context){
        if(instance == null)
            instance = new MyAccountManager(context);
        return instance;
    }
    @Override
    public boolean isLogIn() {
        return prefs.getBoolean(context.getResources().getString(R.string.logged_key),false,context);
    }

    @Override
    public void logIn(final String login, final String password) {
        progressOfLogging=0;
        prefs.putBoolean(context.getResources().getString(R.string.logged_key),false,context);
        createLoginThread(login, password);
        loggingThread.start();
    }


    @Override
    public void logOut() {
        createLogOutThread();;
        logOutThread.start();
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
    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    public boolean checkIfUserIsLogged(){
        createCheckUserIsLoggedThread();
        loggedThread.start();
        try {
            loggedThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return isLogIn();

    }

    @Override
    public  void setCookies(HttpURLConnection conn) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        long timeInMillis = preferences.getLong(context.getResources().getString(R.string.cookie_expired_time), 0);
        if(timeInMillis >0) {
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
    }

    @Override
    public void storeCookies() {

        Calendar calendar = Calendar.getInstance();
        List<HttpCookie> cookies = cookieManager.getCookieStore().getCookies();
        if(cookies != null && cookies.size() >0) {
            HttpCookie cookie = cookies.get(0);
            long timeInMillis = calendar.getTimeInMillis() + 1000 * cookie.getMaxAge();
            prefs.putString(context.getResources().getString(R.string.cookie_name), cookie.getName(), context);
            prefs.putString(context.getResources().getString(R.string.cookie_value), cookie.getValue(), context);
            prefs.putString(context.getResources().getString(R.string.cookie_path), cookie.getPath(), context);
            prefs.putString(context.getResources().getString(R.string.cookie_domain), cookie.getDomain(), context);
            prefs.putLong(context.getResources().getString(R.string.cookie_expired_time), timeInMillis, context);
            prefs.putBoolean(context.getResources().getString(R.string.cookie_secure), cookie.getSecure(), context);
        }
    }

    private void createLoginThread(final String login, final String password) {
        loggingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if(connectionInternetDetector.isConnectingToInternet()) {
                    HttpURLConnection conn = null;
                    URL url = null;
                    try {
                        url = new URL("https://soundmeterpg.pl/login_android");
                        conn = initializeConnection(conn, url);
                        String query = initializeArguments(login, password);
                        openConnection(conn, query);
                        conn.connect();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    getResponse(conn, login, password);
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

    private void getResponse(HttpURLConnection conn, String login, String password) {
        try {
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String result = getStringResponse(conn);
                if (result.toString().contains("success")) {
                    prefs.putString(context.getResources().getString(R.string.login_key),login,context);
                    prefs.putString(context.getResources().getString(R.string.password), password, context);
                    storeCookies();
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



    private void createCheckUserIsLoggedThread() {

        loggedThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (connectionInternetDetector.isConnectingToInternet()) {
                    try {
                         URL url = new URL("https://soundmeterpg.pl/checkSessionAndroid");
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        //Prawdopodobnie zmaknięto aplikację, trzeba sprawdzić, czy są cookiesy.
                        if (cookieManager.getCookieStore().getCookies().size() == 0) {
                            setCookies(conn);
                            conn.connect();

                            String response = getStringResponse(conn);
                            if (response.contains("you are still logged")) {
                                prefs.putBoolean(context.getResources().getString(R.string.logged_key), true, context);
                                storeCookies();
                            } else
                                prefs.putBoolean(context.getResources().getString(R.string.logged_key), false, context);
                            conn.disconnect();
                            Log.i("LoginCheck", response);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    prefs.putBoolean(context.getResources().getString(R.string.logged_key), false, context);
                }
            }
        });
    }






    private String getStringResponse(HttpURLConnection conn) throws IOException {
        InputStream input = conn.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
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

                                String cookiesss = name + "=" + value + ";" + "Path=" + path + ";" + "Domain=" + domain + ";maxAge=" + maxAge;
                                conn.setRequestProperty("Cookie", cookiesss);
                            }
                        }
                        conn.connect();
                        prefs.putLong(context.getResources().getString(R.string.cookie_expired_time), 0, context);
                        prefs.putString(context.getResources().getString(R.string.cookie_name),"",context);
                        prefs.putString(context.getResources().getString(R.string.cookie_value),"",context);
                        prefs.putString(context.getResources().getString(R.string.cookie_path),"",context);
                        prefs.putString(context.getResources().getString(R.string.cookie_domain),"",context);
                        prefs.putString(context.getResources().getString(R.string.login_key),"",context);
                        prefs.putString(context.getResources().getString(R.string.password),"",context);
                        prefs.putBoolean(context.getResources().getString(R.string.logged_key),false, context);
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




        private  String capitalize(String str) {
            if (TextUtils.isEmpty(str)) {
                return str;
            }
            char[] arr = str.toCharArray();
            boolean capitalizeNext = true;
            StringBuilder phrase = new StringBuilder();
            for (char c : arr) {
                if (capitalizeNext && Character.isLetter(c)) {
                    phrase.append(Character.toUpperCase(c));
                    capitalizeNext = false;
                    continue;
                } else if (Character.isWhitespace(c)) {
                    capitalizeNext = true;
                }
                phrase.append(c);
            }
            return phrase.toString();
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
