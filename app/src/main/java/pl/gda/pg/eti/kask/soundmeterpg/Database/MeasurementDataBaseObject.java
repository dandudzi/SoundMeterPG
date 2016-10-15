package pl.gda.pg.eti.kask.soundmeterpg.Database;

import java.util.Date;

import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Location;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Measurement;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.MeasurementStatistics;

/**
 * Created by gierl on 15.10.2016.
 */

public class MeasurementDataBaseObject extends Measurement {
    private int ID;
    private String UserID;
    public MeasurementDataBaseObject(MeasurementStatistics statistics, Location location, boolean storedOnWebServer, Date date, int ID, String UserID){
        super(statistics, location, storedOnWebServer, date);
        this.UserID = UserID;
        this.ID = ID;
    }
    public int getID(){
        return ID;
    }
    public String getUserID(){
        return UserID;
    }

}
