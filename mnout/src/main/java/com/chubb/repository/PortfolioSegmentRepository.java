package com.chubb.repository;

import com.chubb.domain.PortfolioSegment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the PortfolioSegment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PortfolioSegmentRepository extends MongoRepository<PortfolioSegment, String> {}
