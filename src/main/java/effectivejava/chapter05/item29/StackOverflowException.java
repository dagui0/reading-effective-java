package effectivejava.chapter05.item29;

public class StackOverflowException extends RuntimeException {
    public StackOverflowException(Throwable cause) {
        super(cause);
    }

    public StackOverflowException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public StackOverflowException(String message) {
        super(message);
    }

    public StackOverflowException(String message, Throwable cause) {
        super(message, cause);
    }

    public StackOverflowException() {
    }
}
