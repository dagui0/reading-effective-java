package effectivejava.chapter07.item43;

public class TimoutException extends Exception {
    public TimoutException() {
    }

    public TimoutException(String message) {
        super(message);
    }

    public TimoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public TimoutException(Throwable cause) {
        super(cause);
    }

    public TimoutException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
