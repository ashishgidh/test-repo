package com.chubb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.chubb.IntegrationTest;
import com.chubb.domain.ClusterCountry;
import com.chubb.repository.ClusterCountryRepository;
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
 * Integration tests for the {@link ClusterCountryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClusterCountryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cluster-countries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClusterCountryRepository clusterCountryRepository;

    @Autowired
    private MockMvc restClusterCountryMockMvc;

    private ClusterCountry clusterCountry;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClusterCountry createEntity() {
        ClusterCountry clusterCountry = new ClusterCountry().name(DEFAULT_NAME);
        return clusterCountry;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClusterCountry createUpdatedEntity() {
        ClusterCountry clusterCountry = new ClusterCountry().name(UPDATED_NAME);
        return clusterCountry;
    }

    @BeforeEach
    public void initTest() {
        clusterCountryRepository.deleteAll();
        clusterCountry = createEntity();
    }

    @Test
    void createClusterCountry() throws Exception {
        int databaseSizeBeforeCreate = clusterCountryRepository.findAll().size();
        // Create the ClusterCountry
        restClusterCountryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clusterCountry))
            )
            .andExpect(status().isCreated());

        // Validate the ClusterCountry in the database
        List<ClusterCountry> clusterCountryList = clusterCountryRepository.findAll();
        assertThat(clusterCountryList).hasSize(databaseSizeBeforeCreate + 1);
        ClusterCountry testClusterCountry = clusterCountryList.get(clusterCountryList.size() - 1);
        assertThat(testClusterCountry.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void createClusterCountryWithExistingId() throws Exception {
        // Create the ClusterCountry with an existing ID
        clusterCountry.setId(1L);

        int databaseSizeBeforeCreate = clusterCountryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClusterCountryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clusterCountry))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClusterCountry in the database
        List<ClusterCountry> clusterCountryList = clusterCountryRepository.findAll();
        assertThat(clusterCountryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllClusterCountries() throws Exception {
        // Initialize the database
        clusterCountryRepository.save(clusterCountry);

        // Get all the clusterCountryList
        restClusterCountryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clusterCountry.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    void getClusterCountry() throws Exception {
        // Initialize the database
        clusterCountryRepository.save(clusterCountry);

        // Get the clusterCountry
        restClusterCountryMockMvc
            .perform(get(ENTITY_API_URL_ID, clusterCountry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(clusterCountry.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    void getNonExistingClusterCountry() throws Exception {
        // Get the clusterCountry
        restClusterCountryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingClusterCountry() throws Exception {
        // Initialize the database
        clusterCountryRepository.save(clusterCountry);

        int databaseSizeBeforeUpdate = clusterCountryRepository.findAll().size();

        // Update the clusterCountry
        ClusterCountry updatedClusterCountry = clusterCountryRepository.findById(clusterCountry.getId()).orElseThrow();
        updatedClusterCountry.name(UPDATED_NAME);

        restClusterCountryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedClusterCountry.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedClusterCountry))
            )
            .andExpect(status().isOk());

        // Validate the ClusterCountry in the database
        List<ClusterCountry> clusterCountryList = clusterCountryRepository.findAll();
        assertThat(clusterCountryList).hasSize(databaseSizeBeforeUpdate);
        ClusterCountry testClusterCountry = clusterCountryList.get(clusterCountryList.size() - 1);
        assertThat(testClusterCountry.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void putNonExistingClusterCountry() throws Exception {
        int databaseSizeBeforeUpdate = clusterCountryRepository.findAll().size();
        clusterCountry.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClusterCountryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clusterCountry.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clusterCountry))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClusterCountry in the database
        List<ClusterCountry> clusterCountryList = clusterCountryRepository.findAll();
        assertThat(clusterCountryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchClusterCountry() throws Exception {
        int databaseSizeBeforeUpdate = clusterCountryRepository.findAll().size();
        clusterCountry.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClusterCountryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clusterCountry))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClusterCountry in the database
        List<ClusterCountry> clusterCountryList = clusterCountryRepository.findAll();
        assertThat(clusterCountryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamClusterCountry() throws Exception {
        int databaseSizeBeforeUpdate = clusterCountryRepository.findAll().size();
        clusterCountry.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClusterCountryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clusterCountry)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClusterCountry in the database
        List<ClusterCountry> clusterCountryList = clusterCountryRepository.findAll();
        assertThat(clusterCountryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateClusterCountryWithPatch() throws Exception {
        // Initialize the database
        clusterCountryRepository.save(clusterCountry);

        int databaseSizeBeforeUpdate = clusterCountryRepository.findAll().size();

        // Update the clusterCountry using partial update
        ClusterCountry partialUpdatedClusterCountry = new ClusterCountry();
        partialUpdatedClusterCountry.setId(clusterCountry.getId());

        restClusterCountryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClusterCountry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClusterCountry))
            )
            .andExpect(status().isOk());

        // Validate the ClusterCountry in the database
        List<ClusterCountry> clusterCountryList = clusterCountryRepository.findAll();
        assertThat(clusterCountryList).hasSize(databaseSizeBeforeUpdate);
        ClusterCountry testClusterCountry = clusterCountryList.get(clusterCountryList.size() - 1);
        assertThat(testClusterCountry.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void fullUpdateClusterCountryWithPatch() throws Exception {
        // Initialize the database
        clusterCountryRepository.save(clusterCountry);

        int databaseSizeBeforeUpdate = clusterCountryRepository.findAll().size();

        // Update the clusterCountry using partial update
        ClusterCountry partialUpdatedClusterCountry = new ClusterCountry();
        partialUpdatedClusterCountry.setId(clusterCountry.getId());

        partialUpdatedClusterCountry.name(UPDATED_NAME);

        restClusterCountryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClusterCountry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClusterCountry))
            )
            .andExpect(status().isOk());

        // Validate the ClusterCountry in the database
        List<ClusterCountry> clusterCountryList = clusterCountryRepository.findAll();
        assertThat(clusterCountryList).hasSize(databaseSizeBeforeUpdate);
        ClusterCountry testClusterCountry = clusterCountryList.get(clusterCountryList.size() - 1);
        assertThat(testClusterCountry.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void patchNonExistingClusterCountry() throws Exception {
        int databaseSizeBeforeUpdate = clusterCountryRepository.findAll().size();
        clusterCountry.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClusterCountryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, clusterCountry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clusterCountry))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClusterCountry in the database
        List<ClusterCountry> clusterCountryList = clusterCountryRepository.findAll();
        assertThat(clusterCountryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchClusterCountry() throws Exception {
        int databaseSizeBeforeUpdate = clusterCountryRepository.findAll().size();
        clusterCountry.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClusterCountryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clusterCountry))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClusterCountry in the database
        List<ClusterCountry> clusterCountryList = clusterCountryRepository.findAll();
        assertThat(clusterCountryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamClusterCountry() throws Exception {
        int databaseSizeBeforeUpdate = clusterCountryRepository.findAll().size();
        clusterCountry.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClusterCountryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(clusterCountry))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClusterCountry in the database
        List<ClusterCountry> clusterCountryList = clusterCountryRepository.findAll();
        assertThat(clusterCountryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteClusterCountry() throws Exception {
        // Initialize the database
        clusterCountryRepository.save(clusterCountry);

        int databaseSizeBeforeDelete = clusterCountryRepository.findAll().size();

        // Delete the clusterCountry
        restClusterCountryMockMvc
            .perform(delete(ENTITY_API_URL_ID, clusterCountry.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ClusterCountry> clusterCountryList = clusterCountryRepository.findAll();
        assertThat(clusterCountryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
