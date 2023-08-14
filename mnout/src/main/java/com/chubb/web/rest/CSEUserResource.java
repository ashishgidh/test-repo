package com.chubb.web.rest;

import com.chubb.domain.CSEUser;
import com.chubb.repository.CSEUserRepository;
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
 * REST controller for managing {@link com.chubb.domain.CSEUser}.
 */
@RestController
@RequestMapping("/api")
public class CSEUserResource {

    private final Logger log = LoggerFactory.getLogger(CSEUserResource.class);

    private static final String ENTITY_NAME = "cSEUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CSEUserRepository cSEUserRepository;

    public CSEUserResource(CSEUserRepository cSEUserRepository) {
        this.cSEUserRepository = cSEUserRepository;
    }

    /**
     * {@code POST  /cse-users} : Create a new cSEUser.
     *
     * @param cSEUser the cSEUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cSEUser, or with status {@code 400 (Bad Request)} if the cSEUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cse-users")
    public ResponseEntity<CSEUser> createCSEUser(@RequestBody CSEUser cSEUser) throws URISyntaxException {
        log.debug("REST request to save CSEUser : {}", cSEUser);
        if (cSEUser.getId() != null) {
            throw new BadRequestAlertException("A new cSEUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CSEUser result = cSEUserRepository.save(cSEUser);
        return ResponseEntity
            .created(new URI("/api/cse-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cse-users/:id} : Updates an existing cSEUser.
     *
     * @param id the id of the cSEUser to save.
     * @param cSEUser the cSEUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cSEUser,
     * or with status {@code 400 (Bad Request)} if the cSEUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cSEUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cse-users/{id}")
    public ResponseEntity<CSEUser> updateCSEUser(@PathVariable(value = "id", required = false) final Long id, @RequestBody CSEUser cSEUser)
        throws URISyntaxException {
        log.debug("REST request to update CSEUser : {}, {}", id, cSEUser);
        if (cSEUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cSEUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cSEUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CSEUser result = cSEUserRepository.save(cSEUser);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cSEUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cse-users/:id} : Partial updates given fields of an existing cSEUser, field will ignore if it is null
     *
     * @param id the id of the cSEUser to save.
     * @param cSEUser the cSEUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cSEUser,
     * or with status {@code 400 (Bad Request)} if the cSEUser is not valid,
     * or with status {@code 404 (Not Found)} if the cSEUser is not found,
     * or with status {@code 500 (Internal Server Error)} if the cSEUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cse-users/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CSEUser> partialUpdateCSEUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CSEUser cSEUser
    ) throws URISyntaxException {
        log.debug("REST request to partial update CSEUser partially : {}, {}", id, cSEUser);
        if (cSEUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cSEUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cSEUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CSEUser> result = cSEUserRepository
            .findById(cSEUser.getId())
            .map(existingCSEUser -> {
                if (cSEUser.getName() != null) {
                    existingCSEUser.setName(cSEUser.getName());
                }
                if (cSEUser.getEmail() != null) {
                    existingCSEUser.setEmail(cSEUser.getEmail());
                }
                if (cSEUser.getPhone() != null) {
                    existingCSEUser.setPhone(cSEUser.getPhone());
                }

                return existingCSEUser;
            })
            .map(cSEUserRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cSEUser.getId().toString())
        );
    }

    /**
     * {@code GET  /cse-users} : get all the cSEUsers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cSEUsers in body.
     */
    @GetMapping("/cse-users")
    public List<CSEUser> getAllCSEUsers() {
        log.debug("REST request to get all CSEUsers");
        return cSEUserRepository.findAll();
    }

    /**
     * {@code GET  /cse-users/:id} : get the "id" cSEUser.
     *
     * @param id the id of the cSEUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cSEUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cse-users/{id}")
    public ResponseEntity<CSEUser> getCSEUser(@PathVariable Long id) {
        log.debug("REST request to get CSEUser : {}", id);
        Optional<CSEUser> cSEUser = cSEUserRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(cSEUser);
    }

    /**
     * {@code DELETE  /cse-users/:id} : delete the "id" cSEUser.
     *
     * @param id the id of the cSEUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cse-users/{id}")
    public ResponseEntity<Void> deleteCSEUser(@PathVariable Long id) {
        log.debug("REST request to delete CSEUser : {}", id);
        cSEUserRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
