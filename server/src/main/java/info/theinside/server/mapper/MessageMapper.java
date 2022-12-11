package info.theinside.server.mapper;

import info.theinside.server.dto.MessageResponse;
import info.theinside.server.model.Message;

public class MessageMapper {
    public static MessageResponse toMessageResponse(Message message) {
        return MessageResponse.builder()
                .message(message.getMessage())
                .build();
    }
}
