package com.chubb.web.rest;

import com.chubb.domain.Cluster;
import com.chubb.repository.ClusterRepository;
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
 * REST controller for managing {@link com.chubb.domain.Cluster}.
 */
@RestController
@RequestMapping("/api")
public class ClusterResource {

    private final Logger log = LoggerFactory.getLogger(ClusterResource.class);

    private static final String ENTITY_NAME = "cluster";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClusterRepository clusterRepository;

    public ClusterResource(ClusterRepository clusterRepository) {
        this.clusterRepository = clusterRepository;
    }

    /**
     * {@code POST  /clusters} : Create a new cluster.
     *
     * @param cluster the cluster to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cluster, or with status {@code 400 (Bad Request)} if the cluster has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/clusters")
    public ResponseEntity<Cluster> createCluster(@RequestBody Cluster cluster) throws URISyntaxException {
        log.debug("REST request to save Cluster : {}", cluster);
        if (cluster.getId() != null) {
            throw new BadRequestAlertException("A new cluster cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cluster result = clusterRepository.save(cluster);
        return ResponseEntity
            .created(new URI("/api/clusters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /clusters/:id} : Updates an existing cluster.
     *
     * @param id the id of the cluster to save.
     * @param cluster the cluster to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cluster,
     * or with status {@code 400 (Bad Request)} if the cluster is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cluster couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/clusters/{id}")
    public ResponseEntity<Cluster> updateCluster(@PathVariable(value = "id", required = false) final Long id, @RequestBody Cluster cluster)
        throws URISyntaxException {
        log.debug("REST request to update Cluster : {}, {}", id, cluster);
        if (cluster.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cluster.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clusterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Cluster result = clusterRepository.save(cluster);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cluster.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /clusters/:id} : Partial updates given fields of an existing cluster, field will ignore if it is null
     *
     * @param id the id of the cluster to save.
     * @param cluster the cluster to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cluster,
     * or with status {@code 400 (Bad Request)} if the cluster is not valid,
     * or with status {@code 404 (Not Found)} if the cluster is not found,
     * or with status {@code 500 (Internal Server Error)} if the cluster couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/clusters/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Cluster> partialUpdateCluster(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Cluster cluster
    ) throws URISyntaxException {
        log.debug("REST request to partial update Cluster partially : {}, {}", id, cluster);
        if (cluster.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cluster.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clusterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Cluster> result = clusterRepository
            .findById(cluster.getId())
            .map(existingCluster -> {
                if (cluster.getName() != null) {
                    existingCluster.setName(cluster.getName());
                }

                return existingCluster;
            })
            .map(clusterRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cluster.getId().toString())
        );
    }

    /**
     * {@code GET  /clusters} : get all the clusters.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clusters in body.
     */
    @GetMapping("/clusters")
    public List<Cluster> getAllClusters() {
        log.debug("REST request to get all Clusters");
        return clusterRepository.findAll();
    }

    /**
     * {@code GET  /clusters/:id} : get the "id" cluster.
     *
     * @param id the id of the cluster to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cluster, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/clusters/{id}")
    public ResponseEntity<Cluster> getCluster(@PathVariable Long id) {
        log.debug("REST request to get Cluster : {}", id);
        Optional<Cluster> cluster = clusterRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(cluster);
    }

    /**
     * {@code DELETE  /clusters/:id} : delete the "id" cluster.
     *
     * @param id the id of the cluster to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/clusters/{id}")
    public ResponseEntity<Void> deleteCluster(@PathVariable Long id) {
        log.debug("REST request to delete Cluster : {}", id);
        clusterRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
