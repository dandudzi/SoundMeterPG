package pl.gda.pg.eti.kask.soundmeterpg.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.util.MeasureUnit;
import android.support.annotation.NonNull;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.InsufficientInternalStoragePermissionsException;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.NullRecordException;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.OverRangeException;
import pl.gda.pg.eti.kask.soundmeterpg.Fragments.Measure;
import pl.gda.pg.eti.kask.soundmeterpg.R;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Location;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Measurement;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.MeasurementStatistics;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.PreferenceParser;

/**
 * Created by gierl on 19.07.2016.
 */
public class DataBaseHandler extends SQLiteOpenHelper {
    private Context context;
    private static final int DATABASE_VERSION = 1;
    public static final String MEASUREMENT = "MEASUREMENT";
    public static final String ID = "ID";
    public static final String MIN = "Min";
    public static final String MAX = "Max";
    public static final String AVG = "AvgNoise";
    public static final String LATITUDE = "Latitude";
    public static final String LONGITUDE = "Longitude";
    public static final String DATE = "Date";
    public static final String USER_ID = "UserID";
    private static final String STORED_ON_SERVER = "StoredOnServer";
    private Cursor cursor;
    private PreferenceParser preferenceParser;

    public DataBaseHandler(Context ctx, String name) {
        super(ctx, name, null, DATABASE_VERSION);
        this.context = ctx;
        preferenceParser = new PreferenceParser(context);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE MEASUREMENT(" +
                ID + " integer primary key autoincrement," +
                MIN + " integer not null," +
                MAX + " integer not null," +
                AVG + " integer not null," +
                LATITUDE + " double(14,11) not null," +
                LONGITUDE + " double(14,11) not null," +
                DATE + "  character(19) not null," +
                USER_ID + " character(20) not null," +
                STORED_ON_SERVER + " integer default 0);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //TODO naleĹĽy odpowiednio przenieĹ›Ä‡ elementy z starszej wersji bazy do nowej. Ta metoda moĹĽe siÄ™ przydaÄ‡
    }

    public boolean insert(Measurement measurement) throws InsufficientInternalStoragePermissionsException {
        if(!preferenceParser.hasPermissionToUseInternalStorage())
            return throwException();
        if (measurement == null) return  false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = createContentValue(measurement);
            int return_value = -1;
            return_value = (int) db.insert(MEASUREMENT, null, contentValues);
            checkSpaceIntoDataBase();
            db.close();
            if (return_value <= 0) {
                return false;
            } else
            {
                Log.i("DataBaseHandler","Added row to db");
                return true;
            }
        } catch (Exception e) {
            android.util.Log.e("Exception", "Cannot get writableDataBase", e);
        }
        return false;
    }

    public void checkSpaceIntoDataBase() {
        int countItemsThatCanBeStored = preferenceParser.howManyMeasurementsInStorage();
        while(countItemsThatCanBeStored < getItemCount()){
             MeasurementDataBaseObject oldestRow = getTheOldestRow();
             erease(oldestRow.getID());
         }
    }


