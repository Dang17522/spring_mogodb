package com.zalo.Spring_Zalo.Repo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.zalo.Spring_Zalo.Entities.RefeshToken;
@Repository
public interface RefeshTokenMongoRepo extends MongoRepository<RefeshToken, Integer> {
    Optional<RefeshToken> findByRefreshToken(String refreshToken);

}
