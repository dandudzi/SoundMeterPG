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
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import pl.gda.pg.eti.kask.soundmeterpg.Exception.NullRecordException;
import pl.gda.pg.eti.kask.soundmeterpg.Exception.OverrangeException;

import static junit.framework.Assert.fail;

/**
 * Created by gierl on 04.08.2016.
 */
@RunWith(AndroidJUnit4.class)
public class DataBaseTest {
    private static DataBaseHandler _db;
    private static Map<Integer, Probe> _probeMap = new HashMap<Integer, Probe>();
    private static final int LIMIT_PROBE = 10;
    private static final double MIN = 1.0001;
    private static final double MAX = 150.23;
    @ClassRule
    public static IntentsTestRule<MainActivity> mActivityRule = new IntentsTestRule<>(MainActivity.class);
    public final ExpectedException exception = ExpectedException.none();

    @BeforeClass
    public static void setUp() {
        mActivityRule.getActivity().getBaseContext().deleteDatabase("noiseDataBase.db");
        Random rand = new Random();
        for (int i = 0; i < LIMIT_PROBE; i++) {
            try {
                _probeMap.put(i, new Probe(Probe.MIN_NOISE_LEVEL + (Probe.MAX_NOISE_LEVEL - Probe.MIN_NOISE_LEVEL) * rand.nextDouble(),
                        Probe.MIN_LATITUDE + (Probe.MAX_LATITUDE - Probe.MIN_LATITUDE) * rand.nextDouble(),
                        Probe.MIN_LONGITUDE + (Probe.MAX_LONGITUDE - Probe.MIN_LONGITUDE) * rand.nextDouble()));
            } catch (OverrangeException e) {
                e.printStackTrace();
            }
            Probe pr = _probeMap.get(i);
        }
    }

    @Before
    public void tearUp() throws NullRecordException {
        _db = new DataBaseHandler(mActivityRule.getActivity().getBaseContext());
        _db.getWritableDatabase();
        for (int i = 0; i < LIMIT_PROBE; i++) {
            _db.insert(_probeMap.get((Integer) i));
        }
    }

    @After
    public void tearDown() {
        mActivityRule.getActivity().getBaseContext().deleteDatabase("noiseDataBase.db");
    }

    @Test
    public void checkIfDataBaseExist() {
        mActivityRule.getActivity().getBaseContext().deleteDatabase("noiseDataBase.db");
        File dbPath = mActivityRule.getActivity().getBaseContext().getDatabasePath("noiseDataBase.db");
        Assert.assertFalse(dbPath.exists());
        _db = new DataBaseHandler(mActivityRule.getActivity().getBaseContext());
        _db.getReadableDatabase();
        dbPath = mActivityRule.getActivity().getBaseContext().getDatabasePath("noiseDataBase.db");
        Assert.assertTrue(dbPath.exists());
    }

    @Test
    public void insertTest() {
        for (int i = 0; i < LIMIT_PROBE; i++) {
            try {
                Assert.assertTrue(_db.insert(_probeMap.get((Integer) i)));
            } catch (NullRecordException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void ereaseTest() {
        try {
            for (int i = LIMIT_PROBE; i >= 1; i--) {
                Assert.assertTrue(_db.erease(i));
            }
        } catch (NullRecordException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getProbeByIDTest() throws NullRecordException {
        for (int i = 0; i < LIMIT_PROBE; i++) {
            Probe pr = _db.getProbeByID(i + 1);//+1 ze względu że w bazie rekordy są od pierwszego rekordu a nie od zerowego
            Assert.assertEquals(_probeMap.get(i).getLatitude(), pr.getLatitude(), 0.0);
            Assert.assertEquals(_probeMap.get(i).getLongitude(), pr.getLongitude(), 0.0);
            Assert.assertEquals(_probeMap.get(i).getAvgNoiseLevel(), pr.getAvgNoiseLevel(), 0.0);
        }
    }

    @Test
    /* Rzucanie wyjątku podczas próby pobrania rekordu, który nie istnieje. */
    public void throwingExceptionTest() {
        Throwable e = null;
        try {
            Probe pr = _db.getProbeByID(LIMIT_PROBE + 1);
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
            _db.erease(LIMIT_PROBE + 1);
        } catch (Throwable ex) {
            e = ex;
        }
        Assert.assertTrue(e instanceof NullRecordException);
    }

    @Test
    public void insertNullProbe() {
        Throwable e = null;
        Probe probe = null;
        try {
            _db.insert(probe);
        } catch (Throwable ex) {
            e = ex;
        }
        Assert.assertTrue(e instanceof NullRecordException);
    }

    @Test
    public void insertOverageProbe() {
        Probe pr = null;
        try {
            pr = new Probe(Probe.MAX_NOISE_LEVEL + 1, Probe.MAX_LATITUDE + 2, Probe.MAX_LONGITUDE + 22);
            _db.insert(pr);
            Assert.fail("Test should fail, should be exception");
        } catch (OverrangeException e) {
            e.printStackTrace();
        } catch (NullRecordException e) {
            e.printStackTrace();
        }
    }


}
