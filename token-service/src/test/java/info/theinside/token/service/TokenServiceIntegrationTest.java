package info.theinside.token.service;

import info.theinside.token.dto.JwtRequest;
import info.theinside.token.dto.JwtResponse;
import info.theinside.token.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(
        properties = {"spring.datasource.url=jdbc:h2:mem:token_service_db",
        "spring.datasource.driverClassName=org.h2.Driver"},
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
class TokenServiceIntegrationTest {

    private final TokenService tokenService;

    String name = "user 1";

    JwtRequest jwtRequest = JwtRequest.builder()
            .name(name)
            .password("password1")
            .build();

    TokenResponse tokenResponseCorrect = TokenResponse.builder()
            .response("correct token")
            .build();

    TokenResponse tokenResponseIncorrectOwner = TokenResponse.builder()
            .response("token owner is incorrect")
            .build();

    @Test
    void test() {
        JwtResponse jwtResponse = tokenService.getTokenByAuth(jwtRequest);
        String token = jwtResponse.getToken();

        assertEquals(tokenService.checkToken(token, name), tokenResponseCorrect);

        assertEquals(tokenService.checkToken(token, "wrong"), tokenResponseIncorrectOwner);
    }
}