package pl.gda.pg.eti.kask.soundmeterpg.Interfaces;

import java.net.HttpURLConnection;

/**
 * Created by Daniel on 15.08.2016 at 11:12 :).
 */
public interface AccountManager {
    boolean isLogIn();
    void logIn(String user, String password);
    void logOut();
    int getProgress();
    String getErrorMessage();
    String getDeviceName();
    void setCookies(HttpURLConnection conn);
    void storeCookies();
}