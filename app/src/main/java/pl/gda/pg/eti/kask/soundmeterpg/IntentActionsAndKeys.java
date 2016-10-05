package pl.gda.pg.eti.kask.soundmeterpg;

/**
 * Created by Daniel on 05.10.2016.
 */

public enum IntentActionsAndKeys {
    SAMPLE_RECEIVE_ACTION("SAMPLE_RECEIVE_ACTION"),
    SAMPLE_KEY ("SAMPLE_KEY"),
    END_ACTION ("END_ACTION"),
    ERROR_MEASURE_ACTION("ERROR_MEASURE_ACTION"),
    ERROR_KEY("ERROR_KEY"),
    GPS_ERROR_KEY("GPS_ERROR_KEY");

    private final String text;
    private IntentActionsAndKeys(String text){
        this.text = text;
    }


    @Override
    public String toString() {
        return text;
    }
}
