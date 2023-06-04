package pl.kurs.exception;


public class WrongEntityParameterException extends RuntimeException {
    public WrongEntityParameterException(String message) {
        super(message);
    }
}
