package info.theinside.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import info.theinside.server.dto.MessageRequest;
import info.theinside.server.dto.MessageResponse;
import info.theinside.server.service.ServerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ServerControllerRestTest {

    ServerService serverService = Mockito.mock(ServerService.class);

    private final ServerController serverController = new ServerController(serverService);

    private final ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

    private final MockMvc mvc = MockMvcBuilders
            .standaloneSetup(serverController)
            .build();

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


    String bearerToken = "Bearer_eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIDEiLCJleHAiOjE2NzA4NTM5MDJ9.OL0nd-c8QLyxiT_NaC2JB4VxrxEl2uDXykOp8M7Ef0cut10e9e_YGgLQphUq22cx3H_Ku-dMPFMm-WasqgfSVA";

    @Test
    void addMessageTest() throws Exception {

        Mockito.when(serverService.addMessage(Mockito.any(MessageRequest.class), Mockito.anyString()))
                .thenReturn(messageResponse1);

        mvc.perform(post("/message")
                    .content(mapper.writeValueAsString(messageRequest1))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", bearerToken)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(messageResponse1.getMessage()), String.class));

        Mockito.when(serverService.addMessage(Mockito.any(MessageRequest.class), Mockito.anyString()))
                .thenReturn(new ArrayList<MessageResponse>());

        MvcResult mvcResult = mvc.perform(post("/message")
                        .content(mapper.writeValueAsString(messageRequestHistory10))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", bearerToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(mvcResult.getResponse().getContentLength(), 0);
    }
}