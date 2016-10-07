package pl.gda.pg.eti.kask.soundmeterpg.SoundMeter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import pl.gda.pg.eti.kask.soundmeterpg.Interfaces.InternetManager;

/**
 * Created by Daniel
 */
public class ConnectionInternetDetector implements InternetManager {

    private final Context context;

    public ConnectionInternetDetector(Context context) {
        this.context = context;
    }

    @Override
    public boolean isConnectingToInternet() {
        boolean hasInternet =  false;
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected())
                hasInternet = true;
        }

        return  hasInternet;
    }
}