package com.chubb.repository;

import com.chubb.domain.LOB;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the LOB entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LOBRepository extends MongoRepository<LOB, Long> {}
