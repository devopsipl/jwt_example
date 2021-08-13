package pl.devopsijwt.security.domain.service;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.devopsijwt.common.exception.NotFoundException;
import pl.devopsijwt.common.exception.TokenRefreshException;
import pl.devopsijwt.security.config.JwtTokenUtil;
import pl.devopsijwt.security.domain.dto.RefreshTokenRequest;
import pl.devopsijwt.security.domain.dto.TokenRefreshResponse;
import pl.devopsijwt.security.domain.model.RefreshToken;
import pl.devopsijwt.security.infrastructure.reopsitory.RefreshTokenRepository;
import pl.devopsijwt.security.infrastructure.reopsitory.UserRepo;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Value("${devopsijwt.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepo userRepository;
    private final JwtTokenUtil jwtTokenUtil;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Transactional
    public RefreshToken createRefreshToken(ObjectId userId) {

        return userRepository.findById(userId).map
            (user -> refreshTokenRepository.save(RefreshToken.builder()
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .userId(user.getId())
                .token(UUID.randomUUID().toString())
                .build())
            )
            .orElseThrow(() -> new NotFoundException("User not found with id " + userId.toHexString()));
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }

    public ResponseEntity<TokenRefreshResponse> refreshToken(RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return findByToken(requestRefreshToken)
            .map(this::verifyExpiration)
            .map(RefreshToken::getUserId)
            .map(userId -> {
                String token = jwtTokenUtil.generateAccessToken(userRepository.findById(userId).orElseThrow());
                return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
            })
            .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                "Refresh token is not in database!"));
    }
}
