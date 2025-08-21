package org.springboot.services;

import org.springboot.entities.RefreshToken;
import org.springboot.entities.UserInfo;
import org.springboot.model.UserInfoDto;
import org.springboot.repository.RefreshTokenRepo;
import org.springboot.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    RefreshTokenRepo refreshTokenRepo;

    @Autowired
    UserRepo userRepo;

    public RefreshToken createRefreshToken(String username) {
        UserInfo userInfoExtracted = userRepo.findByUsername(username);
        RefreshToken refreshToken = RefreshToken.builder()
                .userInfo(userInfoExtracted)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(1000*60))
                .build();

        return refreshTokenRepo.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if(token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepo.delete(token);

            throw new RuntimeException(token.getToken() + "Refresh token is expired. Login again");
        }
        return token;
    }

    public Optional<RefreshToken> findByToken(String token) {

        return refreshTokenRepo.findByToken(token);
    }
}
