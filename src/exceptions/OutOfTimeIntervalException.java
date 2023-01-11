package exceptions;

public class OutOfTimeIntervalException extends RuntimeException {
    public OutOfTimeIntervalException(String message) {
        super(message);
    }
}
