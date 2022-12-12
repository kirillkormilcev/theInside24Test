package info.theinside.server.service;

import info.theinside.server.client.MyFeignClient;
import info.theinside.server.client.dto.TokenResponse;
import info.theinside.server.dto.MessageRequest;
import info.theinside.server.dto.MessageResponse;
import info.theinside.server.error.exception.BearerValidation;
import info.theinside.server.error.exception.TokenValidation;
import info.theinside.server.mapper.MessageMapper;
import info.theinside.server.model.Message;
import info.theinside.server.repository.MessageRepository;
import info.theinside.server.repository.PageRequestModified;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ServerService {
    final MyFeignClient feignClient;
    final MessageRepository messageRepository;

    @Transactional
    public Object addMessage(MessageRequest messageRequest, String bearerToken) {

        if (!bearerToken.isEmpty()
                && !bearerToken.isBlank()
                && bearerToken.contains("Bearer_")) {
            String token = bearerToken.substring(bearerToken.indexOf("_") + 1);
            TokenResponse tokenResponse = feignClient.checkAuthentication(token, messageRequest.getName());

            if (tokenResponse.getResponse().equals("correct token")) {

                if (messageRequest.getMessage().contains("history ")) {
                    int size = Integer.parseInt(messageRequest.getMessage().substring(8));
                    List<Message> messages = messageRepository.findAllByName(messageRequest.getName(),
                            new PageRequestModified(0, size, Sort.by("id").descending()));
                    return messages.stream().map(MessageMapper::toMessageResponse).collect(Collectors.toList());

                } else {
                    messageRepository.save(Message.builder()
                            .name(messageRequest.getName())
                            .message(messageRequest.getMessage())
                            .build());
                    return MessageResponse.builder()
                            .message("Сообщение сохранено.")
                            .build();
                }
            } else {
                throw new TokenValidation(tokenResponse.getResponse());
            }
        } else {
            throw new BearerValidation("Не корректный предъявитель (bearer).");
        }
    }
}
