package pl.gda.pg.eti.kask.soundmeterpg.Internet;


import java.net.CookieHandler;
import java.net.CookieManager;

/**
 * Created by gierl on 18.11.2016.
 */

public class SingletonCookieManager {

        private static class SingletonHolder {
            private static final CookieManager instance = new CookieManager();

        }
        public static CookieManager getInstance(){
            CookieHandler.setDefault(SingletonHolder.instance);
            return SingletonHolder.instance;
        }
}
