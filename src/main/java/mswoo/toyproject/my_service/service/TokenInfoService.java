package mswoo.toyproject.my_service.service;

import lombok.RequiredArgsConstructor;
import mswoo.toyproject.my_service.enums.ErrorCode;
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


    public TokenDto getTokenInfoById(String accessToken) {
        TokenInfo tokenInfo = tokenRepository.findById(accessToken)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.OK, ErrorCode.EMPTY_DATA.name()));

        return TokenDto.builder()
                .accessToken(tokenInfo.getAccessToken())
                .refreshToken(tokenInfo.getRefreshToken())
                .build();
    }


    public void saveTokenInfo(TokenInfo tokenInfo) {
        tokenRepository.save(tokenInfo);
    }

    public void deleteTokenInfoById(String accessToken) {
        tokenRepository.deleteById(accessToken);
    }

}
