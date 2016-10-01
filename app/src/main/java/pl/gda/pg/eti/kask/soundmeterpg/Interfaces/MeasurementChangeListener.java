package pl.gda.pg.eti.kask.soundmeterpg.Interfaces;

import pl.gda.pg.eti.kask.soundmeterpg.Sample;

/**
 * Created by Daniel on 30.09.2016.
 */

public interface MeasurementChangeListener {
    void onMeasurementChange(Sample newSample);
}
