package info.theinside.server.error;

import feign.FeignException;
import info.theinside.server.error.dto.ErrorResponse;
import info.theinside.server.error.exception.BearerValidation;
import info.theinside.server.error.exception.TokenValidation;
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
    public ResponseEntity<ErrorResponse> handleBearerValidation(final BearerValidation e) {
        return new ResponseEntity<>(new ErrorResponse(logAndGetErrorsFromStackTrace(e), e.getLocalizedMessage(),
                "Ошибка заголовка Authorization (Bearer).", HttpStatus.BAD_REQUEST, LocalDateTime.now()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleTokenValidation(final TokenValidation e) {
        return new ResponseEntity<>(new ErrorResponse(logAndGetErrorsFromStackTrace(e), e.getLocalizedMessage(),
                "Ошибка проверки токена.", HttpStatus.BAD_REQUEST, LocalDateTime.now()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleFeignException(final FeignException e) {
        return new ResponseEntity<>(new ErrorResponse(logAndGetErrorsFromStackTrace(e), e.getLocalizedMessage(),
                "Ошибка отправки запроса FeignClient.", HttpStatus.BAD_REQUEST, LocalDateTime.now()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleNumberFormatException(final NumberFormatException e) {
        return new ResponseEntity<>(new ErrorResponse(logAndGetErrorsFromStackTrace(e), e.getLocalizedMessage(),
                "Ошибка при преобразовании строки в число.", HttpStatus.BAD_REQUEST, LocalDateTime.now()),
                HttpStatus.BAD_REQUEST);
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
