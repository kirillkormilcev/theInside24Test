package info.theinside.token.repository;

import info.theinside.token.error.exception.AuthException;
import info.theinside.token.model.Authentication;
import info.theinside.token.model.Token;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RepositoryTest {

    @Autowired
    AuthenticationRepository authenticationRepository;

    @Autowired
    TokenRepository tokenRepository;

    @Test
    void test() {
        String tokenString = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIDEiLCJleHAiOjE2NzA4NTM5MDJ9.OL0nd-c8QLyxiT_NaC2JB4VxrxEl2uDXykOp8M7Ef0cut10e9e_YGgLQphUq22cx3H_Ku-dMPFMm-WasqgfSVA";

        Token token = tokenRepository.save(Token.builder()
                .token(tokenString)
                .build());

        assertEquals(token.getToken(), tokenString);

        Authentication authentication = authenticationRepository.save(Authentication.builder()
                .name("user 3")
                .password("password3")
                .token(token)
                .build());

        assertEquals(authentication.getName(), "user 3");
        assertEquals(authentication.getPassword(), "password3");
        assertEquals(authentication.getToken().getToken(), tokenString);

        Authentication authenticationFounded = authenticationRepository.findByName("user 1")
                .orElseThrow(() -> new AuthException("Пользователь не найден"));

        assertEquals(authenticationFounded.getPassword(), "password1");
    }
}