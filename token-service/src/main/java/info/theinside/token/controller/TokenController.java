package info.theinside.token.controller;

import info.theinside.token.dto.JwtRequest;
import info.theinside.token.dto.JwtResponse;
import info.theinside.token.dto.TokenResponse;
import info.theinside.token.service.TokenService;
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
@RequestMapping(path = "/authenticate")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenController {
    final TokenService tokenService;

    @PostMapping
    public ResponseEntity<JwtResponse> getTokenByAuth(
            @Validated @RequestBody JwtRequest jwtRequest) {
        log.info("Обработка эндпойнта POST/tokens/.(body: jwtRequest)");
        return new ResponseEntity<>(tokenService.getTokenByAuth(jwtRequest), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<TokenResponse> checkToken(
            @RequestHeader("Authorization") String token,
            @RequestParam("name") String name) {
        log.info("Обработка эндпойнта GET/tokens/.(Header(Authorization): token, param: name)");
        return new ResponseEntity<>(tokenService.checkToken(token, name), HttpStatus.OK);
    }
}