    public MeasurementDataBaseObject getMeasurement(int ID) throws NullRecordException {
        String query = "SELECT * from " + MEASUREMENT + " WHERE ID=" + ID;
        MeasurementDataBaseObject measurement = null;
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst() && cursor.getCount() != 0) {
            measurement = createMeasurement();
            cursor.close();
            db.close();
            return measurement;
        } else{
            cursor.close();
            db.close();
            throw new NullRecordException("Not found records with ID = " + ID);
        }
    }

    public MeasurementDataBaseObject  getLastAddedRow() throws NullRecordException {
        MeasurementDataBaseObject measurement = null;
        String query = "Select * from " + MEASUREMENT + " ORDER BY " + ID + " DESC LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            measurement = createMeasurement();
            cursor.close();
            db.close();
            return measurement;
        }
        if(measurement == null){   cursor.close();
            db.close();
            throw new NullRecordException("Cannot get object from database. Empty database?");
        }
        return  measurement;
    }

    public MeasurementDataBaseObject getTheOldestRow() throws  NullRecordException{
        MeasurementDataBaseObject measurement = null;
        String query = "Select * from " + MEASUREMENT + " ORDER BY datetime(" + DATE + ")" + " ASC LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            measurement = createMeasurement();
            cursor.close();
            db.close();
            return measurement;
        }
        if(measurement == null){   cursor.close();
            db.close();
            throw new NullRecordException("Cannot get object from database. Empty database?");
        }
        return  measurement;
    }
    public ArrayList<MeasurementDataBaseObject> getMeasurementArray(){
        ArrayList<MeasurementDataBaseObject> measurementArrayList = new ArrayList<>();
        MeasurementDataBaseObject measurement = null;
        String query = "SELECT * from " + MEASUREMENT + ";";
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst() && cursor.getCount() != 0) {
            measurement = createMeasurement();
            measurementArrayList.add(measurement);
            while (cursor.moveToNext()) {
                measurement = createMeasurement();
                measurementArrayList.add(measurement);
            }
        }
        cursor.close();
        db.close();
        return measurementArrayList;
    }
    public void changeStoreOnServerFlag(int ID, boolean sendToServer){
        int valueToChange = (sendToServer == true ? 1 : 0);
        String query = "UPDATE " + MEASUREMENT + " SET " + STORED_ON_SERVER + "=" +valueToChange + " where ID=" + ID + ";";
        ContentValues cv = new ContentValues();
        cv.put(STORED_ON_SERVER, valueToChange);
        SQLiteDatabase db = this.getWritableDatabase();

        int value = db.update(MEASUREMENT, cv, "ID="+ID,null);
        db.close();
    }

    public boolean erease(int ID) throws NullRecordException {
        String query = "Select * from " + MEASUREMENT + " WHERE ID=" + ID;
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            int returnValue = db.delete(MEASUREMENT, this.ID + "=" + cursor.getInt(0), null);
            cursor.close();
            db.close();
            return  returnValue>0;
        } else
        {
            cursor.close();
            db.close();
            throw new NullRecordException("Not found records that have ID = " + ID);
        }

    }

    private boolean throwException() throws InsufficientInternalStoragePermissionsException {
        throw new InsufficientInternalStoragePermissionsException("Cannot store object in database.\n" +
                "You don't have inssuficient permission to use internal storage");
    }
    @NonNull
    private MeasurementDataBaseObject createMeasurement() {
        MeasurementDataBaseObject measurement;
        MeasurementStatistics statistics = new MeasurementStatistics();
        statistics.min = getMin();
        statistics.max = getMax();
        statistics.avg = getAvg();
        Location location = new Location(getLatitude(), getLongitude());
        Date date = getDate();
        measurement = new MeasurementDataBaseObject(statistics, location ,getStoredOnServer(), date, getID(), getUserID());
        return measurement;
    }

    private ContentValues createContentValue(Measurement measurement) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MIN, measurement.getMin());
        contentValues.put(MAX, measurement.getMax());
        contentValues.put(AVG, measurement.getAvg());
        contentValues.put(LATITUDE, measurement.getLocation().getLatitude());
        contentValues.put(LONGITUDE, measurement.getLocation().getLongitude());
        contentValues.put(DATE, measurement.getDate());
        contentValues.put(USER_ID, "TEST");
        contentValues.put(STORED_ON_SERVER, measurement.getStoredState());
        return contentValues;
    }

    private  int getItemCount(){
        SQLiteDatabase db = this.getReadableDatabase();
        int itemCount = 0;
        itemCount = (int) DatabaseUtils.queryNumEntries(db, MEASUREMENT);
        db.close();
        return  itemCount;
    }

    private int getID(){ return  cursor.getInt(0);}

    private int getMin(){
        return cursor.getInt(1);
    }

    private int getMax(){
        return cursor.getInt(2);
    }

    private int getAvg(){
        return cursor.getInt(3);
    }

    private  double getLatitude(){
        return cursor.getDouble(4);
    }

    private  double getLongitude(){
        return cursor.getDouble(5);
    }

    private Date getDate() {
        String dateString = cursor.getString(6);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date =  format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private String getUserID(){
        return cursor.getString(7);
    }

    private boolean getStoredOnServer(){
        int storedOnWebServer = cursor.getInt(8);
        return (storedOnWebServer ==0 ? false : true);
    }
}
