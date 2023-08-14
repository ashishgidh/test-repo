package com.chubb.web.rest;

import com.chubb.domain.ClusterCountry;
import com.chubb.repository.ClusterCountryRepository;
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
 * REST controller for managing {@link com.chubb.domain.ClusterCountry}.
 */
@RestController
@RequestMapping("/api")
public class ClusterCountryResource {

    private final Logger log = LoggerFactory.getLogger(ClusterCountryResource.class);

    private static final String ENTITY_NAME = "clusterCountry";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClusterCountryRepository clusterCountryRepository;

    public ClusterCountryResource(ClusterCountryRepository clusterCountryRepository) {
        this.clusterCountryRepository = clusterCountryRepository;
    }

    /**
     * {@code POST  /cluster-countries} : Create a new clusterCountry.
     *
     * @param clusterCountry the clusterCountry to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new clusterCountry, or with status {@code 400 (Bad Request)} if the clusterCountry has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cluster-countries")
    public ResponseEntity<ClusterCountry> createClusterCountry(@RequestBody ClusterCountry clusterCountry) throws URISyntaxException {
        log.debug("REST request to save ClusterCountry : {}", clusterCountry);
        if (clusterCountry.getId() != null) {
            throw new BadRequestAlertException("A new clusterCountry cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClusterCountry result = clusterCountryRepository.save(clusterCountry);
        return ResponseEntity
            .created(new URI("/api/cluster-countries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cluster-countries/:id} : Updates an existing clusterCountry.
     *
     * @param id the id of the clusterCountry to save.
     * @param clusterCountry the clusterCountry to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clusterCountry,
     * or with status {@code 400 (Bad Request)} if the clusterCountry is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clusterCountry couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cluster-countries/{id}")
    public ResponseEntity<ClusterCountry> updateClusterCountry(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ClusterCountry clusterCountry
    ) throws URISyntaxException {
        log.debug("REST request to update ClusterCountry : {}, {}", id, clusterCountry);
        if (clusterCountry.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clusterCountry.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clusterCountryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ClusterCountry result = clusterCountryRepository.save(clusterCountry);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, clusterCountry.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cluster-countries/:id} : Partial updates given fields of an existing clusterCountry, field will ignore if it is null
     *
     * @param id the id of the clusterCountry to save.
     * @param clusterCountry the clusterCountry to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clusterCountry,
     * or with status {@code 400 (Bad Request)} if the clusterCountry is not valid,
     * or with status {@code 404 (Not Found)} if the clusterCountry is not found,
     * or with status {@code 500 (Internal Server Error)} if the clusterCountry couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cluster-countries/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ClusterCountry> partialUpdateClusterCountry(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ClusterCountry clusterCountry
    ) throws URISyntaxException {
        log.debug("REST request to partial update ClusterCountry partially : {}, {}", id, clusterCountry);
        if (clusterCountry.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clusterCountry.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clusterCountryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ClusterCountry> result = clusterCountryRepository
            .findById(clusterCountry.getId())
            .map(existingClusterCountry -> {
                if (clusterCountry.getName() != null) {
                    existingClusterCountry.setName(clusterCountry.getName());
                }

                return existingClusterCountry;
            })
            .map(clusterCountryRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, clusterCountry.getId().toString())
        );
    }

    /**
     * {@code GET  /cluster-countries} : get all the clusterCountries.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clusterCountries in body.
     */
    @GetMapping("/cluster-countries")
    public List<ClusterCountry> getAllClusterCountries() {
        log.debug("REST request to get all ClusterCountries");
        return clusterCountryRepository.findAll();
    }

    /**
     * {@code GET  /cluster-countries/:id} : get the "id" clusterCountry.
     *
     * @param id the id of the clusterCountry to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the clusterCountry, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cluster-countries/{id}")
    public ResponseEntity<ClusterCountry> getClusterCountry(@PathVariable Long id) {
        log.debug("REST request to get ClusterCountry : {}", id);
        Optional<ClusterCountry> clusterCountry = clusterCountryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(clusterCountry);
    }

    /**
     * {@code DELETE  /cluster-countries/:id} : delete the "id" clusterCountry.
     *
     * @param id the id of the clusterCountry to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cluster-countries/{id}")
    public ResponseEntity<Void> deleteClusterCountry(@PathVariable Long id) {
        log.debug("REST request to delete ClusterCountry : {}", id);
        clusterCountryRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
