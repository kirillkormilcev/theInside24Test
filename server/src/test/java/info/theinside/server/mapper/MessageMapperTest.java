package info.theinside.server.mapper;

import info.theinside.server.dto.MessageResponse;
import info.theinside.server.model.Message;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageMapperTest {

    Message message = Message.builder()
            .name("user 1")
            .message("message 1")
            .build();

    @Test
    void test() {
        MessageResponse response = MessageMapper.toMessageResponse(message);

        assertEquals(response.getMessage(), message.getMessage());
    }

}