package info.theinside.token.error;

import info.theinside.token.error.dto.ErrorResponse;
import info.theinside.token.error.exception.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleAuthException(final AuthException e) {
        return new ResponseEntity<>(new ErrorResponse(logAndGetErrorsFromStackTrace(e), e.getLocalizedMessage(),
                "Ошибка авторизации.", HttpStatus.NOT_FOUND, LocalDateTime.now()),
                HttpStatus.NOT_FOUND);
    }

    private List<String> logAndGetErrorsFromStackTrace(Exception e) {
        log.warn(e.getMessage(), e.getCause());
        e.printStackTrace(); //todo: remove after debug
        return Arrays.stream(ExceptionUtils.getRootCauseStackTrace(e)).filter(f -> f.contains("info.theinside"))
                .map((string) -> {
                    if (string.contains("\t")) {
                        string = string.substring(1);
                    }
                    return string;
                })
                .collect(Collectors.toList());
    }
}
