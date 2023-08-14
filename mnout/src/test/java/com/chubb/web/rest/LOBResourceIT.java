package com.chubb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.chubb.IntegrationTest;
import com.chubb.domain.LOB;
import com.chubb.repository.LOBRepository;
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
 * Integration tests for the {@link LOBResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LOBResourceIT {

    private static final String DEFAULT_WV_LOB_CODE = "AAAAAAAAAA";
    private static final String UPDATED_WV_LOB_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_WV_LOB_NAME = "AAAAAAAAAA";
    private static final String UPDATED_WV_LOB_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MAX_LOB_CODE = "AAAAAAAAAA";
    private static final String UPDATED_MAX_LOB_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_MAX_LOB_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MAX_LOB_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/lobs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LOBRepository lOBRepository;

    @Autowired
    private MockMvc restLOBMockMvc;

    private LOB lOB;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LOB createEntity() {
        LOB lOB = new LOB()
            .wvLobCode(DEFAULT_WV_LOB_CODE)
            .wvLobName(DEFAULT_WV_LOB_NAME)
            .maxLobCode(DEFAULT_MAX_LOB_CODE)
            .maxLobName(DEFAULT_MAX_LOB_NAME);
        return lOB;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LOB createUpdatedEntity() {
        LOB lOB = new LOB()
            .wvLobCode(UPDATED_WV_LOB_CODE)
            .wvLobName(UPDATED_WV_LOB_NAME)
            .maxLobCode(UPDATED_MAX_LOB_CODE)
            .maxLobName(UPDATED_MAX_LOB_NAME);
        return lOB;
    }

    @BeforeEach
    public void initTest() {
        lOBRepository.deleteAll();
        lOB = createEntity();
    }

    @Test
    void createLOB() throws Exception {
        int databaseSizeBeforeCreate = lOBRepository.findAll().size();
        // Create the LOB
        restLOBMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lOB)))
            .andExpect(status().isCreated());

        // Validate the LOB in the database
        List<LOB> lOBList = lOBRepository.findAll();
        assertThat(lOBList).hasSize(databaseSizeBeforeCreate + 1);
        LOB testLOB = lOBList.get(lOBList.size() - 1);
        assertThat(testLOB.getWvLobCode()).isEqualTo(DEFAULT_WV_LOB_CODE);
        assertThat(testLOB.getWvLobName()).isEqualTo(DEFAULT_WV_LOB_NAME);
        assertThat(testLOB.getMaxLobCode()).isEqualTo(DEFAULT_MAX_LOB_CODE);
        assertThat(testLOB.getMaxLobName()).isEqualTo(DEFAULT_MAX_LOB_NAME);
    }

    @Test
    void createLOBWithExistingId() throws Exception {
        // Create the LOB with an existing ID
        lOB.setId(1L);

        int databaseSizeBeforeCreate = lOBRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLOBMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lOB)))
            .andExpect(status().isBadRequest());

        // Validate the LOB in the database
        List<LOB> lOBList = lOBRepository.findAll();
        assertThat(lOBList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllLOBS() throws Exception {
        // Initialize the database
        lOBRepository.save(lOB);

        // Get all the lOBList
        restLOBMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lOB.getId().intValue())))
            .andExpect(jsonPath("$.[*].wvLobCode").value(hasItem(DEFAULT_WV_LOB_CODE)))
            .andExpect(jsonPath("$.[*].wvLobName").value(hasItem(DEFAULT_WV_LOB_NAME)))
            .andExpect(jsonPath("$.[*].maxLobCode").value(hasItem(DEFAULT_MAX_LOB_CODE)))
            .andExpect(jsonPath("$.[*].maxLobName").value(hasItem(DEFAULT_MAX_LOB_NAME)));
    }

    @Test
    void getLOB() throws Exception {
        // Initialize the database
        lOBRepository.save(lOB);

        // Get the lOB
        restLOBMockMvc
            .perform(get(ENTITY_API_URL_ID, lOB.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lOB.getId().intValue()))
            .andExpect(jsonPath("$.wvLobCode").value(DEFAULT_WV_LOB_CODE))
            .andExpect(jsonPath("$.wvLobName").value(DEFAULT_WV_LOB_NAME))
            .andExpect(jsonPath("$.maxLobCode").value(DEFAULT_MAX_LOB_CODE))
            .andExpect(jsonPath("$.maxLobName").value(DEFAULT_MAX_LOB_NAME));
    }

    @Test
    void getNonExistingLOB() throws Exception {
        // Get the lOB
        restLOBMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingLOB() throws Exception {
        // Initialize the database
        lOBRepository.save(lOB);

        int databaseSizeBeforeUpdate = lOBRepository.findAll().size();

        // Update the lOB
        LOB updatedLOB = lOBRepository.findById(lOB.getId()).orElseThrow();
        updatedLOB
            .wvLobCode(UPDATED_WV_LOB_CODE)
            .wvLobName(UPDATED_WV_LOB_NAME)
            .maxLobCode(UPDATED_MAX_LOB_CODE)
            .maxLobName(UPDATED_MAX_LOB_NAME);

        restLOBMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLOB.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLOB))
            )
            .andExpect(status().isOk());

        // Validate the LOB in the database
        List<LOB> lOBList = lOBRepository.findAll();
        assertThat(lOBList).hasSize(databaseSizeBeforeUpdate);
        LOB testLOB = lOBList.get(lOBList.size() - 1);
        assertThat(testLOB.getWvLobCode()).isEqualTo(UPDATED_WV_LOB_CODE);
        assertThat(testLOB.getWvLobName()).isEqualTo(UPDATED_WV_LOB_NAME);
        assertThat(testLOB.getMaxLobCode()).isEqualTo(UPDATED_MAX_LOB_CODE);
        assertThat(testLOB.getMaxLobName()).isEqualTo(UPDATED_MAX_LOB_NAME);
    }

    @Test
    void putNonExistingLOB() throws Exception {
        int databaseSizeBeforeUpdate = lOBRepository.findAll().size();
        lOB.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLOBMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lOB.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lOB))
            )
            .andExpect(status().isBadRequest());

        // Validate the LOB in the database
        List<LOB> lOBList = lOBRepository.findAll();
        assertThat(lOBList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchLOB() throws Exception {
        int databaseSizeBeforeUpdate = lOBRepository.findAll().size();
        lOB.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLOBMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lOB))
            )
            .andExpect(status().isBadRequest());

        // Validate the LOB in the database
        List<LOB> lOBList = lOBRepository.findAll();
        assertThat(lOBList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamLOB() throws Exception {
        int databaseSizeBeforeUpdate = lOBRepository.findAll().size();
        lOB.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLOBMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lOB)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LOB in the database
        List<LOB> lOBList = lOBRepository.findAll();
        assertThat(lOBList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateLOBWithPatch() throws Exception {
        // Initialize the database
        lOBRepository.save(lOB);

        int databaseSizeBeforeUpdate = lOBRepository.findAll().size();

        // Update the lOB using partial update
        LOB partialUpdatedLOB = new LOB();
        partialUpdatedLOB.setId(lOB.getId());

        partialUpdatedLOB.wvLobCode(UPDATED_WV_LOB_CODE).wvLobName(UPDATED_WV_LOB_NAME);

        restLOBMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLOB.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLOB))
            )
            .andExpect(status().isOk());

        // Validate the LOB in the database
        List<LOB> lOBList = lOBRepository.findAll();
        assertThat(lOBList).hasSize(databaseSizeBeforeUpdate);
        LOB testLOB = lOBList.get(lOBList.size() - 1);
        assertThat(testLOB.getWvLobCode()).isEqualTo(UPDATED_WV_LOB_CODE);
        assertThat(testLOB.getWvLobName()).isEqualTo(UPDATED_WV_LOB_NAME);
        assertThat(testLOB.getMaxLobCode()).isEqualTo(DEFAULT_MAX_LOB_CODE);
        assertThat(testLOB.getMaxLobName()).isEqualTo(DEFAULT_MAX_LOB_NAME);
    }

    @Test
    void fullUpdateLOBWithPatch() throws Exception {
        // Initialize the database
        lOBRepository.save(lOB);

        int databaseSizeBeforeUpdate = lOBRepository.findAll().size();

        // Update the lOB using partial update
        LOB partialUpdatedLOB = new LOB();
        partialUpdatedLOB.setId(lOB.getId());

        partialUpdatedLOB
            .wvLobCode(UPDATED_WV_LOB_CODE)
            .wvLobName(UPDATED_WV_LOB_NAME)
            .maxLobCode(UPDATED_MAX_LOB_CODE)
            .maxLobName(UPDATED_MAX_LOB_NAME);

        restLOBMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLOB.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLOB))
            )
            .andExpect(status().isOk());

        // Validate the LOB in the database
        List<LOB> lOBList = lOBRepository.findAll();
        assertThat(lOBList).hasSize(databaseSizeBeforeUpdate);
        LOB testLOB = lOBList.get(lOBList.size() - 1);
        assertThat(testLOB.getWvLobCode()).isEqualTo(UPDATED_WV_LOB_CODE);
        assertThat(testLOB.getWvLobName()).isEqualTo(UPDATED_WV_LOB_NAME);
        assertThat(testLOB.getMaxLobCode()).isEqualTo(UPDATED_MAX_LOB_CODE);
        assertThat(testLOB.getMaxLobName()).isEqualTo(UPDATED_MAX_LOB_NAME);
    }

    @Test
    void patchNonExistingLOB() throws Exception {
        int databaseSizeBeforeUpdate = lOBRepository.findAll().size();
        lOB.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLOBMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lOB.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lOB))
            )
            .andExpect(status().isBadRequest());

        // Validate the LOB in the database
        List<LOB> lOBList = lOBRepository.findAll();
        assertThat(lOBList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchLOB() throws Exception {
        int databaseSizeBeforeUpdate = lOBRepository.findAll().size();
        lOB.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLOBMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lOB))
            )
            .andExpect(status().isBadRequest());

        // Validate the LOB in the database
        List<LOB> lOBList = lOBRepository.findAll();
        assertThat(lOBList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamLOB() throws Exception {
        int databaseSizeBeforeUpdate = lOBRepository.findAll().size();
        lOB.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLOBMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(lOB)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LOB in the database
        List<LOB> lOBList = lOBRepository.findAll();
        assertThat(lOBList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteLOB() throws Exception {
        // Initialize the database
        lOBRepository.save(lOB);

        int databaseSizeBeforeDelete = lOBRepository.findAll().size();

        // Delete the lOB
        restLOBMockMvc.perform(delete(ENTITY_API_URL_ID, lOB.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LOB> lOBList = lOBRepository.findAll();
        assertThat(lOBList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
