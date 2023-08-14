package com.chubb.repository;

import com.chubb.domain.Cluster;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Cluster entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClusterRepository extends MongoRepository<Cluster, Long> {}
