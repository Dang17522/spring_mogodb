package com.zalo.Spring_Zalo.Repo;

import com.zalo.Spring_Zalo.Entities.Cart;
import com.zalo.Spring_Zalo.Entities.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryMongoRepo extends MongoRepository<Category,Integer> {

    
}
