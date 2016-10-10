package pl.gda.pg.eti.kask.soundmeterpg.Exceptions;

/**
 * Created by gierl on 09.08.2016.
 */
public class NullRecordException extends RuntimeException {
    public NullRecordException() {
    }

    ;

    public NullRecordException(String message) {
        super(message);
    }
}
