package com.chubb.web.rest;

import com.chubb.domain.Program;
import com.chubb.repository.ProgramRepository;
import com.chubb.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.chubb.domain.Program}.
 */
@RestController
@RequestMapping("/api")
public class ProgramResource {

    private final Logger log = LoggerFactory.getLogger(ProgramResource.class);

    private static final String ENTITY_NAME = "program";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProgramRepository programRepository;

    public ProgramResource(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    /**
     * {@code POST  /programs} : Create a new program.
     *
     * @param program the program to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new program, or with status {@code 400 (Bad Request)} if the program has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/programs")
    public ResponseEntity<Program> createProgram(@RequestBody Program program) throws URISyntaxException {
        log.debug("REST request to save Program : {}", program);
        if (program.getId() != null) {
            throw new BadRequestAlertException("A new program cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Program result = programRepository.save(program);
        return ResponseEntity
            .created(new URI("/api/programs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /programs/:id} : Updates an existing program.
     *
     * @param id the id of the program to save.
     * @param program the program to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated program,
     * or with status {@code 400 (Bad Request)} if the program is not valid,
     * or with status {@code 500 (Internal Server Error)} if the program couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/programs/{id}")
    public ResponseEntity<Program> updateProgram(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Program program
    ) throws URISyntaxException {
        log.debug("REST request to update Program : {}, {}", id, program);
        if (program.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, program.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!programRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Program result = programRepository.save(program);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, program.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /programs/:id} : Partial updates given fields of an existing program, field will ignore if it is null
     *
     * @param id the id of the program to save.
     * @param program the program to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated program,
     * or with status {@code 400 (Bad Request)} if the program is not valid,
     * or with status {@code 404 (Not Found)} if the program is not found,
     * or with status {@code 500 (Internal Server Error)} if the program couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/programs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Program> partialUpdateProgram(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Program program
    ) throws URISyntaxException {
        log.debug("REST request to partial update Program partially : {}, {}", id, program);
        if (program.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, program.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!programRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Program> result = programRepository
            .findById(program.getId())
            .map(existingProgram -> {
                if (program.getProgramId() != null) {
                    existingProgram.setProgramId(program.getProgramId());
                }
                if (program.getClusterId() != null) {
                    existingProgram.setClusterId(program.getClusterId());
                }
                if (program.getCountryId() != null) {
                    existingProgram.setCountryId(program.getCountryId());
                }
                if (program.getBranchId() != null) {
                    existingProgram.setBranchId(program.getBranchId());
                }
                if (program.getMaxLOBId() != null) {
                    existingProgram.setMaxLOBId(program.getMaxLOBId());
                }
                if (program.getWvLOBId() != null) {
                    existingProgram.setWvLOBId(program.getWvLOBId());
                }
                if (program.getProgramEffectiveDate() != null) {
                    existingProgram.setProgramEffectiveDate(program.getProgramEffectiveDate());
                }
                if (program.getProgramExpiryDate() != null) {
                    existingProgram.setProgramExpiryDate(program.getProgramExpiryDate());
                }

                return existingProgram;
            })
            .map(programRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, program.getId())
        );
    }

    /**
     * {@code GET  /programs} : get all the programs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of programs in body.
     */
    @GetMapping("/programs")
    public List<Program> getAllPrograms() {
        log.debug("REST request to get all Programs");
        return programRepository.findAll();
    }

    /**
     * {@code GET  /programs/:id} : get the "id" program.
     *
     * @param id the id of the program to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the program, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/programs/{id}")
    public ResponseEntity<Program> getProgram(@PathVariable String id) {
        log.debug("REST request to get Program : {}", id);
        Optional<Program> program = programRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(program);
    }

    /**
     * {@code DELETE  /programs/:id} : delete the "id" program.
     *
     * @param id the id of the program to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/programs/{id}")
    public ResponseEntity<Void> deleteProgram(@PathVariable String id) {
        log.debug("REST request to delete Program : {}", id);
        programRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
