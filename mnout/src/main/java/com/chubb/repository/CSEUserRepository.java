package com.chubb.repository;

import com.chubb.domain.CSEUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the CSEUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CSEUserRepository extends MongoRepository<CSEUser, Long> {}
