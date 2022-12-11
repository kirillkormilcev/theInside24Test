package info.theinside.server.error.exception;

public class BearerValidation extends RuntimeException {
    public BearerValidation(String message) {
        super(message);
    }
}
