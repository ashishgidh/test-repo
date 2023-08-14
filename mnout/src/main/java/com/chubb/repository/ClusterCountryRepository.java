package com.chubb.repository;

import com.chubb.domain.ClusterCountry;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the ClusterCountry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClusterCountryRepository extends MongoRepository<ClusterCountry, Long> {}
