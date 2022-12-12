package info.theinside.token.error;

import info.theinside.token.error.exception.AuthException;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ErrorHandlerTest {

    ErrorHandler errorHandler = new ErrorHandler();

    @Test
    void test() {

        assertEquals(Objects.requireNonNull(
                        errorHandler.handleAuthException(new AuthException("error")).getBody()).getReason(),
                "Ошибка авторизации.");
    }
}