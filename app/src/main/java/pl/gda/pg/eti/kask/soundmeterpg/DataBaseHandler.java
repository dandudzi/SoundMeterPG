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
    private Context context;
    private static final int DATABASE_VERSION = 1;
    private static final String ID = "ID";

    public DataBaseHandler(Context ctx, String name) {

        super(ctx, name, null, DATABASE_VERSION);
        context = ctx;
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

    public boolean insert(Sample sample) throws NullRecordException {
        if (sample == null) throw new NullRecordException();
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(context.getResources().getString(R.string.noise), sample.getAvgNoiseLevel());
            contentValues.put(context.getResources().getString(R.string.latitude), sample.getLatitude());
            contentValues.put(context.getResources().getString(R.string.longitude), sample.getLongitude());
            contentValues.put(context.getResources().getString(R.string.stored), sample.getState());
            int return_value = -1;
            return_value = (int) db.insert(context.getResources().getString(R.string.table), null, contentValues);
            db.close();
            if (return_value <= 0) {
                return false;
            } else return true;
        } catch (Exception e) {
            android.util.Log.e("Exception", "Cannot get writableDataBase", e);
        }
        return false;
    }

    public Sample getProbeByID(int ID) throws NullRecordException {
        Sample sample = null;
        String query = "SELECT * from " + context.getResources().getString(R.string.table) + " WHERE ID=" + ID;
        SQLiteDatabase db = null;
        try {
            db = this.getReadableDatabase();
        } catch (Exception e) {
            android.util.Log.e("Exception", "Cannot get writableDataBase", e);
        }
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst() && cursor.getCount() != 0) {
            try {
                sample = new Sample(cursor.getDouble(1), cursor.getDouble(2), cursor.getDouble(3), cursor.getInt(4));
            } catch (OverrangeException e) {
                e.printStackTrace();
            }
            cursor.close();
            db.close();
            return sample;
        } else throw new NullRecordException("Not found records that have ID = " + ID);


    }

      public Sample getProbeFromDB() throws NullRecordException {
          Sample sample = null;
          String query = "Select * from " + context.getResources().getString(R.string.table) + " ORDER BY " + ID + " ASC LIMIT 1";

          try {
              SQLiteDatabase db = this.getReadableDatabase();

              Cursor cursor = db.rawQuery(query, null);
              if (cursor.moveToFirst()) {
                  sample = new Sample(cursor.getDouble(1), cursor.getDouble(2), cursor.getDouble(3),cursor.getInt(4));
                  cursor.close();
                  db.close();
                  return sample;
              }
              db.close();
          } catch (Exception e) {
              android.util.Log.e("Exception", "Cannot get writableDataBase", e);
          }
          if(sample == null)
              throw new NullRecordException("Cannot get object from database. Empty database?");

          return sample;
      }

    public boolean erease(int ID) throws NullRecordException {
        String query = "Select * from " + context.getResources().getString(R.string.table) + " WHERE ID=" + ID;
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                int returnValue = db.delete(context.getResources().getString(R.string.table), ID + "=" + cursor.getInt(0), null);
                cursor.close();
                db.close();
                return  returnValue>0;

            } else throw new NullRecordException("Not found records that have ID = " + ID);

    }

    public boolean changeState(int ID, boolean stored) {
        int state = (stored) ? 1 : 0;
        ContentValues args = new ContentValues();
        args.put(context.getResources().getString(R.string.stored), state);
        SQLiteDatabase db = null;
        db = this.getWritableDatabase();
        int rowAffected = db.update(context.getResources().getString(R.string.table), args, "ID" + "=" + ID, null);
        db.close();
        if (rowAffected > 0) return true;
        else return false;
    }
}
