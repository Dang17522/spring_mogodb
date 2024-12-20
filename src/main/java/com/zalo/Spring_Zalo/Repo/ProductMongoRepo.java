package com.zalo.Spring_Zalo.Repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.zalo.Spring_Zalo.Entities.Product;
@Repository
public interface ProductMongoRepo extends MongoRepository<Product, Integer> {
//    @Query("select p from Product p where p.company.id = :companyId order by p.createAt desc ")
    @Query(value = "{ 'company.id' : ?0 }", sort = "{ 'createAt': -1 }")
    Page<Product> findAllbyCompanyId(Integer companyId, Pageable pageable);

//    @Query("select p from Product p  order by p.createAt desc ")
    @Query(value = "{ name: { $regex: ?0, $options: 'i' } }")
    Page<Product> findAllByKey(String key, Pageable pageable);
//    @Query(value = "SELECT * FROM products WHERE company_id =:companyId AND id NOT IN (SELECT product_id FROM product_events WHERE company_id =:companyId AND event_id =:eventId)", nativeQuery = true)
    @Query("{ 'companyId': ?0, 'id': { $nin: ?1 } }")
    List<Product> findAllByNotInProductEvents(Integer companyId, Integer eventId);

//    @Query(value = "SELECT * FROM products WHERE concat(product_name) LIKE %:key% AND company_id =:companyId AND id NOT IN (SELECT product_id FROM product_events WHERE company_id =:companyId AND event_id =:eventId)", nativeQuery = true)
    @Query("{ 'productName': { $regex: ?0, $options: 'i' }, 'companyId': ?1, 'id': { $nin: ?2 } }")
    List<Product> searchByNotInProductEvents(String key,Integer companyId, Integer eventId);
}
