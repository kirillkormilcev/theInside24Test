package info.theinside.server.repository;

import info.theinside.server.model.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MessageRepositoryTest {

    @Autowired
    MessageRepository messageRepository;

    @Test
    void messageRepositoryTest() {

        Message message1 = messageRepository.save(Message.builder()
                .name("user 1")
                .message("message 1")
                .build());

        assertEquals(message1.getName(), "user 1");

        messageRepository.save(Message.builder()
                .name("user 2")
                .message("message 2")
                .build());

        List<Message> messages = messageRepository.findAllByName("user 1",
                new PageRequestModified(0, 10, Sort.by("id").descending()));

        assertEquals(messages.size(), 1);
        assertEquals(messages.get(0).getMessage(), "message 1");
    }
}