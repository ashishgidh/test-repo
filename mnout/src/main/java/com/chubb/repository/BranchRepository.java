package com.chubb.repository;

import com.chubb.domain.Branch;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Branch entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BranchRepository extends MongoRepository<Branch, String> {}
