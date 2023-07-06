package sogeti.carleaseleaseapi.exceptionhandling;

public class DuplicateStartDateException extends RuntimeException {
    public DuplicateStartDateException() {
        super();
    }
    public DuplicateStartDateException(String message) {
        super(message);
    }

    public DuplicateStartDateException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateStartDateException(Throwable cause) {
        super(cause);
    }
}
