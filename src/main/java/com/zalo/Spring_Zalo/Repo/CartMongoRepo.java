package com.zalo.Spring_Zalo.Repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.zalo.Spring_Zalo.Entities.Cart;

@Repository
public interface CartMongoRepo extends MongoRepository<Cart,Integer> {

    
}
