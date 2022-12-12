package info.theinside.token.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import info.theinside.token.dto.JwtRequest;
import info.theinside.token.dto.JwtResponse;
import info.theinside.token.dto.TokenResponse;
import info.theinside.token.service.TokenService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TokenControllerRestTest {

    TokenService tokenService = Mockito.mock(TokenService.class);

    private final TokenController tokenController = new TokenController(tokenService);

    private final ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

    private final MockMvc mvc = MockMvcBuilders
            .standaloneSetup(tokenController)
            .build();

    String name = "user 1";

    JwtRequest jwtRequest = JwtRequest.builder()
            .name(name)
            .password("password1")
            .build();

    String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIDEiLCJleHAiOjE2NzA4NTM5MDJ9.OL0nd-c8QLyxiT_NaC2JB4VxrxEl2uDXykOp8M7Ef0cut10e9e_YGgLQphUq22cx3H_Ku-dMPFMm-WasqgfSVA";

    JwtResponse jwtResponse = JwtResponse.builder()
            .token(token)
            .build();

    TokenResponse tokenResponse = TokenResponse.builder()
            .response("correct token")
            .build();

    @Test
    void getTokenByAuthTest() throws Exception {

        Mockito.when(tokenService.getTokenByAuth(Mockito.any(JwtRequest.class)))
                .thenReturn(jwtResponse);

        mvc.perform(post("/authenticate")
                    .content(mapper.writeValueAsString(jwtRequest))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is(jwtResponse.getToken()), String.class));
    }

    @Test
    void checkTokenTest() throws Exception {

        Mockito.when(tokenService.checkToken(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(tokenResponse);

        mvc.perform(get("/authenticate")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .param("name", name)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response", is(tokenResponse.getResponse()), String.class));
    }
}