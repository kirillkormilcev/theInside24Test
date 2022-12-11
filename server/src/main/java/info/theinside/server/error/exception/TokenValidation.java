package info.theinside.server.error.exception;

public class TokenValidation extends RuntimeException {
    public TokenValidation(String message) {
        super(message);
    }
}
