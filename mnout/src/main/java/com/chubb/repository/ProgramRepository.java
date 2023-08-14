package com.chubb.repository;

import com.chubb.domain.Program;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Program entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProgramRepository extends MongoRepository<Program, String> {}
