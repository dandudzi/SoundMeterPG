package pl.gda.pg.eti.kask.soundmeterpg;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import pl.gda.pg.eti.kask.soundmeterpg.Exception.NullRecordException;
import pl.gda.pg.eti.kask.soundmeterpg.Exception.OverrangeException;

/**
 * Created by gierl on 19.07.2016.
 */
public class DataBaseHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "noiseDataBase.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE = "PROBE";
    private static final String NOISE = "Noise";
    private static final String LATITUDE = "Latitude";
    private static final String LONGITUDE = "Longitude";
    private static final String ID = "ID";
    public DataBaseHandler(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE PROBE(ID integer primary key autoincrement," +
                "Noise double(14,11)," +
                "Latitude double(14,11) not null," +
                "Longitude double(14,11) not null);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //TODO naleĹĽy odpowiednio przenieĹ›Ä‡ elementy z starszej wersji bazy do nowej. Ta metoda moĹĽe siÄ™ przydaÄ‡
    }

    public boolean insert(Probe probe) throws NullRecordException {
        if (probe == null) throw new NullRecordException();
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(NOISE, probe.getAvgNoiseLevel());
            contentValues.put(LATITUDE, probe.getLatitude());
            contentValues.put(LONGITUDE, probe.getLongitude());
            int return_value = -1;
            return_value = (int) db.insert(TABLE, null, contentValues);
            db.close();
            if (return_value <= 0) {
                return false;
            } else return true;
        } catch (Exception e) {
            android.util.Log.e("Exception", "Cannot get writableDataBase", e);
        }
        return false;
    }

    public Probe getProbeByID(int ID) throws NullRecordException {
        Probe probe = null;
        String query = "SELECT * from " + TABLE + " WHERE ID=" + ID;
        SQLiteDatabase db = null;
        try {
            db = this.getReadableDatabase();
        } catch (Exception e) {
            android.util.Log.e("Exception", "Cannot get writableDataBase", e);
        }
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst() && cursor.getCount() != 0) {
            try {
                probe = new Probe(cursor.getDouble(1), cursor.getDouble(2), cursor.getDouble(3));
            } catch (OverrangeException e) {
                e.printStackTrace();
            }
            return probe;
        } else throw new NullRecordException("Not found records that have ID = " + ID);


    }

    /*  private Probe getProbeFromDB() {
          Probe probe = null;
          String query = "Select * from " + TABLE + " ORDER BY " + ID + " ASC LIMIT 1";

          try {
              SQLiteDatabase db = this.getReadableDatabase();
              Cursor cursor = db.rawQuery(query, null);
              if (cursor.moveToFirst()) {
                  probe = new Probe(cursor.getDouble(1), cursor.getDouble(2), cursor.getDouble(3));
                  return probe;
              }
          } catch (Exception e) {
              android.util.Log.e("Exception", "Cannot get writableDataBase", e);
          }
          return probe;
      }
  */
    public boolean erease(int ID) throws NullRecordException {
        String query = "Select * from " + TABLE + " WHERE ID=" + ID;
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                return db.delete(TABLE, ID + "=" + cursor.getInt(0), null) > 0;
            } else throw new NullRecordException("Not found records that have ID = " + ID);

    }
}
