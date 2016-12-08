package pl.gda.pg.eti.kask.soundmeterpg.DataBase;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import pl.gda.pg.eti.kask.soundmeterpg.Database.DataBaseHandler;
import pl.gda.pg.eti.kask.soundmeterpg.Database.MeasurementDataBaseObject;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.InsufficientInternalStoragePermissionsException;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.NullRecordException;
import pl.gda.pg.eti.kask.soundmeterpg.PreferenceTestHelper;
import pl.gda.pg.eti.kask.soundmeterpg.R;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Location;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Measurement;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.MeasurementStatistics;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.PreferenceParser;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Sample;

/**
 * Created by gierl on 09.10.2016.
 */

public class DataBaseHandlerTest {
    private static final int LIMIT_PROBE = 10;
    private DataBaseHandler dataBaseHandler;
    private static Context context = InstrumentationRegistry.getTargetContext();
    static Random random = new Random();
    private static Map<Integer, Measurement> measurementMap = new HashMap<Integer, Measurement>();
    private static SharedPreferences sharedPreferences;

    @BeforeClass
    public static void setUp() {
        context.deleteDatabase(context.getResources().getString(R.string.database_test));
        sharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        for (int i = 0; i < LIMIT_PROBE; i++) {
            MeasurementStatistics statistics = createFakeMeasurementStatistics();
            Location location = createFakeLocation();
            Measurement measurement = new Measurement(statistics, location, (i % 2 == 0 ? true : false), new Date(),1000);
            measurementMap.put(i, measurement);
        }
    }

    @Before
    public void initializeDataBase () throws InsufficientInternalStoragePermissionsException {
        PreferenceTestHelper.setPrivilages(context.getResources().getString(R.string.internal_storage_key_preference),sharedPreferences,true);
        dataBaseHandler = new DataBaseHandler(context, context.getResources().getString(R.string.database_name));
        for (int i = 0; i < LIMIT_PROBE; i++) {
            dataBaseHandler.insert(measurementMap.get( i));
        }

    }
    @After
    public void clearDataBase() {
       context.deleteDatabase(context.getResources().getString(R.string.database_name));
    }

    @Test
    public void checkIfDataBaseExist() {
        context.deleteDatabase(context.getResources().getString(R.string.database_name));
        File dbPath = context.getDatabasePath(context.getResources().getString(R.string.database_name));
        Assert.assertFalse(dbPath.exists());
        dataBaseHandler = new DataBaseHandler(context, context.getResources().getString(R.string.database_name));
        dataBaseHandler.getReadableDatabase();
        dbPath = context.getDatabasePath(context.getResources().getString(R.string.database_name));
        Assert.assertTrue(dbPath.exists());
    }

    @Test
    public void insertTest() throws InsufficientInternalStoragePermissionsException {
        for (int i = 0; i < LIMIT_PROBE; i++) {
            Assert.assertTrue(dataBaseHandler.insert(measurementMap.get( i)));
        }
    }

    @Test
    public void getProbeByIDTest() throws NullRecordException {
        for (int i = 0; i < LIMIT_PROBE; i++) {
            Measurement measurement = dataBaseHandler.getMeasurement(i + 1);//+1 ze względu że w bazie rekordy są od pierwszego rekordu a nie od zerowego
            veryfyRecords(i, measurement);
        }
    }

    @Test(expected = NullRecordException.class)
    public void getProbeByIDExceptionTest(){
        Measurement measurement = dataBaseHandler.getMeasurement(LIMIT_PROBE  * 2);
    }

    @Test
    public void ereaseTest(){
        for(int i = 0; i < LIMIT_PROBE; i++){
            Assert.assertTrue(dataBaseHandler.erease(i+1));
        }
    }

    @Test(expected = NullRecordException.class)
    public void ereaseExceptionTest(){
        dataBaseHandler.erease(LIMIT_PROBE * 2);
    }

    @Test
    public void getAllMeasurementFromDataBaseTest(){
       ArrayList<MeasurementDataBaseObject> arrayMeasurement  = dataBaseHandler.getMeasurementArray();
        for(int i = 0;i < LIMIT_PROBE; i++){
            veryfyRecords(i,arrayMeasurement.get(i));
        }
    }
    @Test
    public void getLastAddedRowTest(){
        veryfyRecords(LIMIT_PROBE-1,dataBaseHandler.getLastAddedRow());
    }

    @Test(expected = InsufficientInternalStoragePermissionsException.class)
    public void inssuficientExceptionTest() throws InsufficientInternalStoragePermissionsException {
        PreferenceTestHelper.setPrivilages(context.getResources().getString(R.string.internal_storage_key_preference),sharedPreferences,false);
        dataBaseHandler.insert(measurementMap.get(0));
    }



    private static MeasurementStatistics createFakeMeasurementStatistics() {
        MeasurementStatistics statistics = new MeasurementStatistics();
        statistics.min = random.nextInt(Sample.MAX_NOISE_LEVEL - Sample.MIN_NOISE_LEVEL) + Sample.MIN_NOISE_LEVEL;
        statistics.max = random.nextInt(Sample.MAX_NOISE_LEVEL - Sample.MIN_NOISE_LEVEL) + Sample.MIN_NOISE_LEVEL;
        statistics.avg = random.nextInt(Sample.MAX_NOISE_LEVEL - Sample.MIN_NOISE_LEVEL) + Sample.MIN_NOISE_LEVEL;
        return  statistics;
    }

    private static Location createFakeLocation() {

        Location location = new Location(Location.MIN_LATITUDE + (Location.MAX_LATITUDE - Location.MIN_LATITUDE) * random.nextDouble(),
                Location.MIN_LONGITUDE + (Location.MAX_LONGITUDE - Location.MIN_LONGITUDE) * random.nextDouble());
        return  location;
    }

    private void veryfyRecords(int i, Measurement measurement) {
        Assert.assertEquals(measurementMap.get(i).getMin(), measurement.getMin());
        Assert.assertEquals(measurementMap.get(i).getMax(), measurement.getMax());
        Assert.assertEquals(measurementMap.get(i).getAvg(), measurement.getAvg());
        Assert.assertEquals(measurementMap.get(i).getLocation(), measurement.getLocation());
        Assert.assertEquals(measurementMap.get(i).getStoredState(), measurement.getStoredState());
        Assert.assertEquals(measurementMap.get(i).getDate(), measurement.getDate());
    }
}
