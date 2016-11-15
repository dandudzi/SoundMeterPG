package pl.gda.pg.eti.kask.soundmeterpg.Internet;

import pl.gda.pg.eti.kask.soundmeterpg.Interfaces.AccountManager;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.PreferenceParser;

/**
 * Created by gierl on 14.11.2016.
 */

public class LoginManager implements AccountManager {
    private PreferenceParser preferenceParser;
    @Override
    public boolean isLogIn() {
        return false;
    }

    @Override
    public void logIn(String user, String password, String mac) {

    }

    @Override
    public void logOut() {

    }

    @Override
    public int getProgress() {
        return 0;
    }

    @Override
    public void stopLogInProcess() {

    }

    @Override
    public String getErrorMessage() {
        return null;
    }
}
