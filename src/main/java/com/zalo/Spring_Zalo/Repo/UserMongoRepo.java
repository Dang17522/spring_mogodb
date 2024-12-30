package com.zalo.Spring_Zalo.Repo;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.zalo.Spring_Zalo.Entities.User;

@Repository
public interface UserMongoRepo extends MongoRepository<User, Integer> {
    @Query("{ 'username' : ?0, 'password' : ?1 }")
    Optional<User> findByUserNameAndPassword(String username,String password);

    @Query("{ 'username' : ?0 }")
    User findByUserName(String username);

    User findByEmail(String email);

    @Query("{ }")
    Page<User> findAll(Pageable pageable);

    @Query("{ $and: [ " +
            "{ $or: [ " +
            "{ username: { $regex: ?0, $options: 'i' } }, " +
            "{ email: { $regex: ?0, $options: 'i' } } " +
            "] }, " +
            "{ createAt: { $gte: ?1, $lte: ?2 } } " +
            "] " +
            "}")
    Page<User> findByUserNameOrEmail(String key, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

//    @Query("SELECT COUNT(u) FROM User u JOIN u.company c WHERE c.name = :companyName")
//    long countByBusinessName( String companyName);

//    @Query("SELECT u FROM User u LEFT JOIN FETCH u.role")
//    Page<User> findAllWithRole(Pageable pageable);

}
