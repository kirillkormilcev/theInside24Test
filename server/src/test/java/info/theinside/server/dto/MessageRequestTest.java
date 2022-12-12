package info.theinside.server.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class MessageRequestTest {

    @Autowired
    private JacksonTester<MessageRequest> json;

    @Test
    void test() throws IOException {

        MessageRequest messageRequest1 = MessageRequest.builder()
                .name("user 1")
                .message("message 1")
                .build();

        JsonContent<MessageRequest> result = json.write(messageRequest1);

        assertThat(result).hasJsonPath("$.name");
        assertThat(result).hasJsonPath("$.message");
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(messageRequest1.getName());
        assertThat(result).extractingJsonPathStringValue("$.message").isEqualTo(messageRequest1.getMessage());
    }
}