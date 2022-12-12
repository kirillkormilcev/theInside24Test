package info.theinside.token.service;

import info.theinside.token.dto.JwtRequest;
import info.theinside.token.dto.JwtResponse;
import info.theinside.token.dto.TokenResponse;
import info.theinside.token.error.exception.AuthException;
import info.theinside.token.model.Authentication;
import info.theinside.token.model.Token;
import info.theinside.token.repository.AuthenticationRepository;
import info.theinside.token.repository.TokenRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class TokenServiceTest {

    AuthenticationRepository authenticationRepository = mock(AuthenticationRepository.class);
    TokenRepository tokenRepository = mock(TokenRepository.class);
    JwtProvider jwtProvider = mock(JwtProvider.class);

    TokenService tokenService = new TokenService(authenticationRepository, tokenRepository, jwtProvider);

    String name = "user 1";

    JwtRequest jwtRequest = JwtRequest.builder()
            .name(name)
            .password("password1")
            .build();

    String tokenString = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIDEiLCJleHAiOjE2NzA4NTM5MDJ9.OL0nd-c8QLyxiT_NaC2JB4VxrxEl2uDXykOp8M7Ef0cut10e9e_YGgLQphUq22cx3H_Ku-dMPFMm-WasqgfSVA";


    Token token = Token.builder()
            .token(tokenString)
            .build();

    Authentication authentication =Authentication.builder()
            .name(name)
            .password("password1")
            .token(token)
            .build();

    JwtResponse jwtResponse = JwtResponse.builder()
            .token(tokenString)
            .build();

    TokenResponse tokenResponseIncorrect = TokenResponse.builder()
            .response("token is incorrect or expired")
            .build();

    @Test
    void getTokenByAuthTest() {

        Mockito.when(authenticationRepository.findByName(Mockito.anyString()))
                .thenReturn(Optional.ofNullable(authentication));
        Mockito.when(jwtProvider.validateAccessToken(Mockito.anyString()))
                .thenReturn(true);

        assertEquals(tokenService.getTokenByAuth(jwtRequest), jwtResponse);

        Mockito.when(jwtProvider.validateAccessToken(Mockito.anyString()))
                .thenReturn(false);
        Mockito.when(tokenRepository.save(Mockito.any(Token.class)))
                .thenReturn(token);

        assertEquals(tokenService.getTokenByAuth(jwtRequest), jwtResponse);

        authentication.setToken(null);

        assertEquals(tokenService.getTokenByAuth(jwtRequest), jwtResponse);

        authentication.setToken(token);

        jwtRequest.setPassword("wrong");

        AuthException ex = assertThrows(AuthException.class,
                () -> tokenService.getTokenByAuth(jwtRequest));

        assertEquals(ex.getMessage(), "Не правильный пароль.");

        jwtRequest.setPassword("password1");
    }

    @Test
    void checkTokenTest() {

        Mockito.when(jwtProvider.validateAccessToken(tokenString))
                .thenReturn(false);

        assertEquals(tokenService.checkToken(tokenString, name), tokenResponseIncorrect);
    }

}