package com.zalo.Spring_Zalo.Repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.zalo.Spring_Zalo.Entities.Category;

@Repository
public interface CategoryMongoRepo extends MongoRepository<Category,Integer> {

}
