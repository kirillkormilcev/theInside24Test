package info.theinside.token.mapper;

import info.theinside.token.dto.JwtResponse;
import info.theinside.token.model.Token;

public class TokenMapper {

    public static JwtResponse toJwtResponse(Token token) {
        return JwtResponse.builder()
                .token(token.getToken())
                .build();
    }
}
