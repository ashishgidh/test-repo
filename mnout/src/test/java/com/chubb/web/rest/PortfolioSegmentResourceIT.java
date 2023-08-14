package com.chubb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.chubb.IntegrationTest;
import com.chubb.domain.PortfolioSegment;
import com.chubb.repository.PortfolioSegmentRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link PortfolioSegmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PortfolioSegmentResourceIT {

    private static final Long DEFAULT_PORTFOLIO_SIGMENT = 1L;
    private static final Long UPDATED_PORTFOLIO_SIGMENT = 2L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SEGMENT = "AAAAAAAAAA";
    private static final String UPDATED_SEGMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/portfolio-segments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private PortfolioSegmentRepository portfolioSegmentRepository;

    @Autowired
    private MockMvc restPortfolioSegmentMockMvc;

    private PortfolioSegment portfolioSegment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PortfolioSegment createEntity() {
        PortfolioSegment portfolioSegment = new PortfolioSegment()
            .portfolioSigment(DEFAULT_PORTFOLIO_SIGMENT)
            .name(DEFAULT_NAME)
            .segment(DEFAULT_SEGMENT);
        return portfolioSegment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PortfolioSegment createUpdatedEntity() {
        PortfolioSegment portfolioSegment = new PortfolioSegment()
            .portfolioSigment(UPDATED_PORTFOLIO_SIGMENT)
            .name(UPDATED_NAME)
            .segment(UPDATED_SEGMENT);
        return portfolioSegment;
    }

    @BeforeEach
    public void initTest() {
        portfolioSegmentRepository.deleteAll();
        portfolioSegment = createEntity();
    }

    @Test
    void createPortfolioSegment() throws Exception {
        int databaseSizeBeforeCreate = portfolioSegmentRepository.findAll().size();
        // Create the PortfolioSegment
        restPortfolioSegmentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(portfolioSegment))
            )
            .andExpect(status().isCreated());

        // Validate the PortfolioSegment in the database
        List<PortfolioSegment> portfolioSegmentList = portfolioSegmentRepository.findAll();
        assertThat(portfolioSegmentList).hasSize(databaseSizeBeforeCreate + 1);
        PortfolioSegment testPortfolioSegment = portfolioSegmentList.get(portfolioSegmentList.size() - 1);
        assertThat(testPortfolioSegment.getPortfolioSigment()).isEqualTo(DEFAULT_PORTFOLIO_SIGMENT);
        assertThat(testPortfolioSegment.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPortfolioSegment.getSegment()).isEqualTo(DEFAULT_SEGMENT);
    }

    @Test
    void createPortfolioSegmentWithExistingId() throws Exception {
        // Create the PortfolioSegment with an existing ID
        portfolioSegment.setId("existing_id");

        int databaseSizeBeforeCreate = portfolioSegmentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPortfolioSegmentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(portfolioSegment))
            )
            .andExpect(status().isBadRequest());

        // Validate the PortfolioSegment in the database
        List<PortfolioSegment> portfolioSegmentList = portfolioSegmentRepository.findAll();
        assertThat(portfolioSegmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllPortfolioSegments() throws Exception {
        // Initialize the database
        portfolioSegmentRepository.save(portfolioSegment);

        // Get all the portfolioSegmentList
        restPortfolioSegmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(portfolioSegment.getId())))
            .andExpect(jsonPath("$.[*].portfolioSigment").value(hasItem(DEFAULT_PORTFOLIO_SIGMENT.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].segment").value(hasItem(DEFAULT_SEGMENT)));
    }

    @Test
    void getPortfolioSegment() throws Exception {
        // Initialize the database
        portfolioSegmentRepository.save(portfolioSegment);

        // Get the portfolioSegment
        restPortfolioSegmentMockMvc
            .perform(get(ENTITY_API_URL_ID, portfolioSegment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(portfolioSegment.getId()))
            .andExpect(jsonPath("$.portfolioSigment").value(DEFAULT_PORTFOLIO_SIGMENT.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.segment").value(DEFAULT_SEGMENT));
    }

    @Test
    void getNonExistingPortfolioSegment() throws Exception {
        // Get the portfolioSegment
        restPortfolioSegmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingPortfolioSegment() throws Exception {
        // Initialize the database
        portfolioSegmentRepository.save(portfolioSegment);

        int databaseSizeBeforeUpdate = portfolioSegmentRepository.findAll().size();

        // Update the portfolioSegment
        PortfolioSegment updatedPortfolioSegment = portfolioSegmentRepository.findById(portfolioSegment.getId()).orElseThrow();
        updatedPortfolioSegment.portfolioSigment(UPDATED_PORTFOLIO_SIGMENT).name(UPDATED_NAME).segment(UPDATED_SEGMENT);

        restPortfolioSegmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPortfolioSegment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPortfolioSegment))
            )
            .andExpect(status().isOk());

        // Validate the PortfolioSegment in the database
        List<PortfolioSegment> portfolioSegmentList = portfolioSegmentRepository.findAll();
        assertThat(portfolioSegmentList).hasSize(databaseSizeBeforeUpdate);
        PortfolioSegment testPortfolioSegment = portfolioSegmentList.get(portfolioSegmentList.size() - 1);
        assertThat(testPortfolioSegment.getPortfolioSigment()).isEqualTo(UPDATED_PORTFOLIO_SIGMENT);
        assertThat(testPortfolioSegment.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPortfolioSegment.getSegment()).isEqualTo(UPDATED_SEGMENT);
    }

    @Test
    void putNonExistingPortfolioSegment() throws Exception {
        int databaseSizeBeforeUpdate = portfolioSegmentRepository.findAll().size();
        portfolioSegment.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPortfolioSegmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, portfolioSegment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(portfolioSegment))
            )
            .andExpect(status().isBadRequest());

        // Validate the PortfolioSegment in the database
        List<PortfolioSegment> portfolioSegmentList = portfolioSegmentRepository.findAll();
        assertThat(portfolioSegmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPortfolioSegment() throws Exception {
        int databaseSizeBeforeUpdate = portfolioSegmentRepository.findAll().size();
        portfolioSegment.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPortfolioSegmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(portfolioSegment))
            )
            .andExpect(status().isBadRequest());

        // Validate the PortfolioSegment in the database
        List<PortfolioSegment> portfolioSegmentList = portfolioSegmentRepository.findAll();
        assertThat(portfolioSegmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPortfolioSegment() throws Exception {
        int databaseSizeBeforeUpdate = portfolioSegmentRepository.findAll().size();
        portfolioSegment.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPortfolioSegmentMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(portfolioSegment))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PortfolioSegment in the database
        List<PortfolioSegment> portfolioSegmentList = portfolioSegmentRepository.findAll();
        assertThat(portfolioSegmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePortfolioSegmentWithPatch() throws Exception {
        // Initialize the database
        portfolioSegmentRepository.save(portfolioSegment);

        int databaseSizeBeforeUpdate = portfolioSegmentRepository.findAll().size();

        // Update the portfolioSegment using partial update
        PortfolioSegment partialUpdatedPortfolioSegment = new PortfolioSegment();
        partialUpdatedPortfolioSegment.setId(portfolioSegment.getId());

        partialUpdatedPortfolioSegment.portfolioSigment(UPDATED_PORTFOLIO_SIGMENT).name(UPDATED_NAME).segment(UPDATED_SEGMENT);

        restPortfolioSegmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPortfolioSegment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPortfolioSegment))
            )
            .andExpect(status().isOk());

        // Validate the PortfolioSegment in the database
        List<PortfolioSegment> portfolioSegmentList = portfolioSegmentRepository.findAll();
        assertThat(portfolioSegmentList).hasSize(databaseSizeBeforeUpdate);
        PortfolioSegment testPortfolioSegment = portfolioSegmentList.get(portfolioSegmentList.size() - 1);
        assertThat(testPortfolioSegment.getPortfolioSigment()).isEqualTo(UPDATED_PORTFOLIO_SIGMENT);
        assertThat(testPortfolioSegment.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPortfolioSegment.getSegment()).isEqualTo(UPDATED_SEGMENT);
    }

    @Test
    void fullUpdatePortfolioSegmentWithPatch() throws Exception {
        // Initialize the database
        portfolioSegmentRepository.save(portfolioSegment);

        int databaseSizeBeforeUpdate = portfolioSegmentRepository.findAll().size();

        // Update the portfolioSegment using partial update
        PortfolioSegment partialUpdatedPortfolioSegment = new PortfolioSegment();
        partialUpdatedPortfolioSegment.setId(portfolioSegment.getId());

        partialUpdatedPortfolioSegment.portfolioSigment(UPDATED_PORTFOLIO_SIGMENT).name(UPDATED_NAME).segment(UPDATED_SEGMENT);

        restPortfolioSegmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPortfolioSegment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPortfolioSegment))
            )
            .andExpect(status().isOk());

        // Validate the PortfolioSegment in the database
        List<PortfolioSegment> portfolioSegmentList = portfolioSegmentRepository.findAll();
        assertThat(portfolioSegmentList).hasSize(databaseSizeBeforeUpdate);
        PortfolioSegment testPortfolioSegment = portfolioSegmentList.get(portfolioSegmentList.size() - 1);
        assertThat(testPortfolioSegment.getPortfolioSigment()).isEqualTo(UPDATED_PORTFOLIO_SIGMENT);
        assertThat(testPortfolioSegment.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPortfolioSegment.getSegment()).isEqualTo(UPDATED_SEGMENT);
    }

    @Test
    void patchNonExistingPortfolioSegment() throws Exception {
        int databaseSizeBeforeUpdate = portfolioSegmentRepository.findAll().size();
        portfolioSegment.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPortfolioSegmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, portfolioSegment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(portfolioSegment))
            )
            .andExpect(status().isBadRequest());

        // Validate the PortfolioSegment in the database
        List<PortfolioSegment> portfolioSegmentList = portfolioSegmentRepository.findAll();
        assertThat(portfolioSegmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPortfolioSegment() throws Exception {
        int databaseSizeBeforeUpdate = portfolioSegmentRepository.findAll().size();
        portfolioSegment.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPortfolioSegmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(portfolioSegment))
            )
            .andExpect(status().isBadRequest());

        // Validate the PortfolioSegment in the database
        List<PortfolioSegment> portfolioSegmentList = portfolioSegmentRepository.findAll();
        assertThat(portfolioSegmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPortfolioSegment() throws Exception {
        int databaseSizeBeforeUpdate = portfolioSegmentRepository.findAll().size();
        portfolioSegment.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPortfolioSegmentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(portfolioSegment))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PortfolioSegment in the database
        List<PortfolioSegment> portfolioSegmentList = portfolioSegmentRepository.findAll();
        assertThat(portfolioSegmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePortfolioSegment() throws Exception {
        // Initialize the database
        portfolioSegmentRepository.save(portfolioSegment);

        int databaseSizeBeforeDelete = portfolioSegmentRepository.findAll().size();

        // Delete the portfolioSegment
        restPortfolioSegmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, portfolioSegment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PortfolioSegment> portfolioSegmentList = portfolioSegmentRepository.findAll();
        assertThat(portfolioSegmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
