package info.theinside.server.error;

import info.theinside.server.error.exception.BearerValidation;
import info.theinside.server.error.exception.TokenValidation;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ErrorHandlerTest {

    ErrorHandler errorHandler = new ErrorHandler();

    @Test
    void test() {

        assertEquals(Objects.requireNonNull(
                errorHandler.handleBearerValidation(new BearerValidation("error")).getBody()).getReason(),
                "Ошибка заголовка Authorization (Bearer).");

        assertEquals(Objects.requireNonNull(
                        errorHandler.handleTokenValidation(new TokenValidation("error")).getBody()).getReason(),
                "Ошибка проверки токена.");

        assertEquals(Objects.requireNonNull(
                        errorHandler.handleNumberFormatException(new NumberFormatException("error")).getBody()).getReason(),
                "Ошибка при преобразовании строки в число.");
    }

}