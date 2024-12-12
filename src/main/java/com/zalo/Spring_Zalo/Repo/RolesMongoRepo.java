package com.zalo.Spring_Zalo.Repo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.zalo.Spring_Zalo.Entities.Roles;
@Repository
public interface RolesMongoRepo  extends MongoRepository<Roles,Integer> {
    @Query(value="{'users.id' : $0}", delete = true)
    void removeUserFromRoleByUserId(int userId);
}
