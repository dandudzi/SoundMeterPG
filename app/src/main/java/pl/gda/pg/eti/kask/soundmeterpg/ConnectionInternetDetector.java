package pl.gda.pg.eti.kask.soundmeterpg;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Daniel on 18.07.2016 :).
 */
public class ConnectionInternetDetector {

    private Context _context;

    public ConnectionInternetDetector(Context context){
        this._context = context;
    }

    public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
            if(networkInfo!=null && networkInfo.isConnected())
                return true;
        }
        return false;
    }
}