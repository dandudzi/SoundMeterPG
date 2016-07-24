package pl.gda.pg.eti.kask.soundmeterpg;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Daniel on 18.07.2016 :).
 */
class ConnectionInternetDetector implements InternetManager {

    private final Context context;

    public ConnectionInternetDetector(Context context){
        this.context = context;
    }

    @Override
    public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
            if(networkInfo!=null && networkInfo.isConnected())
                return true;
        }
        return false;
    }
}