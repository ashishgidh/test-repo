package com.chubb.web.rest;

import com.chubb.domain.PortfolioSegment;
import com.chubb.repository.PortfolioSegmentRepository;
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
 * REST controller for managing {@link com.chubb.domain.PortfolioSegment}.
 */
@RestController
@RequestMapping("/api")
public class PortfolioSegmentResource {

    private final Logger log = LoggerFactory.getLogger(PortfolioSegmentResource.class);

    private static final String ENTITY_NAME = "portfolioSegment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PortfolioSegmentRepository portfolioSegmentRepository;

    public PortfolioSegmentResource(PortfolioSegmentRepository portfolioSegmentRepository) {
        this.portfolioSegmentRepository = portfolioSegmentRepository;
    }

    /**
     * {@code POST  /portfolio-segments} : Create a new portfolioSegment.
     *
     * @param portfolioSegment the portfolioSegment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new portfolioSegment, or with status {@code 400 (Bad Request)} if the portfolioSegment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/portfolio-segments")
    public ResponseEntity<PortfolioSegment> createPortfolioSegment(@RequestBody PortfolioSegment portfolioSegment)
        throws URISyntaxException {
        log.debug("REST request to save PortfolioSegment : {}", portfolioSegment);
        if (portfolioSegment.getId() != null) {
            throw new BadRequestAlertException("A new portfolioSegment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PortfolioSegment result = portfolioSegmentRepository.save(portfolioSegment);
        return ResponseEntity
            .created(new URI("/api/portfolio-segments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /portfolio-segments/:id} : Updates an existing portfolioSegment.
     *
     * @param id the id of the portfolioSegment to save.
     * @param portfolioSegment the portfolioSegment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated portfolioSegment,
     * or with status {@code 400 (Bad Request)} if the portfolioSegment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the portfolioSegment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/portfolio-segments/{id}")
    public ResponseEntity<PortfolioSegment> updatePortfolioSegment(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody PortfolioSegment portfolioSegment
    ) throws URISyntaxException {
        log.debug("REST request to update PortfolioSegment : {}, {}", id, portfolioSegment);
        if (portfolioSegment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, portfolioSegment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!portfolioSegmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PortfolioSegment result = portfolioSegmentRepository.save(portfolioSegment);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, portfolioSegment.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /portfolio-segments/:id} : Partial updates given fields of an existing portfolioSegment, field will ignore if it is null
     *
     * @param id the id of the portfolioSegment to save.
     * @param portfolioSegment the portfolioSegment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated portfolioSegment,
     * or with status {@code 400 (Bad Request)} if the portfolioSegment is not valid,
     * or with status {@code 404 (Not Found)} if the portfolioSegment is not found,
     * or with status {@code 500 (Internal Server Error)} if the portfolioSegment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/portfolio-segments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PortfolioSegment> partialUpdatePortfolioSegment(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody PortfolioSegment portfolioSegment
    ) throws URISyntaxException {
        log.debug("REST request to partial update PortfolioSegment partially : {}, {}", id, portfolioSegment);
        if (portfolioSegment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, portfolioSegment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!portfolioSegmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PortfolioSegment> result = portfolioSegmentRepository
            .findById(portfolioSegment.getId())
            .map(existingPortfolioSegment -> {
                if (portfolioSegment.getPortfolioSigment() != null) {
                    existingPortfolioSegment.setPortfolioSigment(portfolioSegment.getPortfolioSigment());
                }
                if (portfolioSegment.getName() != null) {
                    existingPortfolioSegment.setName(portfolioSegment.getName());
                }
                if (portfolioSegment.getSegment() != null) {
                    existingPortfolioSegment.setSegment(portfolioSegment.getSegment());
                }

                return existingPortfolioSegment;
            })
            .map(portfolioSegmentRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, portfolioSegment.getId())
        );
    }

    /**
     * {@code GET  /portfolio-segments} : get all the portfolioSegments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of portfolioSegments in body.
     */
    @GetMapping("/portfolio-segments")
    public List<PortfolioSegment> getAllPortfolioSegments() {
        log.debug("REST request to get all PortfolioSegments");
        return portfolioSegmentRepository.findAll();
    }

    /**
     * {@code GET  /portfolio-segments/:id} : get the "id" portfolioSegment.
     *
     * @param id the id of the portfolioSegment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the portfolioSegment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/portfolio-segments/{id}")
    public ResponseEntity<PortfolioSegment> getPortfolioSegment(@PathVariable String id) {
        log.debug("REST request to get PortfolioSegment : {}", id);
        Optional<PortfolioSegment> portfolioSegment = portfolioSegmentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(portfolioSegment);
    }

    /**
     * {@code DELETE  /portfolio-segments/:id} : delete the "id" portfolioSegment.
     *
     * @param id the id of the portfolioSegment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/portfolio-segments/{id}")
    public ResponseEntity<Void> deletePortfolioSegment(@PathVariable String id) {
        log.debug("REST request to delete PortfolioSegment : {}", id);
        portfolioSegmentRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
