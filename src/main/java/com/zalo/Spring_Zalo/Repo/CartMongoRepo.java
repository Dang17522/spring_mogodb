package com.zalo.Spring_Zalo.Repo;

import com.zalo.Spring_Zalo.Entities.Cart;
import com.zalo.Spring_Zalo.Entities.Roles;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartMongoRepo extends MongoRepository<Cart,Integer> {

    
}
