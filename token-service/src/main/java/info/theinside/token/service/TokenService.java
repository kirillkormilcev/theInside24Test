package info.theinside.token.service;

import info.theinside.token.dto.JwtRequest;
import info.theinside.token.dto.JwtResponse;
import info.theinside.token.dto.TokenResponse;
import info.theinside.token.error.exception.AuthException;
import info.theinside.token.mapper.TokenMapper;
import info.theinside.token.model.Authentication;
import info.theinside.token.model.Token;
import info.theinside.token.repository.AuthenticationRepository;
import info.theinside.token.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class TokenService {
    final AuthenticationRepository authenticationRepository;
    final TokenRepository tokenRepository;
    final JwtProvider jwtProvider;

    @Transactional
    public JwtResponse getTokenByAuth(JwtRequest jwtRequest) {
        Authentication authentication = findAuthByName(jwtRequest.getName());

        if (authentication.getPassword().equals(jwtRequest.getPassword())) {

            if (authentication.getToken() != null) {

                if (jwtProvider.validateAccessToken(authentication.getToken().getToken())) {
                    return TokenMapper.toJwtResponse(authentication.getToken());

                } else {
                    long oldTokenId = authentication.getToken().getId();
                    authentication.setToken(null);
                    tokenRepository.deleteById(oldTokenId);
                    Token token = tokenRepository.save(Token.builder()
                            .token(jwtProvider.generateAccessToken(authentication.getName()))
                            .build());
                    authentication.setToken(token);
                    return TokenMapper.toJwtResponse(token);
                }
            } else {
                Token token = tokenRepository.save(Token.builder()
                        .token(jwtProvider.generateAccessToken(authentication.getName()))
                        .build());
                authentication.setToken(token);
                return TokenMapper.toJwtResponse(token);
            }
        } else {
            throw new AuthException("Не правильный пароль.");
        }
    }

    public TokenResponse checkToken(String token, String name) {

        if (token != null && jwtProvider.validateAccessToken(token)) {
            Claims claims = jwtProvider.getAccessClaims(token);

            if (findAuthByName(claims.getSubject()).getName().equals(name)) {
                return TokenResponse.builder()
                        .response("correct token")
                        .build();
            } else {
                return TokenResponse.builder()
                        .response("token owner is incorrect")
                        .build();
            }
        } else {
            return TokenResponse.builder()
                    .response("token is incorrect or expired")
                    .build();
        }
    }

    private Authentication findAuthByName(String name) {
        return authenticationRepository.findByName(name)
                .orElseThrow(() -> new AuthException("Пользователь не найден"));
    }
}
