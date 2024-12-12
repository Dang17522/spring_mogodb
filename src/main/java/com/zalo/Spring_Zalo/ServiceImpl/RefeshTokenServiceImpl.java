package com.zalo.Spring_Zalo.ServiceImpl;

import com.zalo.Spring_Zalo.Entities.RefeshToken;
import com.zalo.Spring_Zalo.Repo.RefeshTokenMongoRepo;
import com.zalo.Spring_Zalo.Service.RefeshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class RefeshTokenServiceImpl implements RefeshTokenService {
    @Autowired
    private RefeshTokenMongoRepo refeshTokenRepo;


    @Override
    public RefeshToken findByRefreshToken(String refreshToken) {
        return refeshTokenRepo.findByRefreshToken(refreshToken).orElseThrow(() -> new RuntimeException("RefeshToken not found"));
    }

    @Override
    public boolean verifyExpiration(RefeshToken refeshToken) {
        if(refeshToken.getExpiryDate().compareTo(Instant.now()) <0){
            refeshTokenRepo.delete(refeshToken);
            return true;
        }
        return false;
    }

    @Override
    public RefeshToken saveToken(RefeshToken refeshToken) {
        return refeshTokenRepo.save(refeshToken);
    }

    @Override
    public void deleteToken(RefeshToken refeshToken) {
        refeshTokenRepo.delete(refeshToken);
    }
}
