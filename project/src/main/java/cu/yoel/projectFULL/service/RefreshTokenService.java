package cu.yoel.projectFULL.service;

import cu.yoel.projectFULL.models.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
     Optional<RefreshToken> findByToken(String token);
     RefreshToken createRefreshToken(String userId);
     RefreshToken verifyExpiration(RefreshToken token);
    int deleteByUserId(String userId);
}
