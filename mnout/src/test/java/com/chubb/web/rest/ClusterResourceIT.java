package com.chubb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.chubb.IntegrationTest;
import com.chubb.domain.Cluster;
import com.chubb.repository.ClusterRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link ClusterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClusterResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/clusters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClusterRepository clusterRepository;

    @Autowired
    private MockMvc restClusterMockMvc;

    private Cluster cluster;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cluster createEntity() {
        Cluster cluster = new Cluster().name(DEFAULT_NAME);
        return cluster;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cluster createUpdatedEntity() {
        Cluster cluster = new Cluster().name(UPDATED_NAME);
        return cluster;
    }

    @BeforeEach
    public void initTest() {
        clusterRepository.deleteAll();
        cluster = createEntity();
    }

    @Test
    void createCluster() throws Exception {
        int databaseSizeBeforeCreate = clusterRepository.findAll().size();
        // Create the Cluster
        restClusterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cluster)))
            .andExpect(status().isCreated());

        // Validate the Cluster in the database
        List<Cluster> clusterList = clusterRepository.findAll();
        assertThat(clusterList).hasSize(databaseSizeBeforeCreate + 1);
        Cluster testCluster = clusterList.get(clusterList.size() - 1);
        assertThat(testCluster.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void createClusterWithExistingId() throws Exception {
        // Create the Cluster with an existing ID
        cluster.setId(1L);

        int databaseSizeBeforeCreate = clusterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClusterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cluster)))
            .andExpect(status().isBadRequest());

        // Validate the Cluster in the database
        List<Cluster> clusterList = clusterRepository.findAll();
        assertThat(clusterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllClusters() throws Exception {
        // Initialize the database
        clusterRepository.save(cluster);

        // Get all the clusterList
        restClusterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cluster.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    void getCluster() throws Exception {
        // Initialize the database
        clusterRepository.save(cluster);

        // Get the cluster
        restClusterMockMvc
            .perform(get(ENTITY_API_URL_ID, cluster.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cluster.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    void getNonExistingCluster() throws Exception {
        // Get the cluster
        restClusterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingCluster() throws Exception {
        // Initialize the database
        clusterRepository.save(cluster);

        int databaseSizeBeforeUpdate = clusterRepository.findAll().size();

        // Update the cluster
        Cluster updatedCluster = clusterRepository.findById(cluster.getId()).orElseThrow();
        updatedCluster.name(UPDATED_NAME);

        restClusterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCluster.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCluster))
            )
            .andExpect(status().isOk());

        // Validate the Cluster in the database
        List<Cluster> clusterList = clusterRepository.findAll();
        assertThat(clusterList).hasSize(databaseSizeBeforeUpdate);
        Cluster testCluster = clusterList.get(clusterList.size() - 1);
        assertThat(testCluster.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void putNonExistingCluster() throws Exception {
        int databaseSizeBeforeUpdate = clusterRepository.findAll().size();
        cluster.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClusterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cluster.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cluster))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cluster in the database
        List<Cluster> clusterList = clusterRepository.findAll();
        assertThat(clusterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCluster() throws Exception {
        int databaseSizeBeforeUpdate = clusterRepository.findAll().size();
        cluster.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClusterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cluster))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cluster in the database
        List<Cluster> clusterList = clusterRepository.findAll();
        assertThat(clusterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCluster() throws Exception {
        int databaseSizeBeforeUpdate = clusterRepository.findAll().size();
        cluster.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClusterMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cluster)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cluster in the database
        List<Cluster> clusterList = clusterRepository.findAll();
        assertThat(clusterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateClusterWithPatch() throws Exception {
        // Initialize the database
        clusterRepository.save(cluster);

        int databaseSizeBeforeUpdate = clusterRepository.findAll().size();

        // Update the cluster using partial update
        Cluster partialUpdatedCluster = new Cluster();
        partialUpdatedCluster.setId(cluster.getId());

        restClusterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCluster.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCluster))
            )
            .andExpect(status().isOk());

        // Validate the Cluster in the database
        List<Cluster> clusterList = clusterRepository.findAll();
        assertThat(clusterList).hasSize(databaseSizeBeforeUpdate);
        Cluster testCluster = clusterList.get(clusterList.size() - 1);
        assertThat(testCluster.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void fullUpdateClusterWithPatch() throws Exception {
        // Initialize the database
        clusterRepository.save(cluster);

        int databaseSizeBeforeUpdate = clusterRepository.findAll().size();

        // Update the cluster using partial update
        Cluster partialUpdatedCluster = new Cluster();
        partialUpdatedCluster.setId(cluster.getId());

        partialUpdatedCluster.name(UPDATED_NAME);

        restClusterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCluster.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCluster))
            )
            .andExpect(status().isOk());

        // Validate the Cluster in the database
        List<Cluster> clusterList = clusterRepository.findAll();
        assertThat(clusterList).hasSize(databaseSizeBeforeUpdate);
        Cluster testCluster = clusterList.get(clusterList.size() - 1);
        assertThat(testCluster.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void patchNonExistingCluster() throws Exception {
        int databaseSizeBeforeUpdate = clusterRepository.findAll().size();
        cluster.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClusterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cluster.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cluster))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cluster in the database
        List<Cluster> clusterList = clusterRepository.findAll();
        assertThat(clusterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCluster() throws Exception {
        int databaseSizeBeforeUpdate = clusterRepository.findAll().size();
        cluster.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClusterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cluster))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cluster in the database
        List<Cluster> clusterList = clusterRepository.findAll();
        assertThat(clusterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCluster() throws Exception {
        int databaseSizeBeforeUpdate = clusterRepository.findAll().size();
        cluster.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClusterMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cluster)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cluster in the database
        List<Cluster> clusterList = clusterRepository.findAll();
        assertThat(clusterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCluster() throws Exception {
        // Initialize the database
        clusterRepository.save(cluster);

        int databaseSizeBeforeDelete = clusterRepository.findAll().size();

        // Delete the cluster
        restClusterMockMvc
            .perform(delete(ENTITY_API_URL_ID, cluster.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cluster> clusterList = clusterRepository.findAll();
        assertThat(clusterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
