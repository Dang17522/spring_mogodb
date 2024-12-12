package com.zalo.Spring_Zalo.Service;

import com.zalo.Spring_Zalo.Entities.RefeshToken;
import org.springframework.data.mongodb.repository.Query;

public interface RefeshTokenService {
    @Query(value = "{'refreshToken': ?0}")
    RefeshToken findByRefreshToken(String refreshToken);
    boolean verifyExpiration(RefeshToken refeshToken);
    RefeshToken saveToken(RefeshToken refeshToken);
    void deleteToken(RefeshToken refeshToken);
}
