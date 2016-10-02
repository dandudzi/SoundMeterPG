package pl.gda.pg.eti.kask.soundmeterpg.Exceptions;

/**
 * Created by gierl on 10.08.2016.
 */
public class OverRangeException extends RuntimeException {
    public OverRangeException() {
    }

    public OverRangeException(String message) {
        super(message);
    }
}
