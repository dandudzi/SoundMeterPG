package pl.gda.pg.eti.kask.soundmeterpg.Interfaces;

/**
 * Created by gierl on 25.08.2016.
 */
public interface PreferenceManager {
    boolean hasPermissionToUseInternet();

    boolean hasPermissionToUseGPS();

    boolean hasPermissionToUseInternalStorage();

    boolean hasPermissionToUseMicrophone();

    boolean hasPermissionToWorkInBackground();

    boolean hasPermissionToSendToServer();

    int howManyMeasurementsInStorage();
}
