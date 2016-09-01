package pl.gda.pg.eti.kask.soundmeterpg;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.NullRecordException;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.OverrangeException;

/**
 * Created by gierl on 19.07.2016.
 */
public class DataBaseHandler extends SQLiteOpenHelper {
    private Context _context;
    private static final int DATABASE_VERSION = 1;
    private static final String ID = "ID";

    public DataBaseHandler(Context ctx, String name) {

        super(ctx, name, null, DATABASE_VERSION);
        _context = ctx;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE SAMPLES(ID integer primary key autoincrement," +
                "Noise double(14,11)," +
                "Latitude double(14,11) not null," +
                "Longitude double(14,11) not null, StoredOnServer integer default 0);");
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
            contentValues.put(_context.getResources().getString(R.string.noise), probe.getAvgNoiseLevel());
            contentValues.put(_context.getResources().getString(R.string.latitude), probe.getLatitude());
            contentValues.put(_context.getResources().getString(R.string.longitude), probe.getLongitude());
            contentValues.put(_context.getResources().getString(R.string.stored), probe.getState());
            int return_value = -1;
            return_value = (int) db.insert(_context.getResources().getString(R.string.table), null, contentValues);
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
        String query = "SELECT * from " + _context.getResources().getString(R.string.table) + " WHERE ID=" + ID;
        SQLiteDatabase db = null;
        try {
            db = this.getReadableDatabase();
        } catch (Exception e) {
            android.util.Log.e("Exception", "Cannot get writableDataBase", e);
        }
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst() && cursor.getCount() != 0) {
            try {
                probe = new Probe(cursor.getDouble(1), cursor.getDouble(2), cursor.getDouble(3), cursor.getInt(4));
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
        String query = "Select * from " + _context.getResources().getString(R.string.table) + " WHERE ID=" + ID;
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                return db.delete(_context.getResources().getString(R.string.table), ID + "=" + cursor.getInt(0), null) > 0;
            } else throw new NullRecordException("Not found records that have ID = " + ID);

    }

    public boolean changeState(int ID, boolean stored) {
        int state = (stored) ? 1 : 0;
        ContentValues args = new ContentValues();
        args.put(_context.getResources().getString(R.string.stored), state);
        SQLiteDatabase db = null;
        db = this.getWritableDatabase();
        int rowAffected = db.update(_context.getResources().getString(R.string.table), args, "ID" + "=" + ID, null);
        if (rowAffected > 0) return true;
        else return false;
    }
}
