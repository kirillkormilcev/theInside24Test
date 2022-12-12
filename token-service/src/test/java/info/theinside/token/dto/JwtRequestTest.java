package info.theinside.token.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class JwtRequestTest {

    @Autowired
    private JacksonTester<JwtRequest> json;

    @Test
    void test() throws IOException {

        String name = "user 1";

        JwtRequest jwtRequest = JwtRequest.builder()
                .name(name)
                .password("password1")
                .build();

        JsonContent<JwtRequest> result = json.write(jwtRequest);

        assertThat(result).hasJsonPath("$.name");
        assertThat(result).hasJsonPath("$.password");
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(jwtRequest.getName());
        assertThat(result).extractingJsonPathStringValue("$.password").isEqualTo(jwtRequest.getPassword());
    }

}