package pl.gda.pg.eti.kask.soundmeterpg;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import pl.gda.pg.eti.kask.soundmeterpg.Activities.MainActivity;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import pl.gda.pg.eti.kask.soundmeterpg.Database.DataBaseHandler;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.NullRecordException;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.OverRangeException;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Measurement;

/**
 * Created by gierl on 04.08.2016.
 */
@RunWith(AndroidJUnit4.class)
public class DataBaseTest {
    private static DataBaseHandler db;
    private static Map<Integer, Measurement> probeMap = new HashMap<Integer, Measurement>();
    private static final int LIMIT_PROBE = 10;
    @ClassRule
    public static IntentsTestRule<MainActivity> mActivityRule = new IntentsTestRule<>(MainActivity.class);;

    @BeforeClass
    public static void setUp() {
        mActivityRule.getActivity().getBaseContext().deleteDatabase(mActivityRule.getActivity().getApplicationContext().getResources().getString(R.string.database_test));
        Random rand = new Random();
        for (int i = 0; i < LIMIT_PROBE; i++) {
           /* try {
                probeMap.put(i, new Measurement(Measurement.MIN_NOISE_LEVEL + (Measurement.MAX_NOISE_LEVEL - Measurement.MIN_NOISE_LEVEL) * rand.nextDouble(),
                        Measurement.MIN_LATITUDE + (Measurement.MAX_LATITUDE - Measurement.MIN_LATITUDE) * rand.nextDouble(),
                        Measurement.MIN_LONGITUDE + (Measurement.MAX_LONGITUDE - Measurement.MIN_LONGITUDE) * rand.nextDouble(), 0));
            } catch (OverRangeException e) {
                e.printStackTrace();
            }*/
            Measurement pr = probeMap.get(i);
        }
    }

    @Before
    public void tearUp() throws NullRecordException {
        db = new DataBaseHandler(mActivityRule.getActivity().getBaseContext(), mActivityRule.getActivity().getApplicationContext().getResources().getString(R.string.database_test));
        db.getWritableDatabase();
        for (int i = 0; i < LIMIT_PROBE; i++) {
            db.insert(probeMap.get((Integer) i));
        }
    }

    @After
    public void tearDown() {
        mActivityRule.getActivity().getBaseContext().deleteDatabase(mActivityRule.getActivity().getApplicationContext().getResources().getString(R.string.database_test));
    }

    @Test
    public void checkIfDataBaseExist() {
        mActivityRule.getActivity().getBaseContext().deleteDatabase(mActivityRule.getActivity().getApplicationContext().getResources().getString(R.string.database_test));
        File dbPath = mActivityRule.getActivity().getBaseContext().getDatabasePath(mActivityRule.getActivity().getApplicationContext().getResources().getString(R.string.database_test));
        Assert.assertFalse(dbPath.exists());
        db = new DataBaseHandler(mActivityRule.getActivity().getBaseContext(), mActivityRule.getActivity().getApplicationContext().getResources().getString(R.string.database_test));
        db.getReadableDatabase();
        dbPath = mActivityRule.getActivity().getBaseContext().getDatabasePath(mActivityRule.getActivity().getApplicationContext().getResources().getString(R.string.database_test));
        Assert.assertTrue(dbPath.exists());
    }

    @Test
    public void insertTest() {
        for (int i = 0; i < LIMIT_PROBE; i++) {
            try {
                Assert.assertTrue(db.insert(probeMap.get((Integer) i)));
            } catch (NullRecordException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void ereaseTest() {
        try {
            for (int i = LIMIT_PROBE; i >= 1; i--) {
                Assert.assertTrue(db.erease(i));
            }
        } catch (NullRecordException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getProbeByIDTest() throws NullRecordException {
        for (int i = 0; i < LIMIT_PROBE; i++) {
            Measurement pr = db.getProbeByID(i + 1);//+1 ze względu że w bazie rekordy są od pierwszego rekordu a nie od zerowego
            Assert.assertEquals(probeMap.get(i).getLatitude(), pr.getLatitude(), 0.0);
            Assert.assertEquals(probeMap.get(i).getLongitude(), pr.getLongitude(), 0.0);
            Assert.assertEquals(probeMap.get(i).getAvgNoiseLevel(), pr.getAvgNoiseLevel(), 0.0);
            Assert.assertFalse(probeMap.get(i).getState());
        }
    }

    @Test
    /* Rzucanie wyjątku podczas próby pobrania rekordu, który nie istnieje. */
    public void throwingExceptionTest() {
        Throwable e = null;
        try {
            Measurement pr = db.getProbeByID(LIMIT_PROBE + 1);
        } catch (Throwable ex) {
            e = ex;
        }
        Assert.assertTrue(e instanceof NullRecordException);
    }

    /* Próba usunięcia rekordu, który nie istnieje */
    @Test
    public void ereaseNullProbe() {
        Throwable e = null;
        try {
            db.erease(LIMIT_PROBE + 1);
        } catch (Throwable ex) {
            e = ex;
        }
        Assert.assertTrue(e instanceof NullRecordException);
    }

    @Test
    public void insertNullProbe() {
        Throwable e = null;
        Measurement measurement = null;
        try {
            db.insert(measurement);
        } catch (Throwable ex) {
            e = ex;
        }
        Assert.assertTrue(e instanceof NullRecordException);
    }

    @Test
    public void insertOverageProbe() {
        Measurement pr = null;
        /*try {
            pr = new Measurement(Measurement.MAX_NOISE_LEVEL + 1, Measurement.MAX_LATITUDE + 2, Measurement.MAX_LONGITUDE + 22, 0);
            db.insert(pr);
            Assert.fail("Test should fail, should be exception");
        } catch (OverRangeException e) {
            e.printStackTrace();
        } catch (NullRecordException e) {
            e.printStackTrace();
        }*/
    }

    @Test
    public void isStoredValueOnServer() {
        db.changeState(LIMIT_PROBE - 2 + 1, true);
        db.changeState(LIMIT_PROBE - 4 + 1, true);
        db.changeState(LIMIT_PROBE + LIMIT_PROBE * 2 + 1, true);
        try {
            Assert.assertTrue(db.getProbeByID(LIMIT_PROBE - 2 + 1).getState());
            Assert.assertTrue(db.getProbeByID(LIMIT_PROBE - 4 + 1).getState());
            Assert.assertFalse(db.getProbeByID(LIMIT_PROBE + LIMIT_PROBE * 2 + 1).getState());
        } catch (NullRecordException e) {
            e.printStackTrace();
        }
    }


}
