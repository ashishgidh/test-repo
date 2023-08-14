package com.chubb.web.rest;

import com.chubb.domain.LOB;
import com.chubb.repository.LOBRepository;
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
 * REST controller for managing {@link com.chubb.domain.LOB}.
 */
@RestController
@RequestMapping("/api")
public class LOBResource {

    private final Logger log = LoggerFactory.getLogger(LOBResource.class);

    private static final String ENTITY_NAME = "lOB";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LOBRepository lOBRepository;

    public LOBResource(LOBRepository lOBRepository) {
        this.lOBRepository = lOBRepository;
    }

    /**
     * {@code POST  /lobs} : Create a new lOB.
     *
     * @param lOB the lOB to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lOB, or with status {@code 400 (Bad Request)} if the lOB has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lobs")
    public ResponseEntity<LOB> createLOB(@RequestBody LOB lOB) throws URISyntaxException {
        log.debug("REST request to save LOB : {}", lOB);
        if (lOB.getId() != null) {
            throw new BadRequestAlertException("A new lOB cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LOB result = lOBRepository.save(lOB);
        return ResponseEntity
            .created(new URI("/api/lobs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lobs/:id} : Updates an existing lOB.
     *
     * @param id the id of the lOB to save.
     * @param lOB the lOB to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lOB,
     * or with status {@code 400 (Bad Request)} if the lOB is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lOB couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lobs/{id}")
    public ResponseEntity<LOB> updateLOB(@PathVariable(value = "id", required = false) final Long id, @RequestBody LOB lOB)
        throws URISyntaxException {
        log.debug("REST request to update LOB : {}, {}", id, lOB);
        if (lOB.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lOB.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lOBRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LOB result = lOBRepository.save(lOB);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, lOB.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /lobs/:id} : Partial updates given fields of an existing lOB, field will ignore if it is null
     *
     * @param id the id of the lOB to save.
     * @param lOB the lOB to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lOB,
     * or with status {@code 400 (Bad Request)} if the lOB is not valid,
     * or with status {@code 404 (Not Found)} if the lOB is not found,
     * or with status {@code 500 (Internal Server Error)} if the lOB couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lobs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LOB> partialUpdateLOB(@PathVariable(value = "id", required = false) final Long id, @RequestBody LOB lOB)
        throws URISyntaxException {
        log.debug("REST request to partial update LOB partially : {}, {}", id, lOB);
        if (lOB.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lOB.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lOBRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LOB> result = lOBRepository
            .findById(lOB.getId())
            .map(existingLOB -> {
                if (lOB.getWvLobCode() != null) {
                    existingLOB.setWvLobCode(lOB.getWvLobCode());
                }
                if (lOB.getWvLobName() != null) {
                    existingLOB.setWvLobName(lOB.getWvLobName());
                }
                if (lOB.getMaxLobCode() != null) {
                    existingLOB.setMaxLobCode(lOB.getMaxLobCode());
                }
                if (lOB.getMaxLobName() != null) {
                    existingLOB.setMaxLobName(lOB.getMaxLobName());
                }

                return existingLOB;
            })
            .map(lOBRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, lOB.getId().toString())
        );
    }

    /**
     * {@code GET  /lobs} : get all the lOBS.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lOBS in body.
     */
    @GetMapping("/lobs")
    public List<LOB> getAllLOBS() {
        log.debug("REST request to get all LOBS");
        return lOBRepository.findAll();
    }

    /**
     * {@code GET  /lobs/:id} : get the "id" lOB.
     *
     * @param id the id of the lOB to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lOB, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lobs/{id}")
    public ResponseEntity<LOB> getLOB(@PathVariable Long id) {
        log.debug("REST request to get LOB : {}", id);
        Optional<LOB> lOB = lOBRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(lOB);
    }

    /**
     * {@code DELETE  /lobs/:id} : delete the "id" lOB.
     *
     * @param id the id of the lOB to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lobs/{id}")
    public ResponseEntity<Void> deleteLOB(@PathVariable Long id) {
        log.debug("REST request to delete LOB : {}", id);
        lOBRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
