package effectivejava.chapter05.item29;

import java.io.Serial;

public class StackOverflowException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -9143380554964922527L;

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
