package info.theinside.server.controller;

import info.theinside.server.dto.MessageRequest;
import info.theinside.server.service.ServerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/message")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServerController {
    final ServerService serverService;

    @PostMapping
    public ResponseEntity<Object> addMessage(
            @Validated @RequestBody MessageRequest messageRequest,
            @RequestHeader("Authorization") String bearerToken) {
        log.info("Обработка эндпойнта POST/message/.(body: messageRequest, Header: Authorization (bearerToken))");
        return new ResponseEntity<>(serverService.addMessage(messageRequest, bearerToken), HttpStatus.OK);
    }
}
