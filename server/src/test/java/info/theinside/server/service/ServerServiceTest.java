package info.theinside.server.service;

import info.theinside.server.client.MyFeignClient;
import info.theinside.server.client.dto.TokenResponse;
import info.theinside.server.dto.MessageRequest;
import info.theinside.server.dto.MessageResponse;
import info.theinside.server.error.exception.BearerValidation;
import info.theinside.server.error.exception.TokenValidation;
import info.theinside.server.model.Message;
import info.theinside.server.repository.MessageRepository;
import info.theinside.server.repository.PageRequestModified;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ServerServiceTest {

    MessageRepository messageRepository = mock(MessageRepository.class);
    MyFeignClient feignClient = mock(MyFeignClient.class);

    ServerService serverService = new ServerService(feignClient, messageRepository);

    MessageRequest messageRequest1 = MessageRequest.builder()
            .name("user 1")
            .message("message 1")
            .build();

    MessageRequest messageRequestHistory10 = MessageRequest.builder()
            .name("user 1")
            .message("history 10")
            .build();

    MessageResponse messageResponse1 = MessageResponse.builder()
            .message("Сообщение сохранено.")
            .build();

    Message message1 = Message.builder()
            .name("user 1")
            .message("message 1")
            .build();

    TokenResponse tokenResponseCorrect = TokenResponse.builder()
            .response("correct token")
            .build();

    TokenResponse tokenResponseIncorrect = TokenResponse.builder()
            .response("token is incorrect or expired")
            .build();

    TokenResponse tokenResponseIncorrectOwner = TokenResponse.builder()
            .response("token owner is incorrect")
            .build();

    String bearerToken = "Bearer_eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIDEiLCJleHAiOjE2NzA4NTM5MDJ9.OL0nd-c8QLyxiT_NaC2JB4VxrxEl2uDXykOp8M7Ef0cut10e9e_YGgLQphUq22cx3H_Ku-dMPFMm-WasqgfSVA";


    @Test
    void addMessageTest() {

        Mockito.when(feignClient.checkAuthentication(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(tokenResponseCorrect);
        Mockito.when(messageRepository.save(Mockito.any(Message.class)))
                .thenReturn(message1);

        assertEquals(serverService.addMessage(messageRequest1, bearerToken), messageResponse1);

        BearerValidation ex1 = assertThrows(BearerValidation.class,
                () -> serverService.addMessage(messageRequest1, "incorrect_token"));
        assertEquals(ex1.getMessage(), "Не корректный предъявитель (bearer).");

        Mockito.when(messageRepository.findAllByName(messageRequestHistory10.getName(),
                new PageRequestModified(0, 10, Sort.by("id").descending())))
                .thenReturn(new ArrayList<>(10));

        assertEquals(serverService.addMessage(messageRequestHistory10, bearerToken), new ArrayList<>(10));

        Mockito.when(feignClient.checkAuthentication(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(tokenResponseIncorrect);

        TokenValidation ex2 = assertThrows(TokenValidation.class,
                () -> serverService.addMessage(messageRequest1, bearerToken));
        assertEquals(ex2.getMessage(), "token is incorrect or expired");

        Mockito.when(feignClient.checkAuthentication(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(tokenResponseIncorrectOwner);

        TokenValidation ex3 = assertThrows(TokenValidation.class,
                () -> serverService.addMessage(messageRequest1, bearerToken));
        assertEquals(ex3.getMessage(), "token owner is incorrect");
    }
}