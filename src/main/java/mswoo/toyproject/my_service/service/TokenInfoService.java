package mswoo.toyproject.my_service.service;

import lombok.RequiredArgsConstructor;
import mswoo.toyproject.my_service.code.ErrorCode;
import mswoo.toyproject.my_service.domain.dto.TokenDto;
import mswoo.toyproject.my_service.domain.entity.TokenInfo;
import mswoo.toyproject.my_service.repository.TokenRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class TokenInfoService {

    private final TokenRepository tokenRepository;


    public TokenDto getTokenInfoByUserId(String userId) {
        TokenInfo tokenInfo = tokenRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.OK, ErrorCode.EMPTY_DATA.name()));

        return TokenDto.builder()
                .accessToken(tokenInfo.getAccessToken())
                .refreshToken(tokenInfo.getRefreshToken())
                .build();
    }

    public TokenDto getTokenInfoByAccessToken(String accessToken) {
        TokenInfo tokenInfo = tokenRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.OK, ErrorCode.EMPTY_DATA.name()));

        return TokenDto.builder()
                .accessToken(tokenInfo.getAccessToken())
                .refreshToken(tokenInfo.getRefreshToken())
                .build();
    }

    public void saveTokenInfo(TokenInfo tokenInfo) {
        tokenRepository.save(tokenInfo);
    }

    public void deleteTokenInfoByUserId(String userId) {
        tokenRepository.deleteById(userId);
    }

}
