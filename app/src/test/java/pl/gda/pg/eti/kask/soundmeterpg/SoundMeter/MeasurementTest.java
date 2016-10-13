package pl.gda.pg.eti.kask.soundmeterpg.SoundMeter;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;


/**
 * Created by Daniel on 13.10.2016 at 13:00 :).
 */

public class MeasurementTest {

    @Test
    public void calculateMeasureStatistics() throws Exception {
        MeasurementStatistics statistics =  new MeasurementStatistics();
        statistics.min = 12;
        statistics.max = 44;
        statistics.avg = 36;

        List<Sample> list =  new ArrayList<>();

        MeasurementStatistics emptyTest = Measurement.calculateMeasureStatistics(list);
        assertTrue("Min empty test", emptyTest.min == 140);
        assertTrue("Max empty test", emptyTest.max == 0);
        assertTrue("Avg empty test", emptyTest.avg == 0);

        FakeLocation location =  new FakeLocation();
        int[] noiseLevels = { 12,44,0,34,15,23,33,0};
        for (int nl:noiseLevels) {
            list.add(new Sample(nl,location));
        }
        MeasurementStatistics output = Measurement.calculateMeasureStatistics(list);

        assertTrue("Min test", output.min == statistics.min);
        assertTrue("Max test", output.max == statistics.max);
        assertTrue("Avg test", output.avg == statistics.avg);
    }



}